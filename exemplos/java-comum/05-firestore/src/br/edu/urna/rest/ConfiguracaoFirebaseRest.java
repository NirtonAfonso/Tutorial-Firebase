package br.edu.urna.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

record ConfiguracaoFirebaseRest(String projeto, String chaveApi, String email, String senha) {
    static ConfiguracaoFirebaseRest carregar(Path caminho) throws IOException {
        Map<String, String> valores = new LinkedHashMap<>();
        try (var leitor = Files.newBufferedReader(caminho)) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.isBlank() || linha.stripLeading().startsWith("#")) {
                    continue;
                }
                // Separar só no primeiro "=" permite usar esse caractere na senha.
                int separador = linha.indexOf('=');
                if (separador < 1) {
                    throw new IllegalArgumentException("Use uma linha CHAVE=valor por configuração no .env.");
                }
                String chave = linha.substring(0, separador).strip();
                String valor = linha.substring(separador + 1).strip();
                if (valores.putIfAbsent(chave, valor) != null) {
                    throw new IllegalArgumentException("Há uma configuração repetida no .env.");
                }
            }
        }

        String projeto = obrigatorio(valores, "FIREBASE_PROJECT_ID");
        if (!projeto.matches("[a-z][a-z0-9-]{4,28}[a-z0-9]")) {
            throw new IllegalArgumentException("Informe o ID do projeto Firebase, como urna-poo-grupo-03.");
        }
        return new ConfiguracaoFirebaseRest(
                projeto,
                obrigatorio(valores, "FIREBASE_API_KEY"),
                obrigatorio(valores, "FIREBASE_EMAIL"),
                obrigatorio(valores, "FIREBASE_PASSWORD"));
    }

    private static String obrigatorio(Map<String, String> valores, String chave) {
        String valor = valores.get(chave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Preencha " + chave + " no .env.");
        }
        return valor;
    }

    @Override
    public String toString() {
        return "Configuração Firebase REST [projeto=" + projeto + "]";
    }
}
