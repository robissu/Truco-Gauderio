package br.com.truco.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa um jogador de Truco Gaúcho.
 */
public class Jogador {

    private final String nome;
    private final Equipe equipe;
    private final List<Carta> mao;

    public Jogador(String nome, Equipe equipe) {
        this.nome   = Objects.requireNonNull(nome,   "Nome não pode ser nulo");
        this.equipe = Objects.requireNonNull(equipe, "Equipe não pode ser nula");
        this.mao    = new ArrayList<>();
    }

    /** Recebe cartas do distribuidor. */
    public void receberCartas(List<Carta> cartas) {
        this.mao.clear();
        this.mao.addAll(cartas);
    }

    /**
     * Joga uma carta da mão.
     * @param index índice da carta na mão (0-based)
     * @return a carta jogada
     * @throws IllegalArgumentException se índice inválido
     * @throws IllegalStateException    se mão estiver vazia
     */
    public Carta jogarCarta(int index) {
        if (mao.isEmpty()) {
            throw new IllegalStateException("Jogador " + nome + " não tem cartas na mão");
        }
        if (index < 0 || index >= mao.size()) {
            throw new IllegalArgumentException(
                "Índice inválido: " + index + ". Mão tem " + mao.size() + " carta(s)"
            );
        }
        return mao.remove(index);
    }

    /** Escolhe a carta de maior força na mão (IA simples). */
    public Carta jogarMelhorCarta() {
        if (mao.isEmpty()) {
            throw new IllegalStateException("Jogador " + nome + " não tem cartas na mão");
        }
        Carta melhor = Collections.max(mao);
        mao.remove(melhor);
        return melhor;
    }

    /** Escolhe a carta de menor força na mão (IA conservadora). */
    public Carta jogarPiorCarta() {
        if (mao.isEmpty()) {
            throw new IllegalStateException("Jogador " + nome + " não tem cartas na mão");
        }
        Carta pior = Collections.min(mao);
        mao.remove(pior);
        return pior;
    }

    public String     getNome()   { return nome; }
    public Equipe     getEquipe() { return equipe; }
    public List<Carta> getMao()   { return Collections.unmodifiableList(mao); }
    public int        qtdCartas() { return mao.size(); }
    public boolean    temCartas() { return !mao.isEmpty(); }

    @Override
    public String toString() {
        return "Jogador{nome='" + nome + "', equipe=" + equipe + ", cartas=" + mao.size() + "}";
    }
}
