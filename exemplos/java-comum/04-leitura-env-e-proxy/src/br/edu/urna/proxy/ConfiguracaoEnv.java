/*
 * Exemplo 04 - Leitura de arquivo .env e Padrao Proxy
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Responsavel por carregar configuracoes sensiveis de um arquivo .env externo
 * sem expor credenciais no codigo-fonte Java.
 */
package br.edu.urna.proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Le um arquivo no formato CHAVE=VALOR (como .env) de forma segura no Windows, Linux e macOS.
 * Diferente de java.util.Properties, nao interpreta barra invertida (\) como escape,
 * evitando erros ao informar caminhos de arquivos no Windows.
 */
public class ConfiguracaoEnv {

    private Map<String, String> valores;

    /**
     * Construtor privado: use o metodo estatico carregar(caminho) para instanciar.
     */
    private ConfiguracaoEnv(Map<String, String> valores) {
        this.valores = valores;
    }

    /**
     * Carrega as variaveis definidas no arquivo informado.
     *
     * @param caminhoArquivo Caminho do arquivo (ex: ".env" ou ".env.example").
     * @return Instancia preenchida de ConfiguracaoEnv.
     * @throws IOException Caso o arquivo nao exista ou nao possa ser lido.
     */
    public static ConfiguracaoEnv carregar(String caminhoArquivo) throws IOException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            throw new IOException("Arquivo de configuracao nao encontrado: " + arquivo.getAbsolutePath());
        }

        Map<String, String> mapa = new HashMap<String, String>();

        // try-with-resources fecha automaticamente o BufferedReader ao final do bloco
        try (BufferedReader leitor = new BufferedReader(
                new InputStreamReader(new FileInputStream(arquivo), StandardCharsets.UTF_8))) {

            String linha;
            while ((linha = leitor.readLine()) != null) {
                String linhaLimpa = linha.trim();

                // Ignora linhas vazias e comentarios iniciados com #
                if (linhaLimpa.isEmpty() || linhaLimpa.startsWith("#")) {
                    continue;
                }

                int indiceIgual = linhaLimpa.indexOf('=');
                if (indiceIgual <= 0) {
                    continue;
                }

                String chave = linhaLimpa.substring(0, indiceIgual).trim();
                String valor = linhaLimpa.substring(indiceIgual + 1).trim();

                // Remove aspas simples ou duplas nas extremidades, se o aluno tiver colocado
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

    /**
     * Retorna o valor obrigatorio de uma chave do arquivo .env.
     *
     * @param chave Nome da variavel (ex: FIREBASE_PROJECT_ID).
     * @return Valor configurado.
     * @throws IllegalArgumentException Caso a chave esteja ausente ou vazia.
     */
    public String obterObrigatorio(String chave) {
        String valor = valores.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("A variavel obrigatoria '" + chave + "' nao foi preenchida no arquivo .env.");
        }
        return valor;
    }

    /**
     * Retorna o valor de uma chave ou um valor padrao caso nao esteja definida.
     */
    public String obterOuPadrao(String chave, String valorPadrao) {
        String valor = valores.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            return valorPadrao;
        }
        return valor;
    }
}
