/*
 * Exemplo 01 - Swing Basico: Combinacao de Layouts (BorderLayout + GridLayout)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Objetivo: Mostrar como combinar paineis (JPanel) com gerenciadores de layout
 * diferentes para estruturar uma tela semelhante a uma urna eletronica.
 */
package br.edu.urna.swingbasico;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Demonstra a combinacao de BorderLayout (janela inteira) com GridLayout (teclado).
 * Essa tecnica e a base para construir a tela da Urna Eletronica sem usar layout null.
 */
public class ExemploLayouts extends JFrame {

    private JLabel rotuloDigitado;
    private String textoDigitado;

    /**
     * Monta a estrutura da janela dividindo-a em cabecalho, visor (centro) e teclado (leste).
     */
    public ExemploLayouts() {
        this.textoDigitado = "";

        setTitle("Exemplo 01 - Combinando BorderLayout e GridLayout");
        setSize(560, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal da janela: BorderLayout com espacamento de 12 pixels
        JPanel painelRaiz = new JPanel(new BorderLayout(12, 12));
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // NORTE: titulo explicativo
        JLabel rotuloTitulo = new JLabel("Demonstracao de Layouts Combinados", SwingConstants.CENTER);
        rotuloTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        painelRaiz.add(rotuloTitulo, BorderLayout.NORTH);

        // CENTRO: visor branco simulando a tela da urna
        JPanel painelVisor = new JPanel(new BorderLayout());
        painelVisor.setBackground(Color.WHITE);
        painelVisor.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        rotuloDigitado = new JLabel("Digite no teclado...", SwingConstants.CENTER);
        rotuloDigitado.setFont(new Font("Monospaced", Font.BOLD, 24));
        painelVisor.add(rotuloDigitado, BorderLayout.CENTER);
        painelRaiz.add(painelVisor, BorderLayout.CENTER);

        // LESTE: teclado numerico em grade 4 linhas x 3 colunas (GridLayout)
        JPanel painelTeclado = new JPanel(new GridLayout(4, 3, 6, 6));
        painelTeclado.setPreferredSize(new Dimension(210, 220));

        for (int i = 1; i <= 9; i++) {
            painelTeclado.add(criarBotaoNumerico(String.valueOf(i)));
        }
        painelTeclado.add(new JLabel("")); // Espaco vazio a esquerda do 0
        painelTeclado.add(criarBotaoNumerico("0"));

        JButton botaoApagar = new JButton("C");
        botaoApagar.setBackground(new Color(255, 180, 100));
        botaoApagar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                textoDigitado = "";
                rotuloDigitado.setText("Digite no teclado...");
            }
        });
        painelTeclado.add(botaoApagar);

        painelRaiz.add(painelTeclado, BorderLayout.EAST);

        add(painelRaiz);
    }

    /**
     * Cria um botao numerico configurado com seu ActionListener.
     *
     * @param digito Texto do digito de 0 a 9.
     * @return Botao pronto para adicionar ao painel de teclado.
     */
    private JButton criarBotaoNumerico(final String digito) {
        JButton botao = new JButton(digito);
        botao.setFont(new Font("SansSerif", Font.BOLD, 16));
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                if (textoDigitado.length() < 5) {
                    textoDigitado = textoDigitado + digito;
                    rotuloDigitado.setText(textoDigitado);
                }
            }
        });
        return botao;
    }

    /**
     * Inicia a aplicacao de demonstracao de layouts.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ExemploLayouts janela = new ExemploLayouts();
                janela.setVisible(true);
            }
        });
    }
}
