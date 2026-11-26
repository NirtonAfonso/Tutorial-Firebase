/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.proxy;

import br.edu.urna.trabalho.exception.BancoDadosException;
import br.edu.urna.trabalho.model.Candidato;
import br.edu.urna.trabalho.model.Voto;
import java.util.List;

/**
 * Interface que desacopla a logica da eleicao do banco de dados real.
 * Para ligar ao Firebase, basta criar/adaptar uma implementacao desta interface
 * (veja o Exemplo 05) e passa-la para o EleicaoController no MainUrnaEletronica!
 */
public interface UrnaRepository {

    List<Candidato> listarCandidatos() throws BancoDadosException;

    void salvarVoto(Voto voto) throws BancoDadosException;

    List<Voto> listarVotos() throws BancoDadosException;
}
