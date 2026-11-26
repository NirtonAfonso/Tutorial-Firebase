/*
 * Exemplo 02 - Urna Swing com Exibicao Automatica de Foto
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Classe de modelo (Model) que representa um candidato na urna.
 */
package br.edu.urna.swingfotos.model;

/**
 * Representa um candidato disponivel para votacao.
 * Aplica encapsulamento: todos os atributos sao private com getters publicos.
 */
public class Candidato {

    private String numero;
    private String nome;
    private String partido;
    private String arquivoFoto;

    /**
     * Cria um novo candidato com todos os dados obrigatorios.
     *
     * @param numero      Numero do candidato como texto (preserva zeros a esquerda).
     * @param nome        Nome de exibicao na urna.
     * @param partido     Sigla do partido ficticio.
     * @param arquivoFoto Nome do arquivo PNG dentro da pasta /fotos (ex: golfe.png).
     */
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

    public String getArquivoFoto() {
        return arquivoFoto;
    }

    /**
     * Retorna o caminho completo do recurso no classpath da aplicacao.
     *
     * @return Caminho no formato /fotos/nome-do-arquivo.png.
     */
    public String getCaminhoFoto() {
        return "/fotos/" + arquivoFoto;
    }
}
