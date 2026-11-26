/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.firebase.model;

/**
 * Representa um candidato armazenado na colecao "candidatos" do Cloud Firestore.
 */
public class Candidato {

    private String numero;
    private String nome;
    private String partido;
    private String cargo;
    private String arquivoFoto;

    public Candidato(String numero, String nome, String partido, String cargo, String arquivoFoto) {
        this.numero = numero;
        this.nome = nome;
        this.partido = partido;
        this.cargo = cargo;
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

    public String getCargo() {
        return cargo;
    }

    public String getArquivoFoto() {
        return arquivoFoto;
    }

    public String getCaminhoFoto() {
        return "/fotos/" + arquivoFoto;
    }

    @Override
    public String toString() {
        return numero + " - " + nome + " (" + partido + ") [" + cargo + "]";
    }
}
