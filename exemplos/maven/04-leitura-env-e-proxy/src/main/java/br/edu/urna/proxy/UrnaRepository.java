/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Interface que define o contrato de acesso aos dados da urna (Polimorfismo e Abstracao).
 */
package br.edu.urna.proxy;

import java.util.List;

/**
 * Define as operacoes que qualquer fonte de dados da urna precisa fornecer.
 * A tela (View) e o controlador (Controller) dependem desta interface, e nao
 * diretamente do Firebase ou de listas em memoria.
 */
public interface UrnaRepository {

    /**
     * Lista os candidatos cadastrados.
     *
     * @return Lista de nomes/numeros dos candidatos em texto simples neste exemplo.
     */
    List<String> listarCandidatos();

    /**
     * Registra um voto na fonte de dados.
     *
     * @param numeroDigitado Numero votado (ou "BRANCO" / "NULO").
     */
    void salvarVoto(String numeroDigitado);

    /**
     * Lista todos os votos registrados ate o momento.
     *
     * @return Lista de votos gravados.
     */
    List<String> listarVotos();
}
