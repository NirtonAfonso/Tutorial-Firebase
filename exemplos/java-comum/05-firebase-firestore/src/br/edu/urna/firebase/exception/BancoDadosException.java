/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.firebase.exception;

/**
 * Excecao verificada (checked) lancada quando ocorre falha ao acessar o Firebase.
 */
public class BancoDadosException extends Exception {

    public BancoDadosException(String mensagem) {
        super(mensagem);
    }

    public BancoDadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
