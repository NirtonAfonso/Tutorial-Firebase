/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy (Camada Service)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Excecao lancada pela classe Proxy quando o prefixo de partido informado nao existe.
 */
package br.edu.urna.proxy;

/**
 * Excecao verificada lancada quando o numero de partido informado esta errado.
 */
public class NumeroErradoException extends Exception {

    public NumeroErradoException() {
        super("NUMERO ERRADO");
    }

    public NumeroErradoException(String mensagem) {
        super(mensagem);
    }
}
