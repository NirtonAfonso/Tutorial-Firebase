/*
 * Exemplo 03 - Tratamento de Excecoes em Java
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Excecao personalizada (Checked Exception) para falhas de comunicacao com banco de dados.
 */
package br.edu.urna.excecoes;

/**
 * Excecao lancada quando ocorre falha ao ler ou gravar dados no banco (ex: Firebase).
 * Permite encapsular a causa tecnica original (Throwable) para facilitar o diagnostico.
 */
public class BancoDadosException extends Exception {

    /**
     * Construtor com mensagem explicativa.
     *
     * @param mensagem Mensagem amigavel sobre a falha no banco.
     */
    public BancoDadosException(String mensagem) {
        super(mensagem);
    }

    /**
     * Construtor com mensagem e causa tecnica original (encadeamento de excecoes).
     *
     * @param mensagem Mensagem amigavel sobre a falha.
     * @param causa    Excecao original disparada pela biblioteca ou rede.
     */
    public BancoDadosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
