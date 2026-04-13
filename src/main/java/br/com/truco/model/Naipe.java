package br.com.truco.model;

/**
 * Representa os naipes do baralho no Truco Gaúcho.
 * A ordem de força dos naipes influencia o desempate.
 */
public enum Naipe {
    OUROS("Ouros", "♦"),
    ESPADAS("Espadas", "♠"),
    COPAS("Copas", "♥"),
    PAUS("Paus", "♣");

    private final String nome;
    private final String simbolo;

    Naipe(String nome, String simbolo) {
        this.nome = nome;
        this.simbolo = simbolo;
    }

    public String getNome() { return nome; }
    public String getSimbolo() { return simbolo; }

    @Override
    public String toString() { return nome; }
}
