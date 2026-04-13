package br.com.truco.model;

/**
 * Resultado de uma rodada (vaza) dentro de uma mão de truco.
 */
public class ResultadoRodada {

    public enum Tipo { VITORIA_A, VITORIA_B, EMPATE }

    private final Tipo tipo;
    private final Carta cartaA;
    private final Carta cartaB;
    private final Jogador vencedor; // null em caso de empate

    private ResultadoRodada(Tipo tipo, Carta cartaA, Carta cartaB, Jogador vencedor) {
        this.tipo     = tipo;
        this.cartaA   = cartaA;
        this.cartaB   = cartaB;
        this.vencedor = vencedor;
    }

    public static ResultadoRodada vitoria(Jogador vencedor, Carta cartaVencedora, Carta cartaPerdedora) {
        Tipo tipo = vencedor.getEquipe() == Equipe.TIME_A ? Tipo.VITORIA_A : Tipo.VITORIA_B;
        return new ResultadoRodada(tipo, cartaVencedora, cartaPerdedora, vencedor);
    }

    public static ResultadoRodada empate(Carta cartaA, Carta cartaB) {
        return new ResultadoRodada(Tipo.EMPATE, cartaA, cartaB, null);
    }

    public boolean isEmpate()      { return tipo == Tipo.EMPATE; }
    public boolean isVitoriaA()    { return tipo == Tipo.VITORIA_A; }
    public boolean isVitoriaB()    { return tipo == Tipo.VITORIA_B; }
    public Tipo    getTipo()        { return tipo; }
    public Jogador getVencedor()    { return vencedor; }
    public Carta   getCartaA()      { return cartaA; }
    public Carta   getCartaB()      { return cartaB; }

    @Override
    public String toString() {
        if (isEmpate()) return "Empate [" + cartaA + " vs " + cartaB + "]";
        return "Vitória de " + vencedor.getNome() + " [" + cartaA + " vs " + cartaB + "]";
    }
}
