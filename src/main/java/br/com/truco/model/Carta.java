package br.com.truco.model;

import java.util.Objects;

/**
 * Representa uma carta do baralho no Truco Gaúcho.
 *
 * <p>No Truco Gaúcho, a hierarquia especial de cartas (manilhas fixas) é:
 * <ol>
 *   <li>4 de Paus  (zap)     - a mais forte</li>
 *   <li>7 de Copas (copas)   - segunda mais forte</li>
 *   <li>As de Espadas (espadilha) - terceira</li>
 *   <li>7 de Ouros (sete)    - quarta manilha</li>
 * </ol>
 *
 * <p>Depois das manilhas, a hierarquia das demais cartas é:
 * 3, 2, A (exceto espadas), K, J, Q, 7 (exceto ouros), 6, 5, 4 (exceto paus)
 */
public class Carta implements Comparable<Carta> {

    private final String valor;
    private final Naipe naipe;
    private final int forca;

    public Carta(String valor, Naipe naipe) {
        this.valor = Objects.requireNonNull(valor, "Valor não pode ser nulo");
        this.naipe = Objects.requireNonNull(naipe, "Naipe não pode ser nulo");
        this.forca = calcularForca(valor, naipe);
    }

    /**
     * Calcula a força da carta no Truco Gaúcho.
     * Retorna um valor inteiro — quanto maior, mais forte.
     */
    private int calcularForca(String valor, Naipe naipe) {
        // Manilhas fixas (as 4 mais fortes)
        if ("4".equals(valor) && Naipe.PAUS == naipe)     return 14; // Zap
        if ("7".equals(valor) && Naipe.COPAS == naipe)    return 13; // Copas
        if ("A".equals(valor) && Naipe.ESPADAS == naipe)  return 12; // Espadilha
        if ("7".equals(valor) && Naipe.OUROS == naipe)    return 11; // Sete de ouros

        // Demais cartas por valor
        return switch (valor) {
            case "3" -> 10;
            case "2" -> 9;
            case "A" -> 8;  // Ases que não são espadilha
            case "K" -> 7;
            case "J" -> 6;
            case "Q" -> 5;
            case "7" -> 4;  // Setes que não são de ouros ou copas
            case "6" -> 3;
            case "5" -> 2;
            case "4" -> 1;  // Quatros que não são de paus
            default  -> 0;
        };
    }

    public boolean isManilha() {
        return forca >= 11;
    }

    public String getValor()  { return valor; }
    public Naipe  getNaipe()  { return naipe; }
    public int    getForca()  { return forca; }

    @Override
    public int compareTo(Carta outra) {
        return Integer.compare(this.forca, outra.forca);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carta carta)) return false;
        return Objects.equals(valor, carta.valor) && naipe == carta.naipe;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor, naipe);
    }

    @Override
    public String toString() {
        return valor + " de " + naipe.getNome() + " [força:" + forca + "]";
    }
}
