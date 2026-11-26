/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Demonstra Abstracao e Heranca: classe abstrata Cargo.
 */
package br.edu.urna.trabalho.model;

/**
 * Classe abstrata que representa um cargo eleitoral.
 * Subclasses definem regras especificas (proporcional ou majoritario),
 * demonstrando os pilares de Abstracao e Heranca da POO.
 */
public abstract class Cargo {

    private String nome;
    private int quantidadeDigitos;

    /**
     * Construtor base de um cargo.
     *
     * @param nome              Nome do cargo (ex: "Vereador", "Prefeito").
     * @param quantidadeDigitos Quantidade exata de digitos para votar neste cargo.
     */
    protected Cargo(String nome, int quantidadeDigitos) {
        this.nome = nome;
        this.quantidadeDigitos = quantidadeDigitos;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeDigitos() {
        return quantidadeDigitos;
    }

    /**
     * Retorna a categoria eleitoral do cargo (Proporcional ou Majoritario).
     */
    public abstract String getTipoSistemaEleitoral();
}
