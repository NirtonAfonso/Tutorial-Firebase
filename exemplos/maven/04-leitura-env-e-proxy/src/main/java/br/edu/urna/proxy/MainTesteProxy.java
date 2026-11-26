/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Demonstra a leitura do arquivo .env e o uso polimorfico da interface UrnaRepository.
 */
package br.edu.urna.proxy;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Programa de console para testar o leitor de .env e o padrao Proxy em memoria.
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
        System.out.println("=== 2. Testando o padrao Proxy com Interface ===");

        // Repare que o tipo da variavel e a INTERFACE UrnaRepository (Polimorfismo!)
        UrnaRepository repositorio = new MemoriaUrnaProxy();

        List<String> candidatos = repositorio.listarCandidatos();
        System.out.println("Candidatos carregados:");
        for (int i = 0; i < candidatos.size(); i++) {
            System.out.println(" - " + candidatos.get(i));
        }

        repositorio.salvarVoto("91001");
        repositorio.salvarVoto("BRANCO");
        System.out.println("Total de votos gravados: " + repositorio.listarVotos().size());
    }
}
