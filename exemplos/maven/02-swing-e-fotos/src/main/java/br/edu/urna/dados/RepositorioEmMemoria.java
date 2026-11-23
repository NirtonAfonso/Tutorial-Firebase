package br.edu.urna.dados;

import br.edu.urna.dominio.Candidato;
import br.edu.urna.dominio.Voto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class RepositorioEmMemoria implements RepositorioUrna {
    private final Map<UUID, Voto> votos = new ConcurrentHashMap<>();

    @Override
    public List<Candidato> listarCandidatos() {
        return List.of(
                new Candidato("07", "Júlia Horizonte", "LAB", "julia.png"),
                new Candidato("12", "Rafael das Nuvens", "TRILHA", "rafael.png")
        );
    }

    @Override
    public void salvar(Voto voto) {
        votos.putIfAbsent(voto.id(), voto);
    }

    public int quantidadeDeVotos() {
        return votos.size();
    }
}
