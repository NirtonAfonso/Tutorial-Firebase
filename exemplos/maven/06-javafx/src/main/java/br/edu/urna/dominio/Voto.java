package br.edu.urna.dominio;

import java.util.Objects;
import java.util.UUID;

// O ID é criado uma vez e mantido se for preciso repetir o envio.
public record Voto(UUID id, Tipo tipo, String numero) {
    public Voto {
        Objects.requireNonNull(id);
        Objects.requireNonNull(tipo);
        if (tipo == Tipo.BRANCO) {
            numero = "";
        } else if (numero == null || !numero.matches("[0-9]+")) {
            throw new IllegalArgumentException("Voto nominal ou nulo precisa de um número.");
        }
    }

    public enum Tipo {
        NOMINAL, BRANCO, NULO
    }
}
