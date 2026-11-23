package br.edu.urna.dados;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class FirebaseConexao implements AutoCloseable {
    private final FirebaseApp app;
    private final Firestore banco;

    private FirebaseConexao(FirebaseApp app, Firestore banco) {
        this.app = app;
        this.banco = banco;
    }

    public static FirebaseConexao abrir() throws IOException {
        // O Java não carrega .env por conta própria. Lemos só linhas CHAVE=valor.
        Properties env = new Properties();
        try (var arquivo = Files.newBufferedReader(Path.of(".env"))) {
            env.load(arquivo);
        }
        String projeto = obrigatorio(env, "FIREBASE_PROJECT_ID");
        Path caminho = Path.of(obrigatorio(env, "FIREBASE_CREDENTIALS"));
        try (var arquivo = Files.newInputStream(caminho)) {
            var credenciais = ServiceAccountCredentials.fromStream(arquivo);
            if (!projeto.equals(credenciais.getProjectId())) {
                throw new IllegalArgumentException("O ID do .env precisa ser o mesmo da conta de serviço do laboratório.");
            }
            var opcoes = FirebaseOptions.builder()
                    .setProjectId(projeto)
                    .setCredentials(credenciais)
                    .build();
            var app = FirebaseApp.initializeApp(opcoes);
            try {
                return new FirebaseConexao(app, FirestoreClient.getFirestore(app));
            } catch (RuntimeException erro) {
                app.delete();
                throw erro;
            }
        }
    }

    public Firestore banco() {
        return banco;
    }

    @Override
    public void close() {
        // O FirebaseApp encerra os serviços administrados pelo SDK.
        app.delete();
    }

    private static String obrigatorio(Properties env, String chave) {
        String valor = env.getProperty(chave, "").trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("Preencha " + chave + " no arquivo .env.");
        }
        return valor;
    }
}
