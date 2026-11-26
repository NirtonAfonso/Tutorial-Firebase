/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.firebase;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.proxy.FirestoreProxyRest;
import br.edu.urna.firebase.proxy.UrnaRepository;
import br.edu.urna.firebase.view.TelaUrnaFirebase;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da Urna Swing conectada ao Firebase (trilha Java Comum).
 */
public class MainUrnaFirebase {

    public static void main(String[] args) {
        try {
            final UrnaRepository repositorio = new FirestoreProxyRest(".env");
            final List<Candidato> candidatos = repositorio.listarCandidatos();

            if (candidatos.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Conexao realizada, mas nao ha candidatos na colecao 'candidatos' do Firestore.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    TelaUrnaFirebase tela = new TelaUrnaFirebase(repositorio, candidatos);
                    tela.setVisible(true);
                }
            });
        } catch (BancoDadosException erro) {
            JOptionPane.showMessageDialog(
                    null,
                    "Nao foi possivel iniciar a urna com Firebase:\n" + erro.getMessage(),
                    "Erro ao Conectar",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
