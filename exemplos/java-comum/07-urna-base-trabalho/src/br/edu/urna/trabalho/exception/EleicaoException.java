/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.exception;

/**
 * Excecao lancada em violacoes das regras da eleicao (ex: senha incorreta ou urna fechada).
 */
public class EleicaoException extends Exception {

    public EleicaoException(String mensagem) {
        super(mensagem);
    }
}
