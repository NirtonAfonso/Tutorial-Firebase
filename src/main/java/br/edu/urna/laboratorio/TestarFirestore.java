package br.edu.urna.laboratorio;

import br.edu.urna.dados.FirebaseConexao;
import br.edu.urna.dados.FirestoreProxy;
import br.edu.urna.dados.PersistenciaException;
import br.edu.urna.dominio.Voto;
import java.io.IOException;
import java.util.UUID;

public final class TestarFirestore {
    public static void main(String[] args) {
        try (FirebaseConexao conexao = FirebaseConexao.abrir()) {
            var repositorio = new FirestoreProxy(conexao.banco());
            var candidatos = repositorio.listarCandidatos();
            if (candidatos.isEmpty()) {
                System.out.println("Conexão funcionando. Cadastre os candidatos_demo antes de testar a escrita.");
                return;
            }
            candidatos.forEach(c -> System.out.printf("%s | %s | %s%n", c.numero(), c.nome(), c.partido()));

            var voto = new Voto(UUID.randomUUID(), Voto.Tipo.NOMINAL, candidatos.getFirst().numero());
            repositorio.salvar(voto);
            repositorio.salvar(voto);
            System.out.println("Confira um único documento em votos_demo com o ID " + voto.id());
        } catch (IOException | IllegalArgumentException | PersistenciaException erro) {
            System.err.println("Falha no laboratório. " + erro.getMessage());
            if (erro.getCause() != null) {
                System.err.println("Detalhe: " + erro.getCause());
            }
            // Faz o Maven marcar o comando como falho, sem usar System.exit no exemplo.
            throw new IllegalStateException("O teste de leitura e escrita precisa ser executado novamente.", erro);
        }
    }
}
