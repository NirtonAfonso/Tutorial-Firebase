/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Maven / Admin SDK)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Classe Proxy dedicada para isolar todas as operacoes de banco de dados.
 */
package br.edu.urna.firebase.proxy;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.model.Voto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Proxy que encapsula o acesso ao Cloud Firestore usando o Firebase Admin SDK.
 * As telas Swing interagem apenas com a interface UrnaRepository, mantendo baixo acoplamento.
 */
public class FirestoreProxy implements UrnaRepository {

    private static final String COLECAO_CANDIDATOS = "candidatos";
    private static final String COLECAO_VOTOS = "votos";
    private static final long TEMPO_LIMITE_SEGUNDOS = 10;

    private Firestore banco;

    /**
     * Cria o Proxy recebendo a instancia conectada do Cloud Firestore.
     *
     * @param banco Instancia obtida via FirebaseConexao.obterFirestore().
     */
    public FirestoreProxy(Firestore banco) {
        this.banco = banco;
    }

    /**
     * Busca todos os documentos da colecao "candidatos" e converte para objetos Candidato.
     */
    @Override
    public List<Candidato> listarCandidatos() throws BancoDadosException {
        try {
            ApiFuture<QuerySnapshot> consulta = banco.collection(COLECAO_CANDIDATOS).get();
            QuerySnapshot resultado = consulta.get(TEMPO_LIMITE_SEGUNDOS, TimeUnit.SECONDS);

            List<Candidato> candidatos = new ArrayList<Candidato>();
            List<QueryDocumentSnapshot> documentos = resultado.getDocuments();

            for (int i = 0; i < documentos.size(); i++) {
                QueryDocumentSnapshot doc = documentos.get(i);
                String numero = obterTextoOuPadrao(doc.getString("numero"), doc.getId());
                String nome = obterTextoOuPadrao(doc.getString("nome"), "Sem nome");
                String partido = obterTextoOuPadrao(doc.getString("partido"), "Sem partido");
                String cargo = obterTextoOuPadrao(doc.getString("cargo"), "Vereador");
                String foto = obterTextoOuPadrao(doc.getString("foto"), "golfe.png");

                candidatos.add(new Candidato(numero, nome, partido, cargo, foto));
            }
            return candidatos;
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new BancoDadosException("Leitura de candidatos interrompida.", erro);
        } catch (TimeoutException erro) {
            throw new BancoDadosException("O Firestore demorou para responder. Verifique sua internet.", erro);
        } catch (ExecutionException erro) {
            throw new BancoDadosException("Erro ao consultar candidatos no Firestore.", erro.getCause());
        }
    }

    /**
     * Converte o objeto Voto em um Map e salva um novo documento na colecao "votos".
     */
    @Override
    public void salvarVoto(Voto voto) throws BancoDadosException {
        try {
            Map<String, Object> dados = new HashMap<String, Object>();
            dados.put("cargo", voto.getCargo());
            dados.put("tipo", voto.getTipo());
            dados.put("numeroCandidato", voto.getNumeroCandidato());
            dados.put("dataHora", voto.getDataHora());

            ApiFuture<DocumentReference> operacao = banco.collection(COLECAO_VOTOS).add(dados);
            operacao.get(TEMPO_LIMITE_SEGUNDOS, TimeUnit.SECONDS);
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new BancoDadosException("Gravacao de voto interrompida.", erro);
        } catch (TimeoutException erro) {
            throw new BancoDadosException("Tempo limite esgotado ao salvar voto no Firestore.", erro);
        } catch (ExecutionException erro) {
            throw new BancoDadosException("Erro ao gravar voto no Firestore.", erro.getCause());
        }
    }

    /**
     * Le todos os votos da colecao "votos" para realizar a apuracao final.
     */
    @Override
    public List<Voto> listarVotos() throws BancoDadosException {
        try {
            ApiFuture<QuerySnapshot> consulta = banco.collection(COLECAO_VOTOS).get();
            QuerySnapshot resultado = consulta.get(TEMPO_LIMITE_SEGUNDOS, TimeUnit.SECONDS);

            List<Voto> votos = new ArrayList<Voto>();
            List<QueryDocumentSnapshot> documentos = resultado.getDocuments();

            for (int i = 0; i < documentos.size(); i++) {
                QueryDocumentSnapshot doc = documentos.get(i);
                String cargo = obterTextoOuPadrao(doc.getString("cargo"), "Vereador");
                String tipo = obterTextoOuPadrao(doc.getString("tipo"), "NULO");
                String numero = obterTextoOuPadrao(doc.getString("numeroCandidato"), "");
                String dataHora = obterTextoOuPadrao(doc.getString("dataHora"), "");

                votos.add(new Voto(cargo, tipo, numero, dataHora));
            }
            return votos;
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new BancoDadosException("Leitura de votos interrompida.", erro);
        } catch (TimeoutException erro) {
            throw new BancoDadosException("Tempo limite esgotado ao listar votos no Firestore.", erro);
        } catch (ExecutionException erro) {
            throw new BancoDadosException("Erro ao listar votos no Firestore.", erro.getCause());
        }
    }

    private String obterTextoOuPadrao(String valor, String padrao) {
        if (valor == null || valor.trim().isEmpty()) {
            return padrao;
        }
        return valor;
    }
}
