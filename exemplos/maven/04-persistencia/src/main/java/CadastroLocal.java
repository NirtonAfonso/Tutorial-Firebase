import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class CadastroLocal {
    public static void main(String[] args) throws IOException {
        // O mapa nasce vazio em cada execução. Cada chave aponta para um nome.
        Map<String, String> memoria = new HashMap<>();
        Path arquivo = Path.of("cadastro-de-treino.txt");
        if (args.length == 1 && args[0].equals("gravar")) {
            memoria.put("07", "Júlia Horizonte");
            Files.writeString(arquivo, memoria.get("07"));
        }
        System.out.println("Na memória: " + memoria.getOrDefault("07", "(vazio)"));
        if (Files.exists(arquivo)) {
            System.out.println("No arquivo: " + Files.readString(arquivo));
        } else {
            System.out.println("No arquivo: ainda não há cadastro.");
        }
    }
}
