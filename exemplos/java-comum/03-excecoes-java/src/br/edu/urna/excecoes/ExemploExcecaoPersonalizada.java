/*
 * Exemplo 03 - Tratamento de Excecoes em Java
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Demonstra throw, throws, propagacao na pilha de chamadas e excecoes personalizadas.
 */
package br.edu.urna.excecoes;

/**
 * Simula validacao de voto e gravacao em banco usando excecoes personalizadas.
 */
public class ExemploExcecaoPersonalizada {

    /**
     * Valida o numero digitado e simula o envio para o banco de dados.
     *
     * @param numeroDigitado     Numero digitado pelo eleitor.
     * @param servidorDisponivel Simula se o Firebase esta respondendo (true/false).
     * @throws VotoInvalidoException Se o numero estiver vazio ou nao tiver 5 digitos.
     * @throws BancoDadosException   Se a simulacao indicar falha de rede.
     */
    public static void registrarVoto(String numeroDigitado, boolean servidorDisponivel)
            throws VotoInvalidoException, BancoDadosException {

        if (numeroDigitado == null || numeroDigitado.isEmpty()) {
            throw new VotoInvalidoException("Nenhum numero foi digitado.");
        }

        if (!numeroDigitado.matches("[0-9]{5}")) {
            throw new VotoInvalidoException(
                    "O numero '" + numeroDigitado + "' deve conter exatamente 5 digitos numericos."
            );
        }

        if (!servidorDisponivel) {
            throw new BancoDadosException("Nao foi possivel conectar ao Firebase. Verifique a internet.");
        }

        System.out.println("[SUCESSO] Voto registrado para o numero: " + numeroDigitado);
    }

    public static void main(String[] args) {
        // Teste 1: Voto valido com servidor online
        executarTeste("91001", true);

        // Teste 2: Voto com poucos digitos -> VotoInvalidoException
        executarTeste("91", true);

        // Teste 3: Voto valido, mas servidor fora do ar -> BancoDadosException
        executarTeste("91001", false);
    }

    /**
     * Metodo auxiliar que demonstra o tratamento das duas excecoes personalizadas.
     */
    private static void executarTeste(String numero, boolean online) {
        System.out.println("--------------------------------------------");
        try {
            registrarVoto(numero, online);
        } catch (VotoInvalidoException erroVoto) {
            System.out.println("[AVISO AO ELEITOR] " + erroVoto.getMessage());
        } catch (BancoDadosException erroBanco) {
            System.out.println("[FALHA DE BANCO] " + erroBanco.getMessage());
            System.out.println("Acao da urna: manter voto na tela e permitir tentar novamente.");
        }
    }
}
