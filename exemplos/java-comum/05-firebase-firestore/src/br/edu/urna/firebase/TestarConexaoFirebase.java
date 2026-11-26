/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Teste de console para validar o arquivo .env, a autenticacao e o Cloud Firestore
 * antes de abrir a tela grafica Swing.
 */
package br.edu.urna.firebase;

import br.edu.urna.firebase.exception.BancoDadosException;
import br.edu.urna.firebase.model.Candidato;
import br.edu.urna.firebase.model.Voto;
import br.edu.urna.firebase.proxy.FirestoreProxyRest;
import br.edu.urna.firebase.proxy.UrnaRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Classe de teste rapido no console para conferir leitura e gravacao no Firestore.
 */
public class TestarConexaoFirebase {

    public static void main(String[] args) {
        System.out.println("=== Teste de Conexao com Cloud Firestore (Java Comum / REST) ===");

        try {
            UrnaRepository repositorio = new FirestoreProxyRest(".env");

            System.out.println("1. Buscando candidatos cadastrados no Firebase...");
            List<Candidato> candidatos = repositorio.listarCandidatos();
            if (candidatos.isEmpty()) {
                System.out.println("Conexao OK! Mas a colecao 'candidatos' ainda esta vazia.");
                System.out.println("Cadastre os candidatos no Console Firebase antes de testar a urna.");
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

            System.out.println("SUCESSO! Confira o novo documento na colecao 'votos' do Console Firebase.");
        } catch (BancoDadosException erro) {
            System.err.println("ERRO NA CONEXAO: " + erro.getMessage());
            if (erro.getCause() != null) {
                System.err.println("Causa tecnica: " + erro.getCause().getMessage());
            }
        }
    }
}
