package br.edu.urna.dominio;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

// Cuida da digitação e do voto escolhido. Pode ser usado em qualquer tela.
public final class PainelVotacao {
    private final int quantidadeDigitos;
    private final Map<String, Candidato> candidatos = new HashMap<>();
    private String numero = "";
    private boolean branco;
    private Voto votoPendente;

    public PainelVotacao(int quantidadeDigitos, Collection<Candidato> cadastrados) {
        if (quantidadeDigitos < 1) {
            throw new IllegalArgumentException("O cargo precisa ter pelo menos um dígito.");
        }
        this.quantidadeDigitos = quantidadeDigitos;
        for (Candidato candidato : cadastrados) {
            if (candidato.numero().length() != quantidadeDigitos
                    || candidatos.putIfAbsent(candidato.numero(), candidato) != null) {
                throw new IllegalArgumentException("Número de candidato repetido ou fora do tamanho do cargo.");
            }
        }
    }

    public void digitar(int digito) {
        if (digito < 0 || digito > 9) {
            throw new IllegalArgumentException("Digite um número de 0 a 9.");
        }
        if (!branco && !completo() && votoPendente == null) {
            numero += digito;
        }
    }

    public void votarEmBranco() {
        if (numero.isEmpty() && votoPendente == null) {
            branco = true;
        }
    }

    public void corrigir() {
        // Se o banco recebeu o voto e a resposta se perdeu, a operação já pode existir.
        if (votoPendente == null) {
            limpar();
        }
    }

    public String numero() { return numero; }

    public boolean completo() { return numero.length() == quantidadeDigitos; }

    public boolean branco() { return branco; }

    public boolean podeConfirmar() { return branco || completo(); }

    public boolean temEnvioPendente() { return votoPendente != null; }

    public Optional<Candidato> candidato() {
        return completo() ? Optional.ofNullable(candidatos.get(numero)) : Optional.empty();
    }

    public Voto prepararConfirmacao() {
        if (!podeConfirmar()) {
            throw new IllegalStateException("Complete o número ou escolha BRANCO.");
        }
        if (votoPendente == null) {
            Voto.Tipo tipo;
            if (branco) {
                tipo = Voto.Tipo.BRANCO;
            } else if (candidato().isPresent()) {
                tipo = Voto.Tipo.NOMINAL;
            } else {
                tipo = Voto.Tipo.NULO;
            }
            votoPendente = new Voto(UUID.randomUUID(), tipo, numero);
        }
        return votoPendente;
    }

    public void votoRegistrado() {
        votoPendente = null;
        limpar();
    }

    private void limpar() {
        numero = "";
        branco = false;
    }
}
