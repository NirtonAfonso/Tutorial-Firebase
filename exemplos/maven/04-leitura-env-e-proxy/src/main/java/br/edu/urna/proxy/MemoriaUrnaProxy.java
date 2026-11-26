/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy (Camada Service)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Implementacao em memoria da camada de persistencia (UrnaRepository).
 */
package br.edu.urna.proxy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Persistencia em memoria para testes locais sem precisar de internet.
 * Cumpre o mesmo contrato (UrnaRepository) que a persistencia real do Cloud Firestore.
 */
public class MemoriaUrnaProxy implements UrnaRepository {

    private Map<String, String> candidatosPorNumero;
    private Map<String, String> partidosPorNumero;
    private List<String> votos;

    public MemoriaUrnaProxy() {
        this.candidatosPorNumero = new LinkedHashMap<String, String>();
        this.candidatosPorNumero.put("91001", "91001 - Golfe (PEsp)");
        this.candidatosPorNumero.put("91002", "91002 - Beisebol (PEsp)");
        this.candidatosPorNumero.put("92001", "92001 - Rock (PMus)");
        this.candidatosPorNumero.put("93001", "93001 - Astronauta (PProf)");

        this.partidosPorNumero = new LinkedHashMap<String, String>();
        this.partidosPorNumero.put("91", "PEsp - Partido dos Esportes");
        this.partidosPorNumero.put("92", "PMus - Partido dos Ritmos Musicais");
        this.partidosPorNumero.put("93", "PProf - Partido das Profissoes");

        this.votos = new ArrayList<String>();
    }

    @Override
    public List<String> listarCandidatos() {
        return new ArrayList<String>(candidatosPorNumero.values());
    }

    @Override
    public String buscarCandidatoPorNumero(String numero) {
        return candidatosPorNumero.get(numero);
    }

    @Override
    public String buscarPartidoPorNumero(String numeroPartido) {
        return partidosPorNumero.get(numeroPartido);
    }

    @Override
    public void salvarVoto(String numeroDigitado) {
        System.out.println("[Persistencia] Salvando voto no repositorio: " + numeroDigitado);
        votos.add(numeroDigitado);
    }

    @Override
    public List<String> listarVotos() {
        return new ArrayList<String>(votos);
    }
}
