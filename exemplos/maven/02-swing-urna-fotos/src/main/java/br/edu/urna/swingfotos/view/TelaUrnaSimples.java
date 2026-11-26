/*
 * Exemplo 02 - Urna Swing com Exibicao Automatica de Foto
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Classe de apresentacao (View) que demonstra:
 * - Teclado numerico + BRANCO + CORRIGE + CONFIRMA
 * - Como exibir a foto automaticamente ao completar todos os digitos
 * - Tratamento de voto nominal, voto em branco e voto nulo (sem banco ainda)
 */
package br.edu.urna.swingfotos.view;

import br.edu.urna.swingfotos.model.Candidato;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Janela simplificada da Urna Eletronica em Swing para demonstrar a logica
 * de digitacao e o carregamento automatico da foto do candidato.
 */
public class TelaUrnaSimples extends JFrame {

    private static final int QUANTIDADE_DIGITOS = 5;

    private List<Candidato> listaCandidatos;
    private String numeroDigitado;
    private boolean votoEmBranco;

    private JLabel rotuloCargo;
    private JLabel rotuloNumero;
    private JLabel rotuloNome;
    private JLabel rotuloPartido;
    private JLabel rotuloFoto;
    private JLabel rotuloMensagem;
    private JButton botaoConfirmar;

    /**
     * Inicializa a tela com a lista de candidatos cadastrados em memoria.
     *
     * @param listaCandidatos Lista de candidatos disponiveis para busca.
     */
    public TelaUrnaSimples(List<Candidato> listaCandidatos) {
        this.listaCandidatos = listaCandidatos;
        this.numeroDigitado = "";
        this.votoEmBranco = false;

        configurarJanela();
        montarComponentes();
        atualizarVisor("Digite o numero do candidato (5 digitos) ou aperte BRANCO.");
    }

    /**
     * Define titulo, tamanho e comportamento ao fechar a janela.
     */
    private void configurarJanela() {
        setTitle("Exemplo 02 - Urna Swing com Foto Automatica");
        setSize(820, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    /**
     * Monta o visor (esquerda), o teclado (direita) e a barra de mensagem (baixo).
     */
    private void montarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(16, 12));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        painelPrincipal.setBackground(new Color(235, 238, 242));

        // --- LADO ESQUERDO: VISOR DA URNA ---
        JPanel painelVisor = new JPanel(new BorderLayout(12, 8));
        painelVisor.setBackground(Color.WHITE);
        painelVisor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 188, 198), 2),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JPanel painelDados = new JPanel(new GridLayout(4, 1, 0, 8));
        painelDados.setOpaque(false);

        rotuloCargo = new JLabel("SEU VOTO PARA: VEREADOR");
        rotuloCargo.setFont(new Font("SansSerif", Font.BOLD, 16));

        rotuloNumero = new JLabel();
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
        rotuloFoto.setPreferredSize(new Dimension(150, 180));
        rotuloFoto.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220), 1));

        painelVisor.add(painelDados, BorderLayout.CENTER);
        painelVisor.add(rotuloFoto, BorderLayout.EAST);

        // --- LADO DIREITO: TECLADO DA URNA ---
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
        botaoBranco.setForeground(Color.BLACK);
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
        botaoCorrigir.setForeground(Color.BLACK);
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
        botaoConfirmar.setForeground(Color.BLACK);
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

        // --- RODAPE: MENSAGENS DE ORIENTACAO ---
        rotuloMensagem = new JLabel(" ");
        rotuloMensagem.setFont(new Font("SansSerif", Font.BOLD, 13));
        rotuloMensagem.setForeground(new Color(44, 62, 80));

        painelPrincipal.add(painelVisor, BorderLayout.CENTER);
        painelPrincipal.add(painelTeclado, BorderLayout.EAST);
        painelPrincipal.add(rotuloMensagem, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
    }

    /**
     * Cria cada tecla numerica de 0 a 9.
     */
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
     * Acrescenta um digito ao numero atual, se ainda nao atingiu o limite.
     */
    public void digitarNumero(int digito) {
        if (votoEmBranco) {
            return; // Em voto branco, o eleitor precisa apertar CORRIGE para digitar
        }
        if (numeroDigitado.length() < QUANTIDADE_DIGITOS) {
            numeroDigitado = numeroDigitado + digito;
            atualizarVisor("");
        }
    }

    /**
     * Ativa o modo de voto em branco (somente quando nenhum numero foi digitado).
     */
    public void acionarVotoBranco() {
        if (numeroDigitado.isEmpty()) {
            votoEmBranco = true;
            atualizarVisor("Aperte CONFIRMA para confirmar o voto em BRANCO ou CORRIGE.");
        } else {
            rotuloMensagem.setText("Para votar em BRANCO, aperte CORRIGE primeiro.");
        }
    }

    /**
     * Limpa a digitacao atual e apaga os dados e a foto do visor.
     */
    public void acionarCorrige() {
        numeroDigitado = "";
        votoEmBranco = false;
        atualizarVisor("Voto corrigido. Digite novamente.");
    }

    /**
     * Simula a confirmacao do voto (neste exemplo apenas exibe mensagem na tela).
     */
    private void acionarConfirma() {
        String resumo;
        if (votoEmBranco) {
            resumo = "Voto em BRANCO confirmado (simulacao em memoria)!";
        } else {
            Candidato candidato = buscarCandidatoPorNumero(numeroDigitado);
            if (candidato != null) {
                resumo = "Voto confirmado para " + candidato.getNome() + " (" + candidato.getNumero() + ")!";
            } else {
                resumo = "Voto NULO confirmado (numero " + numeroDigitado + ")!";
            }
        }
        numeroDigitado = "";
        votoEmBranco = false;
        atualizarVisor(resumo);
    }

    /**
     * Busca um candidato na lista pelo numero completo.
     *
     * @param numero Numero digitado pelo eleitor.
     * @return O candidato encontrado ou null se o numero nao existir.
     */
    private Candidato buscarCandidatoPorNumero(String numero) {
        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato candidato = listaCandidatos.get(i);
            if (candidato.getNumero().equals(numero)) {
                return candidato;
            }
        }
        return null;
    }

    /**
     * Atualiza todos os rotulos do visor.
     * E AQUI que ocorre o mecanismo de exibicao da foto quando todos os digitos sao preenchidos!
     */
    private void atualizarVisor(String aviso) {
        // 1. Sempre limpa os dados anteriores antes de redesenhar
        rotuloNome.setText(" ");
        rotuloPartido.setText(" ");
        rotuloFoto.setIcon(null);
        rotuloFoto.setText("");

        // 2. Formata o numero com sublinhados para os digitos que ainda faltam (ex: 910__)
        StringBuilder mascara = new StringBuilder(numeroDigitado);
        while (mascara.length() < QUANTIDADE_DIGITOS) {
            mascara.append("_");
        }
        rotuloNumero.setText(votoEmBranco ? "BRANCO" : mascara.toString());

        // 3. Verifica o estado da votacao
        if (votoEmBranco) {
            rotuloNome.setText("VOTO EM BRANCO");
            botaoConfirmar.setEnabled(true);
        } else if (numeroDigitado.length() == QUANTIDADE_DIGITOS) {
            // Todos os digitos foram preenchidos: agora procuramos o candidato!
            Candidato candidato = buscarCandidatoPorNumero(numeroDigitado);
            if (candidato != null) {
                rotuloNome.setText("Nome: " + candidato.getNome());
                rotuloPartido.setText("Partido: " + candidato.getPartido());
                carregarFotoCandidato(candidato);
                aviso = "Aperte CONFIRMA para votar neste candidato ou CORRIGE.";
            } else {
                rotuloNome.setText("NUMERO ERRADO");
                rotuloPartido.setText("VOTO NULO");
                rotuloFoto.setText("Sem candidato");
                aviso = "Numero inexistente. Se confirmar, seu voto sera NULO.";
            }
            botaoConfirmar.setEnabled(true);
        } else {
            // Ainda faltam digitos: nao mostra foto e desabilita o botao CONFIRMA
            botaoConfirmar.setEnabled(false);
            if (aviso.isEmpty()) {
                aviso = "Continue digitando (" + numeroDigitado.length() + "/" + QUANTIDADE_DIGITOS + ")...";
            }
        }

        rotuloMensagem.setText(aviso);
    }

    /**
     * Carrega a imagem do candidato a partir do classpath e ajusta o tamanho ao rotulo.
     *
     * @param candidato Candidato cuja foto sera exibida.
     */
    private void carregarFotoCandidato(Candidato candidato) {
        URL urlImagem = getClass().getResource(candidato.getCaminhoFoto());
        if (urlImagem == null) {
            rotuloFoto.setText("Foto nao encontrada");
            return;
        }
        ImageIcon iconeOriginal = new ImageIcon(urlImagem);
        Image imagemRedimensionada = iconeOriginal.getImage().getScaledInstance(140, 170, Image.SCALE_SMOOTH);
        rotuloFoto.setIcon(new ImageIcon(imagemRedimensionada));
    }
}
