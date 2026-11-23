package br.edu.urna.rest;

import br.edu.urna.dados.PersistenciaException;
import br.edu.urna.dominio.Voto;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public final class TestarFirestoreRest {
    public static void main(String[] args) {
        try (var repositorio = FirestoreProxyRest.abrir(Path.of(".env"))) {
            var candidatos = repositorio.listarCandidatos();
            if (candidatos.isEmpty()) {
                System.out.println("Conectou. Cadastrem os dois candidatos antes de testar a gravação.");
                return;
            }
            for (var candidato : candidatos) {
                System.out.printf("%s | %s | %s%n", candidato.numero(), candidato.nome(), candidato.partido());
            }

            var voto = new Voto(UUID.randomUUID(), Voto.Tipo.NOMINAL, candidatos.getFirst().numero());
            repositorio.salvar(voto);
            repositorio.salvar(voto);
            System.out.println("Duas gravações com o mesmo ID. Confiram o documento em votos_demo: " + voto.id());
        } catch (IOException | IllegalArgumentException | PersistenciaException erro) {
            throw new IllegalStateException("O teste não foi concluído. " + erro.getMessage(), erro);
        }
    }
}
