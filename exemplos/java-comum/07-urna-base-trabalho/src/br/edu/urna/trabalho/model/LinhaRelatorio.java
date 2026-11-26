/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.model;

/**
 * Representa uma linha do relatorio (Zeresima ou Apuracao Final) de um cargo.
 */
public class LinhaRelatorio {

    private String descricao;
    private String numero;
    private int quantidadeVotos;
    private double percentual;

    public LinhaRelatorio(String descricao, String numero, int quantidadeVotos, double percentual) {
        this.descricao = descricao;
        this.numero = numero;
        this.quantidadeVotos = quantidadeVotos;
        this.percentual = percentual;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNumero() {
        return numero;
    }

    public int getQuantidadeVotos() {
        return quantidadeVotos;
    }

    public double getPercentual() {
        return percentual;
    }

    /**
     * Formata a linha exatamente no padrao pedido no enunciado da AV3.
     * Exemplo: "Golfe (91001): 50% (2 votos)" ou "Votos Brancos: 25% (1 votos)".
     */
    public String formatarLinhaPadrao() {
        long percentualArredondado = Math.round(percentual);
        if (numero != null && !numero.isEmpty()) {
            return descricao + " (" + numero + "): " + percentualArredondado + "% (" + quantidadeVotos + " votos)";
        }
        return descricao + ": " + percentualArredondado + "% (" + quantidadeVotos + " votos)";
    }
}
