package br.edu.urna.swing;

import br.edu.urna.dados.PersistenciaException;
import br.edu.urna.dados.RepositorioUrna;
import br.edu.urna.dominio.PainelVotacao;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public final class PainelSwing extends JPanel {
    private final PainelVotacao votacao;
    private final RepositorioUrna repositorio;
    private final JLabel numero = new JLabel();
    private final JLabel nome = new JLabel(" ");
    private final JLabel partido = new JLabel(" ");
    private final JLabel foto = new JLabel("", SwingConstants.CENTER);
    private final JLabel mensagem = new JLabel(" ");
    private final List<JButton> teclasNumericas = new ArrayList<>();
    private final JButton branco = new JButton("BRANCO");
    private final JButton corrige = new JButton("CORRIGE");
    private final JButton confirma = new JButton("CONFIRMA");
    private boolean gravando;

    public PainelSwing(PainelVotacao votacao, RepositorioUrna repositorio) {
        super(new BorderLayout(18, 12));
        this.votacao = votacao;
        this.repositorio = repositorio;
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setPreferredSize(new Dimension(650, 300));

        var visor = new JPanel(new BorderLayout(10, 10));
        visor.setBackground(Color.WHITE);
        visor.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        var dados = new JPanel(new GridLayout(4, 1));
        dados.setOpaque(false);
        dados.add(new JLabel("Cargo de treino | Prefeito"));
        numero.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
        dados.add(numero);
        dados.add(nome);
        dados.add(partido);
        visor.add(dados, BorderLayout.CENTER);
        foto.setPreferredSize(new Dimension(120, 120));
        visor.add(foto, BorderLayout.EAST);

        var teclado = new JPanel(new BorderLayout(0, 8));
        var numeros = new JPanel(new GridLayout(4, 3, 5, 5));
        for (int valor = 1; valor <= 9; valor++) {
            numeros.add(criarTecla(valor));
        }
        numeros.add(new JLabel());
        numeros.add(criarTecla(0));
        numeros.add(new JLabel());

        var acoes = new JPanel(new GridLayout(1, 3, 5, 5));
        branco.addActionListener(evento -> {
            votacao.votarEmBranco();
            atualizarVisor("");
        });
        corrige.addActionListener(evento -> {
            votacao.corrigir();
            atualizarVisor("");
        });
        confirma.addActionListener(evento -> confirmar());
        branco.setBackground(Color.WHITE);
        corrige.setBackground(new Color(255, 177, 101));
        confirma.setBackground(new Color(139, 218, 160));
        acoes.add(branco);
        acoes.add(corrige);
        acoes.add(confirma);
        teclado.add(numeros, BorderLayout.CENTER);
        teclado.add(acoes, BorderLayout.SOUTH);

        add(visor, BorderLayout.CENTER);
        add(teclado, BorderLayout.EAST);
        add(mensagem, BorderLayout.SOUTH);
        atualizarVisor("");
    }

    private JButton criarTecla(int valor) {
        var tecla = new JButton(Integer.toString(valor));
        tecla.addActionListener(evento -> {
            votacao.digitar(valor);
            atualizarVisor("");
        });
        teclasNumericas.add(tecla);
        return tecla;
    }

    private void atualizarVisor(String aviso) {
        numero.setText((votacao.numero() + "__").substring(0, 2));
        nome.setText(" ");
        partido.setText(" ");
        foto.setIcon(null);
        foto.setText("");
        var selecionado = votacao.candidato();

        if (selecionado.isPresent()) {
            var candidato = selecionado.get();
            nome.setText(candidato.nome());
            partido.setText("Partido " + candidato.partido());
            URL caminho = getClass().getResource(candidato.caminhoDaFoto());
            if (caminho == null) {
                foto.setText("Sem foto");
            } else {
                foto.setIcon(new ImageIcon(caminho));
            }
        } else if (votacao.branco()) {
            nome.setText("Voto em branco");
        } else if (votacao.completo()) {
            nome.setText("Número inexistente. Confirme para votar nulo.");
        }

        boolean podeEditar = !gravando && !votacao.temEnvioPendente();
        teclasNumericas.forEach(tecla -> tecla.setEnabled(podeEditar && !votacao.completo() && !votacao.branco()));
        branco.setEnabled(podeEditar && votacao.numero().isEmpty() && !votacao.branco());
        corrige.setEnabled(podeEditar);
        confirma.setEnabled(!gravando && votacao.podeConfirmar());
        mensagem.setText(aviso.isEmpty() ? " " : aviso);
    }

    private void confirmar() {
        if (gravando || !votacao.podeConfirmar()) {
            return;
        }
        var voto = votacao.prepararConfirmacao();
        gravando = true;
        atualizarVisor("Registrando o voto…");

        // doInBackground salva. done executa na thread do Swing e atualiza o visor.
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws PersistenciaException {
                repositorio.salvar(voto);
                return null;
            }

            @Override
            protected void done() {
                gravando = false;
                try {
                    get();
                    votacao.votoRegistrado();
                    atualizarVisor("Voto registrado. A urna está pronta para outro teste.");
                } catch (InterruptedException erro) {
                    Thread.currentThread().interrupt();
                    atualizarVisor("Envio interrompido. Confira a conexão e aperte CONFIRMA novamente.");
                } catch (ExecutionException erro) {
                    System.getLogger(PainelSwing.class.getName())
                            .log(System.Logger.Level.WARNING, "Falha ao registrar voto", erro.getCause());
                    atualizarVisor("Sem confirmação do banco. Confira a conexão e aperte CONFIRMA novamente.");
                }
            }
        }.execute();
    }
}
