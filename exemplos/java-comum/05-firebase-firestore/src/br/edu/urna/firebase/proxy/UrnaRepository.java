/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.firebase.proxy;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.model.Voto;
import java.util.List;

/**
 * Interface de acesso a dados da urna.
 * Permite trocar a implementacao (REST, Admin SDK ou Memoria) sem alterar a tela Swing.
 */
public interface UrnaRepository {

    List<Candidato> listarCandidatos() throws BancoDadosException;

    void salvarVoto(Voto voto) throws BancoDadosException;

    List<Voto> listarVotos() throws BancoDadosException;
}
