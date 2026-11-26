/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.proxy;

import br.edu.urna.trabalho.model.Candidato;
import br.edu.urna.trabalho.model.Voto;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacao em memoria do UrnaRepository para testes locais rapidos.
 * Contem candidatos ficticios de dois cargos (Vereador - 5 digitos e Prefeito - 2 digitos)
 * inspirados no simulador oficial do TSE.
 */
public class MemoriaUrnaProxy implements UrnaRepository {

    private List<Candidato> candidatos;
    private List<Voto> votos;

    public MemoriaUrnaProxy() {
        this.candidatos = new ArrayList<Candidato>();
        this.votos = new ArrayList<Voto>();

        // Cargo 1: Vereador (5 digitos - Proporcional)
        this.candidatos.add(new Candidato("91001", "Golfe", "PEsp", "Vereador", "golfe.png"));
        this.candidatos.add(new Candidato("91002", "Beisebol", "PEsp", "Vereador", "beisebol.png"));
        this.candidatos.add(new Candidato("92001", "Rock", "PMus", "Vereador", "rock.png"));
        this.candidatos.add(new Candidato("93001", "Astronauta", "PProf", "Vereador", "astronauta.png"));

        // Cargo 2: Prefeito (2 digitos - Majoritario)
        this.candidatos.add(new Candidato("91", "Maria Esporte", "PEsp", "Prefeito", "esportes.png"));
        this.candidatos.add(new Candidato("92", "Carlos Ritmo", "PMus", "Prefeito", "musica.png"));
    }

    @Override
    public List<Candidato> listarCandidatos() {
        return new ArrayList<Candidato>(candidatos);
    }

    @Override
    public void salvarVoto(Voto voto) {
        votos.add(voto);
    }

    @Override
    public List<Voto> listarVotos() {
        return new ArrayList<Voto>(votos);
    }
}
