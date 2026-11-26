/*
 * Exemplo 03 - Tratamento de Excecoes em Java
 * Disciplina: Programacao Orientada a Objetos (POO)
 *
 * Demonstra try, multiplos blocos catch (do especifico para o generico) e finally.
 */
package br.edu.urna.excecoes;

/**
 * Mostra como capturar excecoes comuns sem derrubar o programa.
 */
public class ExemploTryCatch {

    /**
     * Tenta converter um texto em numero inteiro e calcular um percentual.
     *
     * @param textoVotos Texto representando a quantidade de votos.
     * @param totalVotos Total geral de votos do cargo.
     */
    public static void calcularPercentualSeguro(String textoVotos, int totalVotos) {
        System.out.println("--------------------------------------------");
        System.out.println("Testando entrada: votos=\"" + textoVotos + "\", total=" + totalVotos);

        try {
            int votos = Integer.parseInt(textoVotos);
            int percentualInteiro = (votos * 100) / totalVotos;
            System.out.println("Resultado calculado: " + percentualInteiro + "%");
        } catch (NumberFormatException erroFormato) {
            System.out.println("[ERRO DE FORMATO] O texto '" + textoVotos + "' nao e um numero valido.");
            System.out.println("Detalhe tecnico: " + erroFormato.getMessage());
        } catch (ArithmeticException erroMatematico) {
            System.out.println("[ERRO MATEMATICO] Nao e possivel dividir por zero! (totalVotos = 0)");
            System.out.println("Detalhe tecnico: " + erroMatematico.getMessage());
        } catch (Exception erroGenerico) {
            // O catch mais generico SEMPRE deve ficar por ultimo!
            System.out.println("[ERRO INESPERADO] " + erroGenerico.getMessage());
        } finally {
            // O bloco finally SEMPRE executa, ocorrendo erro ou nao.
            System.out.println("Bloco finally executado: finalizando operacao de calculo.");
        }
    }

    public static void main(String[] args) {
        // Caso 1: Tudo certo
        calcularPercentualSeguro("50", 200);

        // Caso 2: Texto invalido -> dispara NumberFormatException
        calcularPercentualSeguro("abc", 200);

        // Caso 3: Divisao por zero -> dispara ArithmeticException
        calcularPercentualSeguro("10", 0);

        System.out.println("--------------------------------------------");
        System.out.println("O programa chegou ao fim normalmente sem travar!");
    }
}
