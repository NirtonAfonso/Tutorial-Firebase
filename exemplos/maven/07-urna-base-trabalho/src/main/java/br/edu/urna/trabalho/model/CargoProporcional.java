/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.model;

/**
 * Especializacao de Cargo para eleicoes proporcionais (ex: Vereador com 5 digitos).
 */
public class CargoProporcional extends Cargo {

    public CargoProporcional(String nome, int quantidadeDigitos) {
        super(nome, quantidadeDigitos);
    }

    @Override
    public String getTipoSistemaEleitoral() {
        return "Proporcional";
    }
}
