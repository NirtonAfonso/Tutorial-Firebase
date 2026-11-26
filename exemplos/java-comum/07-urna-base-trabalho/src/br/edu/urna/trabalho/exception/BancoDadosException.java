/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.exception;

/**
 * Excecao lancada em falhas de persistencia de dados (ex: comunicacao com Firebase).
 */
public class BancoDadosException extends Exception {

    public BancoDadosException(String mensagem) {
        super(mensagem);
    }

    public BancoDadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
