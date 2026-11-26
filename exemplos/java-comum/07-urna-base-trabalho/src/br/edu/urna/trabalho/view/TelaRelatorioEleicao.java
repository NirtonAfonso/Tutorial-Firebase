/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Nova janela Swing para exibicao da Zeresima e do Relatorio Final por cargo.
 */
package br.edu.urna.trabalho.view;

import br.edu.urna.trabalho.model.LinhaRelatorio;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Janela separada que apresenta o relatorio da eleicao (Zeresima ou Apuracao Final),
 * dividido por abas de cargo, exibindo tabela e resumo no formato do enunciado.
 */
public class TelaRelatorioEleicao extends JFrame {

    public TelaRelatorioEleicao(String tituloJanela, Map<String, List<LinhaRelatorio>> relatorioPorCargo) {
        setTitle(tituloJanela);
        setSize(680, 440);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelRaiz = new JPanel(new BorderLayout(10, 10));
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel rotuloTitulo = new JLabel(tituloJanela, SwingConstants.CENTER);
        rotuloTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        painelRaiz.add(rotuloTitulo, BorderLayout.NORTH);

        JTabbedPane painelAbas = new JTabbedPane();

        for (Map.Entry<String, List<LinhaRelatorio>> entrada : relatorioPorCargo.entrySet()) {
            String nomeCargo = entrada.getKey();
            List<LinhaRelatorio> linhas = entrada.getValue();
            painelAbas.addTab("Cargo: " + nomeCargo, criarAbaCargo(linhas));
        }

        painelRaiz.add(painelAbas, BorderLayout.CENTER);
        setContentPane(painelRaiz);
    }

    /**
     * Monta o painel de cada cargo com uma JTable e o resumo textual no formato da AV3.
     */
    private JPanel criarAbaCargo(List<LinhaRelatorio> linhas) {
        JPanel painel = new JPanel(new BorderLayout(8, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Candidato / Tipo", "Numero", "Votos", "Percentual"};
        DefaultTableModel modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }
        };

        StringBuilder textoPadrao = new StringBuilder();
        for (int i = 0; i < linhas.size(); i++) {
            LinhaRelatorio linha = linhas.get(i);
            modeloTabela.addRow(new Object[]{
                    linha.getDescricao(),
                    linha.getNumero().isEmpty() ? "-" : linha.getNumero(),
                    linha.getQuantidadeVotos(),
                    Math.round(linha.getPercentual()) + "%"
            });
            textoPadrao.append(linha.formatarLinhaPadrao()).append("\n");
        }

        JTable tabela = new JTable(modeloTabela);
        tabela.setRowHeight(24);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        JTextArea areaResumo = new JTextArea(textoPadrao.toString());
        areaResumo.setEditable(false);
        areaResumo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResumo.setBorder(BorderFactory.createTitledBorder("Formato Oficial do Relatorio"));
        areaResumo.setRows(6);

        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(new JScrollPane(areaResumo), BorderLayout.SOUTH);
        return painel;
    }
}
