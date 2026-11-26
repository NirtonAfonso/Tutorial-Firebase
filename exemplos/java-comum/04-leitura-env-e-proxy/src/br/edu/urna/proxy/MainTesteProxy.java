/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy (Camada Service)
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Demonstra a leitura segura do arquivo .env e o fluxo completo passando pela
 * classe Proxy (ServiceCandidatoProxy) com tratamento de excecoes personalizadas.
 */
package br.edu.urna.proxy;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Programa de console para testar o leitor de .env e a classe ServiceCandidatoProxy.
 */
public class MainTesteProxy {

    public static void main(String[] args) {
        System.out.println("=== 1. Testando leitura de configuracao (.env) ===");
        String arquivoAlvo = new File(".env").exists() ? ".env" : ".env.example";

        try {
            ConfiguracaoEnv configuracao = ConfiguracaoEnv.carregar(arquivoAlvo);
            System.out.println("Arquivo carregado: " + arquivoAlvo);
            System.out.println("Projeto Firebase : " + configuracao.obterObrigatorio("FIREBASE_PROJECT_ID"));
            System.out.println("Caminho da chave : " + configuracao.obterObrigatorio("FIREBASE_CREDENTIALS"));
            System.out.println("Ambiente         : " + configuracao.obterOuPadrao("AMBIENTE_EXECUCAO", "PADRAO"));
        } catch (IOException erroLeitura) {
            System.out.println("Erro ao abrir o arquivo de configuracao: " + erroLeitura.getMessage());
        } catch (IllegalArgumentException erroCampo) {
            System.out.println("Configuracao incompleta: " + erroCampo.getMessage());
        }

        System.out.println();
        System.out.println("=== 2. Testando a camada Proxy (ServiceCandidatoProxy -> UrnaRepository) ===");

        // A tela/main injeta a persistencia (Memoria ou Firebase) no Proxy de servico:
        UrnaRepository persistencia = new MemoriaUrnaProxy();
        ServiceCandidatoProxy servicoProxy = new ServiceCandidatoProxy(persistencia);

        List<String> candidatos = servicoProxy.listarTodosCandidatos();
        System.out.println("Candidatos disponiveis:");
        for (int i = 0; i < candidatos.size(); i++) {
            System.out.println(" - " + candidatos.get(i));
        }

        // Busca valida de partido e candidato
        try {
            System.out.println("Partido 91     : " + servicoProxy.obterInformacoesPartido("91"));
            System.out.println("Candidato 91001: " + servicoProxy.obterInformacoesCandidato("91001"));
            servicoProxy.registrarVoto("91001");
            servicoProxy.registrarVoto("BRANCO");
        } catch (NumeroErradoException | CandidatoInexistenteException erro) {
            System.out.println("[AVISO] " + erro.getMessage());
        }

        // Busca invalida -> Proxy protege a tela lancando excecao clara!
        try {
            System.out.println("Tentando buscar partido 88...");
            servicoProxy.obterInformacoesPartido("88");
        } catch (NumeroErradoException erro) {
            System.out.println("[EXCECAO CAPTURADA] " + erro.getMessage());
        }

        try {
            System.out.println("Tentando buscar candidato 99998...");
            servicoProxy.obterInformacoesCandidato("99998");
        } catch (CandidatoInexistenteException erro) {
            System.out.println("[EXCECAO CAPTURADA] " + erro.getMessage());
        }

        System.out.println("Total de votos gravados: " + servicoProxy.listarTodosVotos().size());
    }
}
