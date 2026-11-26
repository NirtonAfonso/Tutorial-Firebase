/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Ponto de entrada do projeto base.
 */
package br.edu.urna.trabalho;

import br.edu.urna.trabalho.controller.EleicaoController;
import br.edu.urna.trabalho.exception.BancoDadosException;
import br.edu.urna.trabalho.model.Cargo;
import br.edu.urna.trabalho.model.CargoMajoritario;
import br.edu.urna.trabalho.model.CargoProporcional;
import br.edu.urna.trabalho.proxy.MemoriaUrnaProxy;
import br.edu.urna.trabalho.proxy.UrnaRepository;
import br.edu.urna.trabalho.view.TelaUrnaEletronica;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Inicializa os dois cargos (Proporcional e Majoritario), o repositorio e a tela MVC.
 */
public class MainUrnaEletronica {

    public static void main(String[] args) {
        // 1. Escolha os dois cargos da eleicao (Vereador + Prefeito ou Governador + Presidente)
        List<Cargo> cargos = new ArrayList<Cargo>();
        cargos.add(new CargoProporcional("Vereador", 5));
        cargos.add(new CargoMajoritario("Prefeito", 2));

        // 2. Escolha o repositorio (Polimorfismo!):
        //    Aqui iniciamos em memoria para rodar imediatamente.
        //    TODO: Para a entrega final, substitua pela sua classe FirestoreProxy / FirestoreProxyRest (Exemplo 05)!
        UrnaRepository repositorio = new MemoriaUrnaProxy();

        try {
            final EleicaoController controlador = new EleicaoController(repositorio, cargos);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    TelaUrnaEletronica tela = new TelaUrnaEletronica(controlador);
                    tela.setVisible(true);
                }
            });
        } catch (BancoDadosException erro) {
            JOptionPane.showMessageDialog(
                    null,
                    "Falha ao carregar os dados iniciais da urna:\n" + erro.getMessage(),
                    "Erro de Inicializacao",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
