package br.edu.urna.dados;

public final class PersistenciaException extends Exception {
    public PersistenciaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
