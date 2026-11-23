package br.edu.urna.laboratorio;

import br.edu.urna.dados.FirebaseConexao;
import br.edu.urna.dados.FirestoreProxy;
import br.edu.urna.dados.PersistenciaException;
import br.edu.urna.swing.UrnaSwing;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class UrnaSwingComFirestore {
    public static void main(String[] args) {
        try {
            var conexao = FirebaseConexao.abrir();
            try {
                UrnaSwing.abrir(new FirestoreProxy(conexao.banco()), conexao::close);
            } catch (PersistenciaException | RuntimeException erro) {
                conexao.close();
                throw erro;
            }
        } catch (IOException | PersistenciaException | RuntimeException erro) {
            System.getLogger(UrnaSwingComFirestore.class.getName())
                    .log(System.Logger.Level.ERROR, "Falha ao abrir a urna com o Firestore", erro);
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Não consegui abrir a urna. Confira o .env, a conexão e os candidatos do laboratório.",
                    "Conexão com o Firestore", JOptionPane.ERROR_MESSAGE));
        }
    }
}
