package br.edu.urna.rest;

import br.edu.urna.dados.PersistenciaException;
import br.edu.urna.swing.UrnaSwing;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class UrnaSwingComFirestoreRest {
    public static void main(String[] args) {
        try {
            var repositorio = FirestoreProxyRest.abrir(Path.of(".env"));
            try {
                UrnaSwing.abrir(repositorio, repositorio::close);
            } catch (PersistenciaException | RuntimeException erro) {
                repositorio.close();
                throw erro;
            }
        } catch (IOException | PersistenciaException | RuntimeException erro) {
            System.getLogger(UrnaSwingComFirestoreRest.class.getName())
                    .log(System.Logger.Level.ERROR, "Falha ao abrir a urna", erro);
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Confira o .env, a conexão e as regras do projeto. Veja o console para mais detalhes.",
                    "Conexão com o Firestore", JOptionPane.ERROR_MESSAGE));
        }
    }
}
