/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Interface Swing conectada ao Firebase atraves da interface UrnaRepository.
 * Repare que esta tela NAO conhece detalhes de HTTP, JSON ou Firebase!
 */
package br.edu.urna.firebase.view;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.model.Voto;
import br.edu.urna.firebase.proxy.UrnaRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * Tela da Urna conectada a um UrnaRepository (neste exemplo, o Cloud Firestore).
 * Grava o voto em uma Thread separada para evitar que a janela trave durante o envio.
 */
public class TelaUrnaFirebase extends JFrame {

    private static final int QUANTIDADE_DIGITOS = 5;

    private UrnaRepository repositorio;
    private List<Candidato> listaCandidatos;
    private String numeroDigitado;
    private boolean votoEmBranco;
    private boolean enviandoVoto;

    private JLabel rotuloNumero;
    private JLabel rotuloNome;
    private JLabel rotuloPartido;
    private JLabel rotuloFoto;
    private JLabel rotuloMensagem;
    private JButton botaoConfirmar;

    public TelaUrnaFirebase(UrnaRepository repositorio, List<Candidato> listaCandidatos) {
        this.repositorio = repositorio;
        this.listaCandidatos = listaCandidatos;
        this.numeroDigitado = "";
        this.votoEmBranco = false;
        this.enviandoVoto = false;

        configurarJanela();
        montarComponentes();
        atualizarVisor("Conectado ao Firebase. Digite o numero (5 digitos) ou aperte BRANCO.");
    }

    private void configurarJanela() {
        setTitle("Exemplo 05 - Urna Swing Conectada ao Cloud Firestore");
        setSize(820, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void montarComponentes() {
        JPanel painelPrincipal = new JPanel(new BorderLayout(16, 12));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        painelPrincipal.setBackground(new Color(235, 238, 242));

        JPanel painelVisor = new JPanel(new BorderLayout(12, 8));
        painelVisor.setBackground(Color.WHITE);
        painelVisor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 188, 198), 2),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JPanel painelDados = new JPanel(new GridLayout(4, 1, 0, 8));
        painelDados.setOpaque(false);

        JLabel rotuloCargo = new JLabel("SEU VOTO PARA: VEREADOR (FIREBASE)");
        rotuloCargo.setFont(new Font("SansSerif", Font.BOLD, 15));

        rotuloNumero = new JLabel();
        rotuloNumero.setFont(new Font("Monospaced", Font.BOLD, 34));
        rotuloNumero.setForeground(new Color(25, 42, 86));

        rotuloNome = new JLabel(" ");
        rotuloNome.setFont(new Font("SansSerif", Font.BOLD, 18));

        rotuloPartido = new JLabel(" ");
        rotuloPartido.setFont(new Font("SansSerif", Font.PLAIN, 15));

        painelDados.add(rotuloCargo);
        painelDados.add(rotuloNumero);
        painelDados.add(rotuloNome);
        painelDados.add(rotuloPartido);

        rotuloFoto = new JLabel("", SwingConstants.CENTER);
        rotuloFoto.setPreferredSize(new Dimension(150, 180));
        rotuloFoto.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 220), 1));

        painelVisor.add(painelDados, BorderLayout.CENTER);
        painelVisor.add(rotuloFoto, BorderLayout.EAST);

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
                if (!enviandoVoto && numeroDigitado.isEmpty()) {
                    votoEmBranco = true;
                    atualizarVisor("Aperte CONFIRMA para gravar o voto em BRANCO no Firebase.");
                }
            }
        });

        JButton botaoCorrigir = new JButton("CORRIGE");
        botaoCorrigir.setBackground(new Color(243, 156, 18));
        botaoCorrigir.setFont(new Font("SansSerif", Font.BOLD, 11));
        botaoCorrigir.setMargin(new Insets(6, 4, 6, 4));
        botaoCorrigir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                if (!enviandoVoto) {
                    numeroDigitado = "";
                    votoEmBranco = false;
                    atualizarVisor("Voto corrigido.");
                }
            }
        });

        botaoConfirmar = new JButton("CONFIRMA");
        botaoConfirmar.setBackground(new Color(39, 174, 96));
        botaoConfirmar.setFont(new Font("SansSerif", Font.BOLD, 11));
        botaoConfirmar.setMargin(new Insets(6, 4, 6, 4));
        botaoConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evento) {
                enviarVotoParaBanco();
            }
        });

        painelAcoes.add(botaoBranco);
        painelAcoes.add(botaoCorrigir);
        painelAcoes.add(botaoConfirmar);

        painelTeclado.add(painelNumeros, BorderLayout.CENTER);
        painelTeclado.add(painelAcoes, BorderLayout.SOUTH);

        rotuloMensagem = new JLabel(" ");
        rotuloMensagem.setFont(new Font("SansSerif", Font.BOLD, 13));

        painelPrincipal.add(painelVisor, BorderLayout.CENTER);
        painelPrincipal.add(painelTeclado, BorderLayout.EAST);
        painelPrincipal.add(rotuloMensagem, BorderLayout.SOUTH);

        setContentPane(painelPrincipal);
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
                if (!enviandoVoto && !votoEmBranco && numeroDigitado.length() < QUANTIDADE_DIGITOS) {
                    numeroDigitado = numeroDigitado + valor;
                    atualizarVisor("");
                }
            }
        });
        return botao;
    }

    /**
     * Monta o objeto Voto e realiza a gravacao no Firebase fora da thread da tela,
     * impedindo que a interface congele enquanto aguarda a resposta da internet.
     */
    private void enviarVotoParaBanco() {
        if (enviandoVoto) {
            return;
        }

        String tipo;
        String numeroParaSalvar;
        if (votoEmBranco) {
            tipo = "BRANCO";
            numeroParaSalvar = "";
        } else if (buscarCandidatoPorNumero(numeroDigitado) != null) {
            tipo = "NOMINAL";
            numeroParaSalvar = numeroDigitado;
        } else {
            tipo = "NULO";
            numeroParaSalvar = numeroDigitado;
        }

        final Voto voto = new Voto("Vereador", tipo, numeroParaSalvar, LocalDateTime.now().toString());
        enviandoVoto = true;
        botaoConfirmar.setEnabled(false);
        rotuloMensagem.setText("Enviando voto para o Firebase...");

        Thread threadEnvio = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    repositorio.salvarVoto(voto);

                    // Toda atualizacao visual no Swing deve voltar para a Event Dispatch Thread
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            enviandoVoto = false;
                            numeroDigitado = "";
                            votoEmBranco = false;
                            atualizarVisor("Voto gravado com sucesso no Cloud Firestore!");
                        }
                    });
                } catch (final BancoDadosException erro) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            enviandoVoto = false;
                            botaoConfirmar.setEnabled(true);
                            rotuloMensagem.setText("Falha ao salvar voto. Tente confirmar novamente.");
                            JOptionPane.showMessageDialog(
                                    TelaUrnaFirebase.this,
                                    "Nao foi possivel salvar o voto no Firebase:\n" + erro.getMessage(),
                                    "Erro de Conexao",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    });
                }
            }
        });
        threadEnvio.start();
    }

    private Candidato buscarCandidatoPorNumero(String numero) {
        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato candidato = listaCandidatos.get(i);
            if (candidato.getNumero().equals(numero)) {
                return candidato;
            }
        }
        return null;
    }

    private void atualizarVisor(String aviso) {
        rotuloNome.setText(" ");
        rotuloPartido.setText(" ");
        rotuloFoto.setIcon(null);
        rotuloFoto.setText("");

        StringBuilder mascara = new StringBuilder(numeroDigitado);
        while (mascara.length() < QUANTIDADE_DIGITOS) {
            mascara.append("_");
        }
        rotuloNumero.setText(votoEmBranco ? "BRANCO" : mascara.toString());

        if (votoEmBranco) {
            rotuloNome.setText("VOTO EM BRANCO");
            botaoConfirmar.setEnabled(true);
        } else if (numeroDigitado.length() == QUANTIDADE_DIGITOS) {
            Candidato candidato = buscarCandidatoPorNumero(numeroDigitado);
            if (candidato != null) {
                rotuloNome.setText("Nome: " + candidato.getNome());
                rotuloPartido.setText("Partido: " + candidato.getPartido());
                carregarFoto(candidato);
                aviso = "Aperte CONFIRMA para gravar no Firebase ou CORRIGE.";
            } else {
                rotuloNome.setText("NUMERO ERRADO");
                rotuloPartido.setText("VOTO NULO");
                aviso = "Numero nao encontrado. Confirmar gravara um voto NULO.";
            }
            botaoConfirmar.setEnabled(true);
        } else {
            botaoConfirmar.setEnabled(false);
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
        Image redimensionada = icone.getImage().getScaledInstance(140, 170, Image.SCALE_SMOOTH);
        rotuloFoto.setIcon(new ImageIcon(redimensionada));
    }
}
