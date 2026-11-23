import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Apuracao {
    // O parâmetro representa as escolhas já confirmadas para UM cargo.
    static void imprimir(String titulo, List<String> escolhas) {
        Map<String, Integer> linhas = new LinkedHashMap<>();
        for (String opcao : List.of("07", "12", "BRANCO", "NULO")) {
            linhas.put(opcao, 0);
        }
        for (String escolha : escolhas) {
            linhas.merge(escolha, 1, Integer::sum);
        }
        System.out.println(titulo + " | Total: " + escolhas.size());
        for (var linha : linhas.entrySet()) {
            double percentual = escolhas.isEmpty() ? 0 : 100.0 * linha.getValue() / escolhas.size();
            System.out.printf(Locale.forLanguageTag("pt-BR"), "%s | %d | %.1f%%%n",
                    linha.getKey(), linha.getValue(), percentual);
        }
    }

    public static void main(String[] args) {
        imprimir("Antes da abertura", List.of());
        imprimir("Depois de quatro confirmações", List.of("07", "07", "BRANCO", "NULO"));
    }
}
