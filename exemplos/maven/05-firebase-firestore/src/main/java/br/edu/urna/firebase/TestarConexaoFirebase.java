/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Maven / Admin SDK)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Teste de console para validar o .env, a chave JSON e o Firestore antes de abrir o Swing.
 */
package br.edu.urna.firebase;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.model.Voto;
import br.edu.urna.firebase.proxy.FirebaseConexao;
import br.edu.urna.firebase.proxy.FirestoreProxy;
import br.edu.urna.firebase.proxy.UrnaRepository;
import com.google.cloud.firestore.Firestore;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Executa um teste simples de leitura e gravacao no Cloud Firestore via Admin SDK.
 */
public class TestarConexaoFirebase {

    public static void main(String[] args) {
        System.out.println("=== Teste de Conexao com Cloud Firestore (Maven / Admin SDK) ===");

        try {
            Firestore banco = FirebaseConexao.obterFirestore();
            UrnaRepository repositorio = new FirestoreProxy(banco);

            System.out.println("1. Buscando candidatos na colecao 'candidatos'...");
            List<Candidato> candidatos = repositorio.listarCandidatos();
            if (candidatos.isEmpty()) {
                System.out.println("Conexao OK! Porem a colecao 'candidatos' ainda nao possui documentos.");
                System.out.println("Cadastre os candidatos no Console Firebase e execute novamente.");
                return;
            }

            for (int i = 0; i < candidatos.size(); i++) {
                System.out.println("   -> " + candidatos.get(i));
            }

            System.out.println("2. Gravando um voto de teste na colecao 'votos'...");
            Candidato primeiro = candidatos.get(0);
            Voto votoTeste = new Voto(
                    primeiro.getCargo(),
                    "NOMINAL",
                    primeiro.getNumero(),
                    LocalDateTime.now().toString()
            );
            repositorio.salvarVoto(votoTeste);

            System.out.println("SUCESSO! Abra o Console Firebase -> Cloud Firestore -> votos para ver o registro.");
        } catch (BancoDadosException erro) {
            System.err.println("ERRO NA CONEXAO: " + erro.getMessage());
            if (erro.getCause() != null) {
                System.err.println("Causa tecnica: " + erro.getCause().getMessage());
            }
        }
    }
}
