/*
 * Exemplo 07 - Projeto Base MVC para o Trabalho Final (AV3 e Extensao)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Camada Controller (MVC): coordena abertura, votacao nos dois cargos,
 * persistencia via UrnaRepository, encerramento e geracao de relatorios.
 */
package br.edu.urna.trabalho.controller;

import br.edu.urna.trabalho.exception.BancoDadosException;
import br.edu.urna.trabalho.exception.EleicaoException;
import br.edu.urna.trabalho.model.Candidato;
import br.edu.urna.trabalho.model.Cargo;
import br.edu.urna.trabalho.model.LinhaRelatorio;
import br.edu.urna.trabalho.model.TipoVoto;
import br.edu.urna.trabalho.model.Voto;
import br.edu.urna.trabalho.proxy.UrnaRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador central da eleicao (Camada de Negocio / Controller do padrao MVC).
 * Gerencia o ciclo de vida: AGUARDANDO_ABERTURA -> EM_VOTACAO -> ENCERRADA.
 */
public class EleicaoController {

    public static final String SENHA_MESARIO = "99999";

    public static final int ESTADO_AGUARDANDO_ABERTURA = 0;
    public static final int ESTADO_EM_VOTACAO = 1;
    public static final int ESTADO_ENCERRADA = 2;

    private UrnaRepository repositorio;
    private List<Cargo> listaCargos;
    private List<Candidato> listaCandidatos;
    private int estadoAtual;
    private int indiceCargoAtual;

    public EleicaoController(UrnaRepository repositorio, List<Cargo> listaCargos) throws BancoDadosException {
        this.repositorio = repositorio;
        this.listaCargos = listaCargos;
        this.listaCandidatos = repositorio.listarCandidatos();
        this.estadoAtual = ESTADO_AGUARDANDO_ABERTURA;
        this.indiceCargoAtual = 0;
    }

    public int getEstadoAtual() {
        return estadoAtual;
    }

    public List<Cargo> getListaCargos() {
        return listaCargos;
    }

    public Cargo getCargoAtual() {
        return listaCargos.get(indiceCargoAtual);
    }

    public boolean isEmVotacao() {
        return estadoAtual == ESTADO_EM_VOTACAO;
    }

    /**
     * Valida a senha do mesario (99999) e libera a votacao.
     */
    public void abrirEleicao(String senhaInformada) throws EleicaoException {
        if (estadoAtual != ESTADO_AGUARDANDO_ABERTURA) {
            throw new EleicaoException("A eleicao ja foi aberta anteriormente.");
        }
        if (!SENHA_MESARIO.equals(senhaInformada)) {
            throw new EleicaoException("Senha invalida! Digite 99999 para iniciar a votacao.");
        }
        this.estadoAtual = ESTADO_EM_VOTACAO;
        this.indiceCargoAtual = 0;
    }

    /**
     * Valida a senha do mesario (99999) e encerra a votacao definitivamente.
     */
    public void encerrarEleicao(String senhaInformada) throws EleicaoException {
        if (estadoAtual != ESTADO_EM_VOTACAO) {
            throw new EleicaoException("A eleicao nao esta em andamento.");
        }
        if (!SENHA_MESARIO.equals(senhaInformada)) {
            throw new EleicaoException("Senha invalida! Digite 99999 para encerrar a votacao.");
        }
        this.estadoAtual = ESTADO_ENCERRADA;
    }

    /**
     * Busca um candidato pelo cargo atual e pelo numero digitado.
     */
    public Candidato buscarCandidatoNoCargoAtual(String numeroDigitado) {
        String nomeCargo = getCargoAtual().getNome();
        for (int i = 0; i < listaCandidatos.size(); i++) {
            Candidato candidato = listaCandidatos.get(i);
            if (candidato.getNomeCargo().equalsIgnoreCase(nomeCargo)
                    && candidato.getNumero().equals(numeroDigitado)) {
                return candidato;
            }
        }
        return null;
    }

    /**
     * Registra o voto para o cargo atual e avanca para o proximo cargo (ou próximo eleitor).
     *
     * @param numeroDigitado Numero digitado pelo eleitor.
     * @param votoEmBranco   True se o eleitor pressionou BRANCO.
     * @return True se o eleitor concluiu todos os cargos (fim da votacao deste eleitor).
     */
    public boolean confirmarVotoAtual(String numeroDigitado, boolean votoEmBranco)
            throws EleicaoException, BancoDadosException {

        if (!isEmVotacao()) {
            throw new EleicaoException("A urna nao esta aberta para votacao.");
        }

        Cargo cargo = getCargoAtual();
        TipoVoto tipo;
        String numeroParaGravar;

        if (votoEmBranco) {
            tipo = TipoVoto.BRANCO;
            numeroParaGravar = "";
        } else {
            if (numeroDigitado == null || numeroDigitado.length() != cargo.getQuantidadeDigitos()) {
                throw new EleicaoException("Digite todos os " + cargo.getQuantidadeDigitos() + " digitos antes de confirmar.");
            }
            Candidato candidato = buscarCandidatoNoCargoAtual(numeroDigitado);
            tipo = (candidato != null) ? TipoVoto.NOMINAL : TipoVoto.NULO;
            numeroParaGravar = numeroDigitado;
        }

        Voto voto = new Voto(cargo.getNome(), tipo, numeroParaGravar, LocalDateTime.now().toString());
        repositorio.salvarVoto(voto);

        // Avanca para o segundo cargo ou volta para o primeiro cargo para o proximo eleitor
        indiceCargoAtual++;
        if (indiceCargoAtual >= listaCargos.size()) {
            indiceCargoAtual = 0;
            return true; // Eleitor terminou de votar nos dois cargos
        }
        return false; // Ainda falta votar no proximo cargo
    }

    /**
     * Calcula o relatorio completo (Zeresima ou Apuracao Final) separado por cargo.
     * Sempre lista todos os candidatos cadastrados (mesmo com 0 votos), alem de Brancos e Nulos.
     */
    public Map<String, List<LinhaRelatorio>> gerarRelatorioPorCargo() throws BancoDadosException {
        List<Voto> todosVotos = repositorio.listarVotos();
        Map<String, List<LinhaRelatorio>> relatorio = new LinkedHashMap<String, List<LinhaRelatorio>>();

        for (int i = 0; i < listaCargos.size(); i++) {
            Cargo cargo = listaCargos.get(i);
            String nomeCargo = cargo.getNome();

            // 1. Filtra os votos deste cargo
            int totalVotosCargo = 0;
            int votosBrancos = 0;
            int votosNulos = 0;
            Map<String, Integer> contagemPorNumero = new LinkedHashMap<String, Integer>();

            // Inicializa todos os candidatos do cargo com 0 votos (garante a Zeresima!)
            List<Candidato> candidatosDoCargo = new ArrayList<Candidato>();
            for (int j = 0; j < listaCandidatos.size(); j++) {
                Candidato candidato = listaCandidatos.get(j);
                if (candidato.getNomeCargo().equalsIgnoreCase(nomeCargo)) {
                    candidatosDoCargo.add(candidato);
                    contagemPorNumero.put(candidato.getNumero(), 0);
                }
            }

            // 2. Contabiliza cada voto gravado para este cargo
            for (int j = 0; j < todosVotos.size(); j++) {
                Voto voto = todosVotos.get(j);
                if (!voto.getNomeCargo().equalsIgnoreCase(nomeCargo)) {
                    continue;
                }
                totalVotosCargo++;
                if (voto.getTipoVoto() == TipoVoto.BRANCO) {
                    votosBrancos++;
                } else if (voto.getTipoVoto() == TipoVoto.NULO) {
                    votosNulos++;
                } else if (contagemPorNumero.containsKey(voto.getNumeroDigitado())) {
                    int atual = contagemPorNumero.get(voto.getNumeroDigitado());
                    contagemPorNumero.put(voto.getNumeroDigitado(), atual + 1);
                } else {
                    votosNulos++;
                }
            }

            // 3. Monta as linhas com percentuais seguros contra divisao por zero
            List<LinhaRelatorio> linhas = new ArrayList<LinhaRelatorio>();
            for (int j = 0; j < candidatosDoCargo.size(); j++) {
                Candidato candidato = candidatosDoCargo.get(j);
                int quantidade = contagemPorNumero.get(candidato.getNumero());
                double percentual = calcularPercentual(quantidade, totalVotosCargo);
                linhas.add(new LinhaRelatorio(candidato.getNome(), candidato.getNumero(), quantidade, percentual));
            }

            linhas.add(new LinhaRelatorio("Votos Brancos", "", votosBrancos, calcularPercentual(votosBrancos, totalVotosCargo)));
            linhas.add(new LinhaRelatorio("Votos Nulos", "", votosNulos, calcularPercentual(votosNulos, totalVotosCargo)));

            relatorio.put(nomeCargo, linhas);
        }

        return relatorio;
    }

    private double calcularPercentual(int votosLinha, int totalCargo) {
        if (totalCargo == 0) {
            return 0.0;
        }
        return (votosLinha * 100.0) / totalCargo;
    }
}
