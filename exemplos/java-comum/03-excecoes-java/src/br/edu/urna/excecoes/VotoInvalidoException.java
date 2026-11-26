/*
 * Exemplo 03 - Tratamento de Excecoes em Java
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Excecao personalizada (Checked Exception) para regras de validacao de voto.
 */
package br.edu.urna.excecoes;

/**
 * Excecao verificada (checked) lancada quando um voto nao atende as regras da urna.
 * Por herdar de Exception, o compilador obriga quem chama o metodo a tratar com try/catch
 * ou propagar com throws.
 */
public class VotoInvalidoException extends Exception {

    /**
     * Construtor padrao sem mensagem.
     */
    public VotoInvalidoException() {
        super("Os dados do voto informado sao invalidos.");
    }

    /**
     * Construtor com mensagem explicativa personalizada.
     *
     * @param mensagem Descricao clara do problema encontrado.
     */
    public VotoInvalidoException(String mensagem) {
        super(mensagem);
    }
}
