package br.edu.urna.dados;

import br.edu.urna.dominio.Candidato;
import br.edu.urna.dominio.Voto;
import java.util.List;

public interface RepositorioUrna {
    List<Candidato> listarCandidatos() throws PersistenciaException;

    void salvar(Voto voto) throws PersistenciaException;
}
