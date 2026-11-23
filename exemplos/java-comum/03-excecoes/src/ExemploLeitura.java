import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class ExemploLeitura {
    // Esta camada conhece o arquivo. Quem chamou decide o que dizer ao usuário.
    static String lerNumero(Path arquivo) throws IOException {
        try (BufferedReader leitor = Files.newBufferedReader(arquivo)) {
            String numero = leitor.readLine();
            if (numero == null || !numero.matches("[0-9]{2}")) {
                throw new IllegalArgumentException("Use dois dígitos na primeira linha, por exemplo 07.");
            }
            return numero;
        }
    }

    public static void main(String[] args) {
        Path arquivo = Path.of(args.length == 0 ? "candidato.txt" : args[0]);
        try {
            System.out.println("Número lido: " + lerNumero(arquivo));
        } catch (NoSuchFileException erro) {
            System.out.println("Arquivo ausente: " + arquivo);
        } catch (IOException erro) {
            System.out.println("Falha na leitura: " + erro.getMessage());
        } catch (IllegalArgumentException erro) {
            System.out.println(erro.getMessage());
        }
        System.out.println("Fim da leitura.");
    }
}
