/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy (Camada Service)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Interface da camada de persistencia (contrato de comunicacao com o banco).
 */
package br.edu.urna.proxy;

import java.util.List;

/**
 * Define as operacoes basicas que qualquer classe de persistencia da urna
 * (Firebase Admin SDK, Firebase REST ou Memoria) precisa implementar.
 */
public interface UrnaRepository {

    /**
     * Lista todos os candidatos cadastrados.
     */
    List<String> listarCandidatos();

    /**
     * Busca as informacoes de um candidato pelo numero completo.
     * Retorna null se nao existir.
     */
    String buscarCandidatoPorNumero(String numero);

    /**
     * Busca o nome do partido pelos dois primeiros digitos.
     * Retorna null se o partido nao existir.
     */
    String buscarPartidoPorNumero(String numeroPartido);

    /**
     * Registra um voto na fonte de dados.
     */
    void salvarVoto(String numeroDigitado);

    /**
     * Lista todos os votos registrados ate o momento.
     */
    List<String> listarVotos();
}
