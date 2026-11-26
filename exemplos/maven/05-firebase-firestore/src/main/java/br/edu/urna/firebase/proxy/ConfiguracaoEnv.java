/*
 * Exemplo 05 - Conexao com Firebase Cloud Firestore (Java Comum / API REST)
 * Disciplina: Programacao Orientada a Objetos (POO)
 */
package br.edu.urna.firebase.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Carrega configuracoes do arquivo .env sem expor credenciais no codigo Java.
 */
public class ConfiguracaoEnv {

    private Map<String, String> valores;

    private ConfiguracaoEnv(Map<String, String> valores) {
        this.valores = valores;
    }

    public static ConfiguracaoEnv carregar(String caminhoArquivo) throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            throw new IOException("Arquivo .env nao encontrado em: " + arquivo.getAbsolutePath());
        }

        Map<String, String> mapa = new HashMap<String, String>();
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                String linhaLimpa = linha.trim();
                if (linhaLimpa.isEmpty() || linhaLimpa.startsWith("#")) {
                    continue;
                }
                int indiceIgual = linhaLimpa.indexOf('=');
                if (indiceIgual <= 0) {
                    continue;
                }
                String chave = linhaLimpa.substring(0, indiceIgual).trim();
                String valor = linhaLimpa.substring(indiceIgual + 1).trim();
                if ((valor.startsWith("\"") && valor.endsWith("\""))
                        || (valor.startsWith("'") && valor.endsWith("'"))) {
                    if (valor.length() >= 2) {
                        valor = valor.substring(1, valor.length() - 1);
                    }
                }
                mapa.put(chave, valor);
            }
        }
        return new ConfiguracaoEnv(mapa);
    }

    public String obterObrigatorio(String chave) {
        String valor = valores.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Preencha a variavel '" + chave + "' no arquivo .env.");
        }
        return valor;
    }
}
