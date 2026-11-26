/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Maven / Admin SDK)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.firebase;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.proxy.FirebaseConexao;
import br.edu.urna.firebase.proxy.FirestoreProxy;
import br.edu.urna.firebase.proxy.UrnaRepository;
import br.edu.urna.firebase.view.TelaUrnaFirebase;
import com.google.cloud.firestore.Firestore;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada da Urna Swing conectada ao Firebase via Admin SDK (trilha Maven).
 */
public class MainUrnaFirebase {

    public static void main(String[] args) {
        try {
            Firestore banco = FirebaseConexao.obterFirestore();
            final UrnaRepository repositorio = new FirestoreProxy(banco);
            final List<Candidato> candidatos = repositorio.listarCandidatos();

            if (candidatos.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Conexao realizada, mas a colecao 'candidatos' esta vazia no Firestore.",
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
                    "Nao foi possivel conectar ao Firebase:\n" + erro.getMessage(),
                    "Erro de Conexao",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
