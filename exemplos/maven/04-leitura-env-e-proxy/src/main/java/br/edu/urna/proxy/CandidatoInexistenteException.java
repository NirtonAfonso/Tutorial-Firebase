/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy (Camada Service)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Excecao lancada pela classe Proxy (ServiceCandidatoProxy) quando o numero
 * completo digitado nao corresponde a nenhum candidato cadastrado no banco.
 */
package br.edu.urna.proxy;

/**
 * Excecao verificada lancada quando um candidato nao e encontrado na base de dados.
 */
public class CandidatoInexistenteException extends Exception {

    public CandidatoInexistenteException() {
        super("CANDIDATO INEXISTENTE");
    }

    public CandidatoInexistenteException(String mensagem) {
        super(mensagem);
    }
}
