/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy (Camada Service)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Classe Proxy inspirada no padrao ServiceCandidato do projeto urna-eletronica:
 * atua como intermediaria entre a tela grafica (Presentation/View) e o banco
 * de dados (Persistence), centralizando validacoes e lancando excecoes claras.
 */
package br.edu.urna.proxy;

import java.util.List;

/**
 * Classe Proxy da camada de servico que intermedia toda comunicacao entre
 * a interface grafica e a camada de persistencia (UrnaRepository).
 *
 * Boas praticas demonstradas:
 * 1. A tela nunca fala diretamente com o banco (baixo acoplamento).
 * 2. Validacoes de negocio e lancamento de excecoes ficam centralizados aqui.
 * 3. Cada busca e feita UMA unica vez no banco (guardando o retorno em variavel),
 *    evitando consultas duplicadas e economizando leituras no Firebase!
 */
public class ServiceCandidatoProxy {

    private UrnaRepository persistencia;

    /**
     * Recebe qualquer implementacao de UrnaRepository (Polimorfismo: Memoria ou Firebase).
     *
     * @param persistencia Implementacao da camada de persistencia.
     */
    public ServiceCandidatoProxy(UrnaRepository persistencia) {
        this.persistencia = persistencia;
    }

    /**
     * Consulta um candidato pelo numero completo.
     * Se nao existir no banco, lanca CandidatoInexistenteException em vez de devolver null.
     *
     * @param numero Numero completo digitado na urna (ex: "91001").
     * @return Descricao/dados do candidato encontrado.
     * @throws CandidatoInexistenteException Se o numero nao corresponder a nenhum candidato.
     */
    public String obterInformacoesCandidato(String numero) throws CandidatoInexistenteException {
        // Boa pratica: faz a consulta UMA unica vez e guarda o resultado em variavel
        String candidatoEncontrado = persistencia.buscarCandidatoPorNumero(numero);
        if (candidatoEncontrado == null) {
            throw new CandidatoInexistenteException("CANDIDATO INEXISTENTE para o numero: " + numero);
        }
        return candidatoEncontrado;
    }

    /**
     * Consulta o partido pelos dois primeiros digitos.
     * Se o numero do partido nao existir, lanca NumeroErradoException.
     *
     * @param numeroPartido Dois primeiros digitos digitados (ex: "91").
     * @return Nome do partido encontrado.
     * @throws NumeroErradoException Se o partido nao estiver cadastrado.
     */
    public String obterInformacoesPartido(String numeroPartido) throws NumeroErradoException {
        String partidoEncontrado = persistencia.buscarPartidoPorNumero(numeroPartido);
        if (partidoEncontrado == null) {
            throw new NumeroErradoException("NUMERO ERRADO: nenhum partido cadastrado com " + numeroPartido);
        }
        return partidoEncontrado;
    }

    /**
     * Valida e encaminha o voto para gravacao na persistencia.
     *
     * @param numeroDigitado Numero do candidato, "BRANCO" ou "NULO".
     */
    public void registrarVoto(String numeroDigitado) throws CandidatoInexistenteException {
        if ("BRANCO".equalsIgnoreCase(numeroDigitado) || "NULO".equalsIgnoreCase(numeroDigitado)) {
            persistencia.salvarVoto(numeroDigitado.toUpperCase());
            return;
        }

        String candidato = persistencia.buscarCandidatoPorNumero(numeroDigitado);
        if (candidato == null) {
            throw new CandidatoInexistenteException(
                    "Nao e possivel registrar voto nominal para numero inexistente: " + numeroDigitado
            );
        }
        persistencia.salvarVoto(numeroDigitado);
    }

    public List<String> listarTodosCandidatos() {
        return persistencia.listarCandidatos();
    }

    public List<String> listarTodosVotos() {
        return persistencia.listarVotos();
    }
}
