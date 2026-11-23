package br.edu.urna.rest;

import br.edu.urna.dados.PersistenciaException;
import br.edu.urna.dados.RepositorioUrna;
import br.edu.urna.dominio.Candidato;
import br.edu.urna.dominio.Voto;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class FirestoreProxyRest implements RepositorioUrna, AutoCloseable {
    private static final Duration LIMITE_DA_REQUISICAO = Duration.ofSeconds(10);

    private final ConfiguracaoFirebaseRest config;
    private final HttpClient cliente;
    private final URI login;
    private final URI documentos;
    private String token;
    private Instant expiraEm = Instant.EPOCH;

    public static FirestoreProxyRest abrir(Path caminho) throws IOException {
        var config = ConfiguracaoFirebaseRest.carregar(caminho);
        var login = URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key="
                + URLEncoder.encode(config.chaveApi(), StandardCharsets.UTF_8));
        var documentos = URI.create("https://firestore.googleapis.com/v1/projects/"
                + config.projeto() + "/databases/(default)/documents/");
        var cliente = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
        return new FirestoreProxyRest(config, cliente, login, documentos);
    }

    FirestoreProxyRest(ConfiguracaoFirebaseRest config, HttpClient cliente, URI login, URI documentos) {
        this.config = config;
        this.cliente = cliente;
        this.login = login;
        this.documentos = documentos;
    }

    @Override
    public List<Candidato> listarCandidatos() throws PersistenciaException {
        List<Candidato> candidatos = new ArrayList<>();
        String pagina = "";
        do {
            String caminho = "candidatos_demo?pageSize=100";
            if (!pagina.isEmpty()) {
                caminho += "&pageToken=" + URLEncoder.encode(pagina, StandardCharsets.UTF_8);
            }
            var consulta = HttpRequest.newBuilder(documentos.resolve(caminho)).GET();
            var resposta = enviarAoFirestore(consulta);
            try {
                var documentosDaPagina = resposta.getAsJsonArray("documents");
                if (documentosDaPagina != null) {
                    for (var item : documentosDaPagina) {
                        var doc = item.getAsJsonObject();
                        var nomeCompleto = texto(doc, "name");
                        var numero = nomeCompleto.substring(nomeCompleto.lastIndexOf('/') + 1);
                        var campos = doc.getAsJsonObject("fields");
                        if (campos == null) {
                            throw new IllegalArgumentException("O documento está sem campos.");
                        }
                        candidatos.add(new Candidato(numero, texto(campos.getAsJsonObject("nome"), "stringValue"),
                                texto(campos.getAsJsonObject("partido"), "stringValue"),
                                texto(campos.getAsJsonObject("foto"), "stringValue")));
                    }
                }
                pagina = resposta.has("nextPageToken") ? texto(resposta, "nextPageToken") : "";
            } catch (IllegalArgumentException | IllegalStateException | ClassCastException erro) {
                throw new PersistenciaException("Confira os documentos e os campos da coleção candidatos_demo.", erro);
            }
        } while (!pagina.isEmpty());

        candidatos.sort(Comparator.comparing(Candidato::numero));
        return List.copyOf(candidatos);
    }

    @Override
    public void salvar(Voto voto) throws PersistenciaException {
        // No Firestore REST, cada campo declara seu tipo. Aqui os dois são strings.
        var campos = new JsonObject();
        campos.add("tipo", campoTexto(voto.tipo().name()));
        campos.add("numero", campoTexto(voto.numero()));
        var documento = new JsonObject();
        documento.add("fields", campos);

        // PATCH com ID fixo grava no mesmo lugar ao tentar de novo.
        var pedido = HttpRequest.newBuilder(documentos.resolve("votos_demo/" + voto.id()))
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(documento.toString()));
        enviarAoFirestore(pedido);
    }

    private JsonObject enviarAoFirestore(HttpRequest.Builder pedido) throws PersistenciaException {
        var tokenAtual = obterToken();
        var resposta = enviar(pedido.header("Authorization", "Bearer " + tokenAtual));
        int status = resposta.statusCode();
        if (status == 401) {
            invalidarToken(tokenAtual);
            throw new PersistenciaException("A sessão do Firebase expirou. Tente a operação novamente.", null);
        }
        if (status == 403) {
            throw new PersistenciaException("Acesso negado. Confira o UID e as regras publicadas do Firestore.", null);
        }
        if (status < 200 || status >= 300) {
            throw new PersistenciaException("O Firestore respondeu com HTTP " + status + ".", null);
        }
        return lerObjeto(resposta.body(), "A resposta do Firestore não veio no formato esperado.");
    }

    private synchronized String obterToken() throws PersistenciaException {
        if (token != null && Instant.now().plusSeconds(30).isBefore(expiraEm)) {
            return token;
        }

        var dados = new JsonObject();
        dados.addProperty("email", config.email());
        dados.addProperty("password", config.senha());
        dados.addProperty("returnSecureToken", true);
        var pedido = HttpRequest.newBuilder(login)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(dados.toString()));
        var resposta = enviar(pedido);
        if (resposta.statusCode() != 200) {
            throw new PersistenciaException("Firebase Authentication respondeu com HTTP " + resposta.statusCode()
                    + ". Confira a chave Web e a conta de teste.", null);
        }

        var sessao = lerObjeto(resposta.body(), "O Firebase Authentication devolveu uma sessão inválida.");
        try {
            String idToken = texto(sessao, "idToken");
            long segundos = Long.parseLong(texto(sessao, "expiresIn"));
            if (idToken.isBlank() || segundos <= 0) {
                throw new IllegalArgumentException("Token vazio ou com duração inválida.");
            }
            expiraEm = Instant.now().plusSeconds(segundos);
            token = idToken;
            return token;
        } catch (IllegalArgumentException erro) {
            // A mensagem de uma conversão pode incluir dados da resposta. Não repassar.
            throw new PersistenciaException("O Firebase Authentication devolveu uma sessão inválida.", null);
        }
    }

    private synchronized void invalidarToken(String tokenAtual) {
        if (tokenAtual.equals(token)) {
            token = null;
        }
    }

    private HttpResponse<String> enviar(HttpRequest.Builder pedido) throws PersistenciaException {
        try {
            return cliente.send(pedido.timeout(LIMITE_DA_REQUISICAO).build(),
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (HttpTimeoutException erro) {
            throw new PersistenciaException("O Firebase demorou para responder. Confira a conexão.", erro);
        } catch (IOException erro) {
            throw new PersistenciaException("Falha de rede ao acessar o Firebase.", erro);
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new PersistenciaException("A operação com o Firebase foi interrompida.", erro);
        }
    }

    private static JsonObject lerObjeto(String json, String mensagem) throws PersistenciaException {
        try {
            return JsonParser.parseString(json).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException erro) {
            // O conteúdo recebido pode conter credenciais de sessão.
            throw new PersistenciaException(mensagem, null);
        }
    }

    private static String texto(JsonObject objeto, String nome) {
        var valor = objeto == null ? null : objeto.get(nome);
        if (valor == null || !valor.isJsonPrimitive() || !valor.getAsJsonPrimitive().isString()) {
            throw new IllegalArgumentException("Campo textual ausente ou inválido: " + nome);
        }
        return valor.getAsString();
    }

    private static JsonObject campoTexto(String valor) {
        var campo = new JsonObject();
        campo.addProperty("stringValue", valor);
        return campo;
    }

    @Override
    public void close() {
        cliente.close();
    }
}
