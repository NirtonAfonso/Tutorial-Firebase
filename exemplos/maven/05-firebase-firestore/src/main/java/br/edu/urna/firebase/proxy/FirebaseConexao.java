/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Maven / Admin SDK)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Responsavel por ler o arquivo .env, autenticar usando o arquivo JSON da
 * conta de servico e fornecer a instancia do Cloud Firestore (padrao Singleton).
 */
package br.edu.urna.firebase.proxy;

import br.edu.urna.firebase.exception.BancoDadosException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Gerencia a inicializacao unica da conexao com o Firebase Admin SDK.
 * Le o caminho das credenciais a partir do arquivo .env (boas praticas de seguranca).
 */
public class FirebaseConexao {

    private static Firestore instanciaFirestore;

    /**
     * Construtor privado para garantir uma unica conexao (padrao Singleton).
     */
    private FirebaseConexao() {
    }

    /**
     * Retorna a instancia conectada do Cloud Firestore, inicializando-a na primeira chamada.
     *
     * @return Objeto Firestore pronto para leitura e escrita.
     * @throws BancoDadosException Se o arquivo .env ou o JSON de credenciais falharem.
     */
    public static Firestore obterFirestore() throws BancoDadosException {
        if (instanciaFirestore != null) {
            return instanciaFirestore;
        }

        try {
            ConfiguracaoEnv configuracao = ConfiguracaoEnv.carregar(".env");
            String projetoId = configuracao.obterObrigatorio("FIREBASE_PROJECT_ID");
            String caminhoChave = configuracao.obterObrigatorio("FIREBASE_CREDENTIALS");

            File arquivoChave = new File(caminhoChave);
            if (!arquivoChave.exists()) {
                throw new BancoDadosException(
                        "Arquivo JSON da conta de servico nao encontrado em: " + arquivoChave.getAbsolutePath()
                );
            }

            try (FileInputStream fluxoChave = new FileInputStream(arquivoChave)) {
                GoogleCredentials credenciais = GoogleCredentials.fromStream(fluxoChave);

                FirebaseOptions opcoes = FirebaseOptions.builder()
                        .setCredentials(credenciais)
                        .setProjectId(projetoId)
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(opcoes);
                }

                instanciaFirestore = FirestoreClient.getFirestore();
                return instanciaFirestore;
            }
        } catch (IOException | IllegalArgumentException erro) {
            throw new BancoDadosException("Falha ao inicializar conexao com o Firebase: " + erro.getMessage(), erro);
        }
    }
}
