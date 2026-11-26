/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.firebase.model;

/**
 * Representa um voto a ser persistido na colecao "votos" do Cloud Firestore.
 */
public class Voto {

    private String cargo;
    private String tipo;
    private String numeroCandidato;
    private String dataHora;

    /**
     * Cria um registro de voto.
     *
     * @param cargo           Nome do cargo (ex: "Vereador" ou "Prefeito").
     * @param tipo            Tipo do voto: "NOMINAL", "BRANCO" ou "NULO".
     * @param numeroCandidato Numero digitado pelo eleitor (vazio para voto em branco).
     * @param dataHora        Carimbo de data/hora do registro.
     */
    public Voto(String cargo, String tipo, String numeroCandidato, String dataHora) {
        this.cargo = cargo;
        this.tipo = tipo;
        this.numeroCandidato = numeroCandidato;
        this.dataHora = dataHora;
    }

    public String getCargo() {
        return cargo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNumeroCandidato() {
        return numeroCandidato;
    }

    public String getDataHora() {
        return dataHora;
    }
}
