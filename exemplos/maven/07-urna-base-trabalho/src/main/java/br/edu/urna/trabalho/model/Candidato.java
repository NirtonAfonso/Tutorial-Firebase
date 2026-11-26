/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.model;

/**
 * Entidade de modelo que representa um candidato vinculado a um cargo.
 */
public class Candidato {

    private String numero;
    private String nome;
    private String partido;
    private String nomeCargo;
    private String arquivoFoto;

    public Candidato(String numero, String nome, String partido, String nomeCargo, String arquivoFoto) {
        this.numero = numero;
        this.nome = nome;
        this.partido = partido;
        this.nomeCargo = nomeCargo;
        this.arquivoFoto = arquivoFoto;
    }

    public String getNumero() {
        return numero;
    }

    public String getNome() {
        return nome;
    }

    public String getPartido() {
        return partido;
    }

    public String getNomeCargo() {
        return nomeCargo;
    }

    public String getArquivoFoto() {
        return arquivoFoto;
    }

    public String getCaminhoFoto() {
        return "/fotos/" + arquivoFoto;
    }
}
