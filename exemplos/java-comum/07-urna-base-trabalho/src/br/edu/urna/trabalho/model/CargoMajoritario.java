/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.trabalho.model;

/**
 * Especializacao de Cargo para eleicoes majoritarias (ex: Prefeito ou Presidente com 2 digitos).
 */
public class CargoMajoritario extends Cargo {

    public CargoMajoritario(String nome, int quantidadeDigitos) {
        super(nome, quantidadeDigitos);
    }

    @Override
    public String getTipoSistemaEleitoral() {
        return "Majoritario";
    }
}
