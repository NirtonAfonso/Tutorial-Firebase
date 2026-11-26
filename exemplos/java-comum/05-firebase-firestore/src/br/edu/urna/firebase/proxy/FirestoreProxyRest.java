/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Classe Proxy responsavel por toda comunicacao HTTP/JSON com o Cloud Firestore.
 * Usa apenas HttpClient (nativo do Java 11+) e a biblioteca Gson (gson.jar).
 */
package br.edu.urna.firebase.proxy;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.model.Voto;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Proxy de comunicacao com o Cloud Firestore usando a API REST oficial do Firebase.
 * Ideal para projetos Java comuns (sem Maven), pois exige apenas o JAR do Gson.
 */
public class FirestoreProxyRest implements UrnaRepository {

    private static final Duration TEMPO_LIMITE = Duration.ofSeconds(10);

    private String projetoId;
    private String chaveApiWeb;
    private String emailUsuario;
    private String senhaUsuario;
    private String tokenAutenticacao;
    private HttpClient clienteHttp;

    /**
     * Cria o Proxy carregando as configuracoes do arquivo .env.
     *
     * @param caminhoEnv Caminho para o arquivo .env (ex: ".env").
     * @throws BancoDadosException Se o arquivo .env nao existir ou estiver incompleto.
     */
    public FirestoreProxyRest(String caminhoEnv) throws BancoDadosException {
        try {
            ConfiguracaoEnv configuracao = ConfiguracaoEnv.carregar(caminhoEnv);
            this.projetoId = configuracao.obterObrigatorio("FIREBASE_PROJECT_ID");
            this.chaveApiWeb = configuracao.obterObrigatorio("FIREBASE_API_KEY");
            this.emailUsuario = configuracao.obterObrigatorio("FIREBASE_EMAIL");
            this.senhaUsuario = configuracao.obterObrigatorio("FIREBASE_PASSWORD");
            this.clienteHttp = HttpClient.newBuilder()
                    .connectTimeout(TEMPO_LIMITE)
                    .build();
        } catch (IOException | IllegalArgumentException erro) {
            throw new BancoDadosException("Falha ao ler configuracao do arquivo .env: " + erro.getMessage(), erro);
        }
    }

    /**
     * Autentica no Firebase Authentication com e-mail e senha para obter o token JWT.
     */
    private void autenticarSeNecessario() throws BancoDadosException {
        if (tokenAutenticacao != null && !tokenAutenticacao.isEmpty()) {
            return;
        }

        String urlLogin = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + chaveApiWeb;

        JsonObject corpo = new JsonObject();
        corpo.addProperty("email", emailUsuario);
        corpo.addProperty("password", senhaUsuario);
        corpo.addProperty("returnSecureToken", true);

        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(urlLogin))
                .timeout(TEMPO_LIMITE)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(corpo.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resposta = enviarRequisicao(requisicao);
        if (resposta.statusCode() != 200) {
            throw new BancoDadosException(
                    "Falha na autenticacao Firebase (HTTP " + resposta.statusCode()
                    + "). Confira API Key, e-mail e senha no arquivo .env."
            );
        }

        JsonObject jsonResposta = JsonParser.parseString(resposta.body()).getAsJsonObject();
        this.tokenAutenticacao = jsonResposta.get("idToken").getAsString();
    }

    /**
     * Busca todos os documentos da colecao "candidatos" no Firestore.
     */
    @Override
    public List<Candidato> listarCandidatos() throws BancoDadosException {
        autenticarSeNecessario();

        String url = montarUrlColecao("candidatos");
        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(TEMPO_LIMITE)
                .header("Authorization", "Bearer " + tokenAutenticacao)
                .GET()
                .build();

        HttpResponse<String> resposta = enviarRequisicao(requisicao);
        validarStatusResposta(resposta, "listar candidatos");

        List<Candidato> candidatos = new ArrayList<Candidato>();
        JsonObject jsonRaiz = JsonParser.parseString(resposta.body()).getAsJsonObject();

        if (!jsonRaiz.has("documents")) {
            return candidatos; // Colecao ainda esta vazia
        }

        JsonArray documentos = jsonRaiz.getAsJsonArray("documents");
        for (int i = 0; i < documentos.size(); i++) {
            JsonObject documento = documentos.get(i).getAsJsonObject();
            String caminhoCompleto = documento.get("name").getAsString();
            String idDocumento = caminhoCompleto.substring(caminhoCompleto.lastIndexOf('/') + 1);
            JsonObject campos = documento.getAsJsonObject("fields");

            String numero = lerCampoTexto(campos, "numero", idDocumento);
            String nome = lerCampoTexto(campos, "nome", "Sem nome");
            String partido = lerCampoTexto(campos, "partido", "Sem partido");
            String cargo = lerCampoTexto(campos, "cargo", "Vereador");
            String foto = lerCampoTexto(campos, "foto", "golfe.png");

            candidatos.add(new Candidato(numero, nome, partido, cargo, foto));
        }

        return candidatos;
    }

    /**
     * Grava um novo documento na colecao "votos" do Firestore.
     */
    @Override
    public void salvarVoto(Voto voto) throws BancoDadosException {
        autenticarSeNecessario();

        // Na API REST do Firestore, cada campo precisa declarar seu tipo (stringValue)
        JsonObject campos = new JsonObject();
        campos.add("cargo", criarCampoTexto(voto.getCargo()));
        campos.add("tipo", criarCampoTexto(voto.getTipo()));
        campos.add("numeroCandidato", criarCampoTexto(voto.getNumeroCandidato()));
        campos.add("dataHora", criarCampoTexto(voto.getDataHora()));

        JsonObject documento = new JsonObject();
        documento.add("fields", campos);

        String url = montarUrlColecao("votos");
        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(TEMPO_LIMITE)
                .header("Authorization", "Bearer " + tokenAutenticacao)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(documento.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> resposta = enviarRequisicao(requisicao);
        validarStatusResposta(resposta, "salvar voto");
    }

    /**
     * Busca todos os votos gravados na colecao "votos" para a apuracao.
     */
    @Override
    public List<Voto> listarVotos() throws BancoDadosException {
        autenticarSeNecessario();

        String url = montarUrlColecao("votos");
        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(TEMPO_LIMITE)
                .header("Authorization", "Bearer " + tokenAutenticacao)
                .GET()
                .build();

        HttpResponse<String> resposta = enviarRequisicao(requisicao);
        validarStatusResposta(resposta, "listar votos");

        List<Voto> votos = new ArrayList<Voto>();
        JsonObject jsonRaiz = JsonParser.parseString(resposta.body()).getAsJsonObject();

        if (!jsonRaiz.has("documents")) {
            return votos;
        }

        JsonArray documentos = jsonRaiz.getAsJsonArray("documents");
        for (int i = 0; i < documentos.size(); i++) {
            JsonObject documento = documentos.get(i).getAsJsonObject();
            JsonObject campos = documento.getAsJsonObject("fields");

            String cargo = lerCampoTexto(campos, "cargo", "Vereador");
            String tipo = lerCampoTexto(campos, "tipo", "NULO");
            String numero = lerCampoTexto(campos, "numeroCandidato", "");
            String dataHora = lerCampoTexto(campos, "dataHora", "");

            votos.add(new Voto(cargo, tipo, numero, dataHora));
        }

        return votos;
    }

    private String montarUrlColecao(String nomeColecao) {
        return "https://firestore.googleapis.com/v1/projects/"
                + projetoId + "/databases/(default)/documents/" + nomeColecao;
    }

    private HttpResponse<String> enviarRequisicao(HttpRequest requisicao) throws BancoDadosException {
        try {
            return clienteHttp.send(requisicao, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (IOException erroRede) {
            throw new BancoDadosException("Erro de rede ao conversar com o Firebase. Verifique sua internet.", erroRede);
        } catch (InterruptedException erroInterrupcao) {
            Thread.currentThread().interrupt();
            throw new BancoDadosException("A comunicacao com o Firebase foi interrompida.", erroInterrupcao);
        }
    }

    private void validarStatusResposta(HttpResponse<String> resposta, String operacao) throws BancoDadosException {
        int codigo = resposta.statusCode();
        if (codigo == 401) {
            this.tokenAutenticacao = null; // Forca renovar login na proxima tentativa
            throw new BancoDadosException("Sessao expirada ao " + operacao + ". Tente novamente.");
        }
        if (codigo == 403) {
            throw new BancoDadosException("Acesso negado (HTTP 403) ao " + operacao + ". Confira as regras (firestore.rules).");
        }
        if (codigo < 200 || codigo >= 300) {
            throw new BancoDadosException("Erro ao " + operacao + " no Firestore (HTTP " + codigo + ").");
        }
    }

    private JsonObject criarCampoTexto(String valor) {
        JsonObject campo = new JsonObject();
        campo.addProperty("stringValue", valor == null ? "" : valor);
        return campo;
    }

    private String lerCampoTexto(JsonObject campos, String nomeCampo, String valorPadrao) {
        if (campos == null || !campos.has(nomeCampo)) {
            return valorPadrao;
        }
        JsonElement elemento = campos.get(nomeCampo);
        if (elemento != null && elemento.isJsonObject()) {
            JsonObject objetoCampo = elemento.getAsJsonObject();
            if (objetoCampo.has("stringValue")) {
                return objetoCampo.get("stringValue").getAsString();
            }
        }
        return valorPadrao;
    }
}
