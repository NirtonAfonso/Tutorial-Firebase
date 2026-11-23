package br.edu.urna.dominio;

// Número é texto: assim "07" continua com dois dígitos.
public record Candidato(String numero, String nome, String partido, String foto) {
    public Candidato {
        if (numero == null || !numero.matches("[0-9]+")
                || nome == null || nome.isBlank()
                || partido == null || partido.isBlank()
                || foto == null || !foto.matches("[a-zA-Z0-9_-]+\\.png")) {
            throw new IllegalArgumentException("Confira número, nome, partido e arquivo PNG.");
        }
    }

    public String caminhoDaFoto() {
        return "/fotos/" + foto;
    }
}
