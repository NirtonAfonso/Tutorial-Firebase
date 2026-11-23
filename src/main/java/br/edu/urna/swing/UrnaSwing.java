package br.edu.urna.swing;

import br.edu.urna.dados.PersistenciaException;
import br.edu.urna.dados.RepositorioEmMemoria;
import br.edu.urna.dados.RepositorioUrna;
import br.edu.urna.dominio.PainelVotacao;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class UrnaSwing {
    public static void main(String[] args) throws PersistenciaException {
        abrir(new RepositorioEmMemoria(), () -> {});
    }

    public static void abrir(RepositorioUrna repositorio, Runnable aoFechar) throws PersistenciaException {
        // main roda fora da thread da tela. Aqui podemos buscar os candidatos uma vez.
        var candidatos = repositorio.listarCandidatos();
        if (candidatos.isEmpty()) {
            throw new IllegalArgumentException("Cadastre os candidatos do laboratório antes de abrir a urna.");
        }
        var votacao = new PainelVotacao(2, candidatos);

        SwingUtilities.invokeLater(() -> {
            var janela = new JFrame("Urna de treino | Swing");
            janela.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            janela.setContentPane(new PainelSwing(votacao, repositorio));
            janela.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent evento) {
                    // O fechamento da conexão também pode levar tempo.
                    new Thread(aoFechar, "fechar-repositorio").start();
                }
            });
            janela.pack();
            janela.setLocationRelativeTo(null);
            janela.setVisible(true);
        });
    }
}
