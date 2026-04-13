package br.com.truco.model;

/**
 * Representa uma das duas equipes na partida de Truco.
 */
public enum Equipe {
    TIME_A("Time A"),
    TIME_B("Time B");

    private final String nome;

    Equipe(String nome) {
        this.nome = nome;
    }

    public String getNome() { return nome; }

    /** Retorna a equipe adversária. */
    public Equipe adversaria() {
        return this == TIME_A ? TIME_B : TIME_A;
    }

    @Override
    public String toString() { return nome; }
}
