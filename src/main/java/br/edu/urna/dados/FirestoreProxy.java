package br.edu.urna.dados;

import br.edu.urna.dominio.Candidato;
import br.edu.urna.dominio.Voto;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class FirestoreProxy implements RepositorioUrna {
    private final Firestore banco;

    public FirestoreProxy(Firestore banco) {
        this.banco = banco;
    }

    @Override
    public List<Candidato> listarCandidatos() throws PersistenciaException {
        var resultado = aguardar(banco.collection("candidatos_demo").get());
        try {
            return resultado.getDocuments().stream()
                    .map(doc -> new Candidato(doc.getId(), doc.getString("nome"),
                            doc.getString("partido"), doc.getString("foto")))
                    .toList();
        } catch (IllegalArgumentException | ClassCastException erro) {
            throw new PersistenciaException("Confira os campos dos documentos em candidatos_demo.", erro);
        }
    }

    @Override
    public void salvar(Voto voto) throws PersistenciaException {
        var dados = Map.of("tipo", voto.tipo().name(), "numero", voto.numero());
        // O mesmo ID e o mesmo conteúdo mantêm um único documento numa tentativa repetida.
        aguardar(banco.collection("votos_demo").document(voto.id().toString()).set(dados));
    }

    private <T> T aguardar(ApiFuture<T> operacao) throws PersistenciaException {
        try {
            return operacao.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException erro) {
            Thread.currentThread().interrupt();
            throw new PersistenciaException("A operação com o Firestore foi interrompida.", erro);
        } catch (TimeoutException erro) {
            throw new PersistenciaException("O Firestore demorou para responder. Confira a conexão.", erro);
        } catch (ExecutionException erro) {
            throw new PersistenciaException("O Firestore devolveu um erro. Confira o projeto e a permissão.", erro.getCause());
        }
    }
}
