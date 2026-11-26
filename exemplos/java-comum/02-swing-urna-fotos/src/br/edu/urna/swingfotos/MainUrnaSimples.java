/*
 * Exemplo 02 - Urna Swing com Exibicao Automatica de Foto
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Classe principal que cadastra candidatos ficticios do simulador do TSE
 * e abre a janela da urna.
 */
package br.edu.urna.swingfotos;

import br.edu.urna.swingfotos.model.Candidato;
import br.edu.urna.swingfotos.view.TelaUrnaSimples;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Inicia a demonstracao da Urna Swing com carregamento de fotos.
 */
public class MainUrnaSimples {

    public static void main(String[] args) {
        // Candidatos ficticios inspirados no simulador oficial do TSE
        List<Candidato> candidatos = new ArrayList<Candidato>();
        candidatos.add(new Candidato("91001", "Golfe", "PEsp", "golfe.png"));
        candidatos.add(new Candidato("91002", "Beisebol", "PEsp", "beisebol.png"));
        candidatos.add(new Candidato("92001", "Rock", "PMus", "rock.png"));
        candidatos.add(new Candidato("93001", "Astronauta", "PProf", "astronauta.png"));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TelaUrnaSimples tela = new TelaUrnaSimples(candidatos);
                tela.setVisible(true);
            }
        });
    }
}
