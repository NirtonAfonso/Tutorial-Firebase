/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.model;

/**
 * Entidade de modelo que representa um voto confirmado para um cargo especifico.
 */
public class Voto {

    private String nomeCargo;
    private TipoVoto tipoVoto;
    private String numeroDigitado;
    private String dataHora;

    public Voto(String nomeCargo, TipoVoto tipoVoto, String numeroDigitado, String dataHora) {
        this.nomeCargo = nomeCargo;
        this.tipoVoto = tipoVoto;
        this.numeroDigitado = numeroDigitado;
        this.dataHora = dataHora;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public TipoVoto getTipoVoto() {
        return tipoVoto;
    }

    public String getNumeroDigitado() {
        return numeroDigitado;
    }

    public String getDataHora() {
        return dataHora;
    }
}
