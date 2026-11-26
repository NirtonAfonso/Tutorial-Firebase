/*
 * Exemplo 06 - Interface Grafica com JavaFX
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.javafx.model;

/**
 * Representa um candidato para a demonstracao em JavaFX.
 */
public class Candidato {

    private String numero;
    private String nome;
    private String partido;
    private String arquivoFoto;

    public Candidato(String numero, String nome, String partido, String arquivoFoto) {
        this.numero = numero;
        this.nome = nome;
        this.partido = partido;
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

    public String getCaminhoFoto() {
        return "/fotos/" + arquivoFoto;
    }
}
