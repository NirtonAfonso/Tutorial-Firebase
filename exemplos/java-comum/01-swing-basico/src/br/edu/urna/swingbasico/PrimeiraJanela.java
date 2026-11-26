/*
 * Exemplo 01 - Swing Basico: Primeira Janela e Evento de Clique
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Objetivo: Entender os componentes essenciais do Java Swing (JFrame, JPanel,
 * JLabel, JButton) e como o padrao de eventos (ActionListener) funciona.
 */
package br.edu.urna.swingbasico;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Demonstra a criacao da primeira janela em Java Swing com um visor e botoes.
 * Ao clicar em um botao, um ouvinte (ActionListener) atualiza o texto do visor.
 */
public class PrimeiraJanela extends JFrame {

    private JLabel rotuloVisor;
    private JButton botaoSete;
    private JButton botaoLimpar;

    /**
     * Construtor responsavel por configurar a janela e adicionar os componentes.
     */
    public PrimeiraJanela() {
        // 1. Configuracoes basicas da janela (JFrame)
        setTitle("Exemplo 01 - Primeira Janela Swing");
        setSize(420, 180);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela na tela

        // 2. Painel principal usando FlowLayout (coloca os componentes lado a lado)
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 35));
        painelPrincipal.setBackground(new Color(245, 247, 250));

        // 3. Criacao dos componentes (sem notacao hungara: nomes claros e completos)
        rotuloVisor = new JLabel("Visor: _");
        rotuloVisor.setFont(new Font("SansSerif", Font.BOLD, 24));

        botaoSete = new JButton("Digitar 7");
        botaoSete.setFont(new Font("SansSerif", Font.PLAIN, 16));

        botaoLimpar = new JButton("Limpar");
        botaoLimpar.setFont(new Font("SansSerif", Font.PLAIN, 16));

        // 4. Registro dos ouvintes de evento (ActionListener com classe anonima)
        // O metodo actionPerformed so e executado no momento em que o usuario clica.
        botaoSete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                rotuloVisor.setText("Visor: 7");
            }
        });

        botaoLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                rotuloVisor.setText("Visor: _");
            }
        });

        // 5. Adicao dos componentes ao painel e do painel a janela
        painelPrincipal.add(rotuloVisor);
        painelPrincipal.add(botaoSete);
        painelPrincipal.add(botaoLimpar);

        add(painelPrincipal);
    }

    /**
     * Ponto de entrada do programa.
     * SwingUtilities.invokeLater garante que a tela seja criada na thread do Swing (EDT).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PrimeiraJanela janela = new PrimeiraJanela();
                janela.setVisible(true);
            }
        });
    }
}
