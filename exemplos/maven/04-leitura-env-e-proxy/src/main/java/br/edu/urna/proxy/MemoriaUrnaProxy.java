/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Implementacao em memoria do contrato UrnaRepository.
 * Permite testar toda a aplicacao localmente antes de ligar ao Firebase.
 */
package br.edu.urna.proxy;

import java.util.ArrayList;
import java.util.List;

/**
 * Proxy em memoria que simula um banco de dados enquanto a equipe desenvolve as telas.
 * Depois, basta trocar esta classe por FirestoreProxy sem alterar a interface grafica.
 */
public class MemoriaUrnaProxy implements UrnaRepository {

    private List<String> candidatos;
    private List<String> votos;

    /**
     * Inicializa o proxy com candidatos ficticios de treino.
     */
    public MemoriaUrnaProxy() {
        this.candidatos = new ArrayList<String>();
        this.candidatos.add("91001 - Golfe (PEsp)");
        this.candidatos.add("91002 - Beisebol (PEsp)");
        this.candidatos.add("92001 - Rock (PMus)");

        this.votos = new ArrayList<String>();
    }

    @Override
    public List<String> listarCandidatos() {
        return new ArrayList<String>(candidatos);
    }

    @Override
    public void salvarVoto(String numeroDigitado) {
        System.out.println("[MemoriaUrnaProxy] Salvando voto em memoria: " + numeroDigitado);
        votos.add(numeroDigitado);
    }

    @Override
    public List<String> listarVotos() {
        return new ArrayList<String>(votos);
    }
}
