/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Tela principal Swing conectada ao EleicaoController (Padrao MVC).
 * Contempla abertura (99999 + Zeresima), votacao nos 2 cargos, foto automatica
 * e encerramento (99999 + janela de apuracao).
 */
package br.edu.urna.trabalho.view;

import br.edu.urna.trabalho.controller.EleicaoController;
import br.edu.urna.trabalho.exception.BancoDadosException;
import br.edu.urna.trabalho.exception.EleicaoException;
import br.edu.urna.trabalho.model.Candidato;
import br.edu.urna.trabalho.model.Cargo;
import br.edu.urna.trabalho.model.LinhaRelatorio;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;

/**
 * Interface principal da Urna Eletronica Simplificada (Camada View do padrao MVC).
 */
public class TelaUrnaEletronica extends JFrame {

    private EleicaoController controlador;
    private String numeroDigitado;
    private boolean votoEmBranco;

    private JLabel rotuloStatusEleicao;
    private JLabel rotuloCargo;
    private JLabel rotuloNumero;
    private JLabel rotuloNome;
    private JLabel rotuloPartido;
    private JLabel rotuloFoto;
    private JLabel rotuloMensagem;
    private JButton botaoAbrirEleicao;
    private JButton botaoEncerrarEleicao;
    private JButton botaoConfirmar;

    public TelaUrnaEletronica(EleicaoController controlador) {
        this.controlador = controlador;
        this.numeroDigitado = "";
        this.votoEmBranco = false;

        configurarJanela();
        montarInterface();
        atualizarVisor("Urna bloqueada. Clique em 'Iniciar Votacao' e informe a senha 99999.");
    }

    private void configurarJanela() {
        setTitle("Urna Eletronica Simplificada - Projeto Base POO");
        setSize(860, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarInterface() {
        JPanel painelRaiz = new JPanel(new BorderLayout(14, 12));
        painelRaiz.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        painelRaiz.setBackground(new Color(235, 238, 242));

        // --- BARRA SUPERIOR DO MESARIO (ABERTURA / ENCERRAMENTO COM SENHA 99999) ---
        JPanel painelMesario = new JPanel(new BorderLayout());
        painelMesario.setBackground(new Color(30, 58, 95));
        painelMesario.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        rotuloStatusEleicao = new JLabel("STATUS: AGUARDANDO ABERTURA");
        rotuloStatusEleicao.setForeground(Color.WHITE);
        rotuloStatusEleicao.setFont(new Font("SansSerif", Font.BOLD, 13));

        JPanel painelBotoesMesario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoesMesario.setOpaque(false);

        botaoAbrirEleicao = new JButton("Iniciar Votacao (Senha)");
        botaoAbrirEleicao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                solicitarAberturaEleicao();
            }
        });

        botaoEncerrarEleicao = new JButton("Encerrar Votacao (Senha)");
        botaoEncerrarEleicao.setEnabled(false);
        botaoEncerrarEleicao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                solicitarEncerramentoEleicao();
            }
        });

        painelBotoesMesario.add(botaoAbrirEleicao);
        painelBotoesMesario.add(botaoEncerrarEleicao);

        painelMesario.add(rotuloStatusEleicao, BorderLayout.WEST);
        painelMesario.add(painelBotoesMesario, BorderLayout.EAST);

        // --- VISOR DA URNA ---
        JPanel painelVisor = new JPanel(new BorderLayout(12, 8));
        painelVisor.setBackground(Color.WHITE);
        painelVisor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(175, 185, 196), 2),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JPanel painelDados = new JPanel(new GridLayout(4, 1, 0, 8));
        painelDados.setOpaque(false);

        rotuloCargo = new JLabel("URNA AGUARDANDO ABERTURA");
        rotuloCargo.setFont(new Font("SansSerif", Font.BOLD, 16));

        rotuloNumero = new JLabel("-----");
        rotuloNumero.setFont(new Font("Monospaced", Font.BOLD, 34));
        rotuloNumero.setForeground(new Color(25, 42, 86));

        rotuloNome = new JLabel(" ");
        rotuloNome.setFont(new Font("SansSerif", Font.BOLD, 18));

        rotuloPartido = new JLabel(" ");
        rotuloPartido.setFont(new Font("SansSerif", Font.PLAIN, 15));
        rotuloPartido.setForeground(new Color(80, 90, 100));

        painelDados.add(rotuloCargo);
        painelDados.add(rotuloNumero);
        painelDados.add(rotuloNome);
        painelDados.add(rotuloPartido);

        rotuloFoto = new JLabel("", SwingConstants.CENTER);
        rotuloFoto.setPreferredSize(new Dimension(150, 185));
        rotuloFoto.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220), 1));

        painelVisor.add(painelDados, BorderLayout.CENTER);
        painelVisor.add(rotuloFoto, BorderLayout.EAST);

        // --- TECLADO ---
        JPanel painelTeclado = new JPanel(new BorderLayout(0, 10));
        painelTeclado.setPreferredSize(new Dimension(315, 300));
        painelTeclado.setBackground(new Color(45, 50, 58));
        painelTeclado.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel painelNumeros = new JPanel(new GridLayout(4, 3, 8, 8));
        painelNumeros.setOpaque(false);
        for (int i = 1; i <= 9; i++) {
            painelNumeros.add(criarBotaoNumero(i));
        }
        painelNumeros.add(new JLabel(""));
        painelNumeros.add(criarBotaoNumero(0));
        painelNumeros.add(new JLabel(""));

        JPanel painelAcoes = new JPanel(new GridLayout(1, 3, 6, 6));
        painelAcoes.setOpaque(false);

        JButton botaoBranco = new JButton("BRANCO");
        botaoBranco.setBackground(Color.WHITE);
        botaoBranco.setFont(new Font("SansSerif", Font.BOLD, 11));
        botaoBranco.setMargin(new Insets(6, 4, 6, 4));
        botaoBranco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                acionarVotoBranco();
            }
        });

        JButton botaoCorrigir = new JButton("CORRIGE");
        botaoCorrigir.setBackground(new Color(243, 156, 18));
        botaoCorrigir.setFont(new Font("SansSerif", Font.BOLD, 11));
        botaoCorrigir.setMargin(new Insets(6, 4, 6, 4));
        botaoCorrigir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                acionarCorrige();
            }
        });

        botaoConfirmar = new JButton("CONFIRMA");
        botaoConfirmar.setBackground(new Color(39, 174, 96));
        botaoConfirmar.setFont(new Font("SansSerif", Font.BOLD, 11));
        botaoConfirmar.setMargin(new Insets(6, 4, 6, 4));
        botaoConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                acionarConfirma();
            }
        });

        painelAcoes.add(botaoBranco);
        painelAcoes.add(botaoCorrigir);
        painelAcoes.add(botaoConfirmar);

        painelTeclado.add(painelNumeros, BorderLayout.CENTER);
        painelTeclado.add(painelAcoes, BorderLayout.SOUTH);

        rotuloMensagem = new JLabel(" ");
        rotuloMensagem.setFont(new Font("SansSerif", Font.BOLD, 13));

        painelRaiz.add(painelMesario, BorderLayout.NORTH);
        painelRaiz.add(painelVisor, BorderLayout.CENTER);
        painelRaiz.add(painelTeclado, BorderLayout.EAST);
        painelRaiz.add(rotuloMensagem, BorderLayout.SOUTH);

        setContentPane(painelRaiz);
    }

    private JButton criarBotaoNumero(final int valor) {
        JButton botao = new JButton(String.valueOf(valor));
        botao.setFont(new Font("SansSerif", Font.BOLD, 18));
        botao.setBackground(new Color(30, 34, 40));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                digitarNumero(valor);
            }
        });
        return botao;
    }

    /**
     * Solicita a senha administrativa com JPasswordField.
     */
    private String pedirSenhaMesario(String titulo) {
        JPasswordField campoSenha = new JPasswordField();
        int opcao = JOptionPane.showConfirmDialog(
                this,
                campoSenha,
                titulo + " (Senha: 99999)",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );
        if (opcao == JOptionPane.OK_OPTION) {
            return new String(campoSenha.getPassword());
        }
        return null;
    }

    private void solicitarAberturaEleicao() {
        String senha = pedirSenhaMesario("Abrir Eleicao");
        if (senha == null) {
            return;
        }
        try {
            controlador.abrirEleicao(senha);
            rotuloStatusEleicao.setText("STATUS: VOTACAO ABERTA");
            botaoAbrirEleicao.setEnabled(false);
            botaoEncerrarEleicao.setEnabled(true);

            // Exibe a Zeresima obrigatoria antes do primeiro voto
            Map<String, List<LinhaRelatorio>> zeresima = controlador.gerarRelatorioPorCargo();
            TelaRelatorioEleicao janelaZeresima = new TelaRelatorioEleicao("Zeresima - Relatorio Inicial (Tudo Zerado)", zeresima);
            janelaZeresima.setVisible(true);

            atualizarVisor("Votacao liberada! Vote para " + controlador.getCargoAtual().getNome() + ".");
        } catch (EleicaoException | BancoDadosException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(), "Erro ao Abrir", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void solicitarEncerramentoEleicao() {
        String senha = pedirSenhaMesario("Encerrar Eleicao");
        if (senha == null) {
            return;
        }
        try {
            controlador.encerrarEleicao(senha);
            rotuloStatusEleicao.setText("STATUS: VOTACAO ENCERRADA");
            botaoEncerrarEleicao.setEnabled(false);
            numeroDigitado = "";
            votoEmBranco = false;

            atualizarVisor("Eleicao encerrada! Consulte o relatorio na nova janela.");

            Map<String, List<LinhaRelatorio>> apuracao = controlador.gerarRelatorioPorCargo();
            TelaRelatorioEleicao janelaApuracao = new TelaRelatorioEleicao("Relatorio Final da Eleicao", apuracao);
            janelaApuracao.setVisible(true);
        } catch (EleicaoException | BancoDadosException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(), "Erro ao Encerrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void digitarNumero(int digito) {
        if (!controlador.isEmVotacao() || votoEmBranco) {
            return;
        }
        int limite = controlador.getCargoAtual().getQuantidadeDigitos();
        if (numeroDigitado.length() < limite) {
            numeroDigitado = numeroDigitado + digito;
            atualizarVisor("");
        }
    }

    private void acionarVotoBranco() {
        if (!controlador.isEmVotacao()) {
            return;
        }
        if (numeroDigitado.isEmpty()) {
            votoEmBranco = true;
            atualizarVisor("Aperte CONFIRMA para confirmar o voto em BRANCO ou CORRIGE.");
        }
    }

    private void acionarCorrige() {
        if (!controlador.isEmVotacao()) {
            return;
        }
        numeroDigitado = "";
        votoEmBranco = false;
        atualizarVisor("Numero apagado. Digite novamente.");
    }

    private void acionarConfirma() {
        if (!controlador.isEmVotacao()) {
            return;
        }
        try {
            boolean concluiuTodosCargos = controlador.confirmarVotoAtual(numeroDigitado, votoEmBranco);
            numeroDigitado = "";
            votoEmBranco = false;

            if (concluiuTodosCargos) {
                JOptionPane.showMessageDialog(this, "FIM! Seu voto foi registrado com sucesso.", "Votacao Concluida", JOptionPane.INFORMATION_MESSAGE);
                atualizarVisor("Proximo eleitor: vote para " + controlador.getCargoAtual().getNome() + ".");
            } else {
                atualizarVisor("Primeiro cargo confirmado! Agora vote para " + controlador.getCargoAtual().getNome() + ".");
            }
        } catch (EleicaoException | BancoDadosException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void atualizarVisor(String aviso) {
        rotuloNome.setText(" ");
        rotuloPartido.setText(" ");
        rotuloFoto.setIcon(null);
        rotuloFoto.setText("");

        if (!controlador.isEmVotacao()) {
            rotuloCargo.setText(controlador.getEstadoAtual() == EleicaoController.ESTADO_ENCERRADA
                    ? "ELEICAO ENCERRADA"
                    : "AGUARDANDO ABERTURA");
            rotuloNumero.setText("-----");
            botaoConfirmar.setEnabled(false);
            rotuloMensagem.setText(aviso);
            return;
        }

        if (controlador.isEmVotacao()) {
            rotuloStatusEleicao.setText("STATUS: VOTACAO ABERTA");
            botaoAbrirEleicao.setEnabled(false);
            botaoEncerrarEleicao.setEnabled(true);
        }

        Cargo cargoAtual = controlador.getCargoAtual();
        int limiteDigitos = cargoAtual.getQuantidadeDigitos();
        rotuloCargo.setText("SEU VOTO PARA: " + cargoAtual.getNome().toUpperCase());

        StringBuilder mascara = new StringBuilder(numeroDigitado);
        while (mascara.length() < limiteDigitos) {
            mascara.append("_");
        }
        rotuloNumero.setText(votoEmBranco ? "BRANCO" : mascara.toString());

        if (votoEmBranco) {
            rotuloNome.setText("VOTO EM BRANCO");
            botaoConfirmar.setEnabled(true);
        } else if (numeroDigitado.length() == limiteDigitos) {
            Candidato candidato = controlador.buscarCandidatoNoCargoAtual(numeroDigitado);
            if (candidato != null) {
                rotuloNome.setText("Nome: " + candidato.getNome());
                rotuloPartido.setText("Partido: " + candidato.getPartido());
                carregarFoto(candidato);
                aviso = "Confira os dados e aperte CONFIRMA ou CORRIGE.";
            } else {
                rotuloNome.setText("NUMERO ERRADO");
                rotuloPartido.setText("VOTO NULO");
                rotuloFoto.setText("Sem candidato");
                aviso = "Numero inexistente. Confirmar registrara voto NULO.";
            }
            botaoConfirmar.setEnabled(true);
        } else {
            botaoConfirmar.setEnabled(false);
            if (aviso.isEmpty()) {
                aviso = "Digite os " + limiteDigitos + " digitos do cargo " + cargoAtual.getNome() + ".";
            }
        }

        rotuloMensagem.setText(aviso);
    }

    private void carregarFoto(Candidato candidato) {
        URL urlImagem = getClass().getResource(candidato.getCaminhoFoto());
        if (urlImagem == null) {
            rotuloFoto.setText("Sem foto");
            return;
        }
        ImageIcon icone = new ImageIcon(urlImagem);
        Image redimensionada = icone.getImage().getScaledInstance(140, 175, Image.SCALE_SMOOTH);
        rotuloFoto.setIcon(new ImageIcon(redimensionada));
    }
}
