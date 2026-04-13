package br.com.truco.model;

/**
 * Estados possíveis do pedido de truco / aumento de aposta.
 *
 * <p>No Truco Gaúcho, a escalada de pontos é:
 * Truco (2 pts) → Seis (6 pts) → Nove (9 pts) → Doze (12 pts = mão fechada)
 */
public enum EstadoTruco {

    SEM_TRUCO(1, "Sem truco"),
    TRUCO(2, "Truco"),
    SEIS(6, "Seis"),
    NOVE(9, "Nove"),
    DOZE(12, "Doze");

    private final int pontos;
    private final String descricao;

    EstadoTruco(int pontos, String descricao) {
        this.pontos    = pontos;
        this.descricao = descricao;
    }

    /** Retorna o próximo nível de aposta, ou null se já é máximo. */
    public EstadoTruco proximoNivel() {
        return switch (this) {
            case SEM_TRUCO -> TRUCO;
            case TRUCO     -> SEIS;
            case SEIS      -> NOVE;
            case NOVE      -> DOZE;
            case DOZE      -> null;
        };
    }

    public boolean isMaximo() { return this == DOZE; }
    public int     getPontos()     { return pontos; }
    public String  getDescricao()  { return descricao; }

    @Override
    public String toString() { return descricao + " (" + pontos + "pts)"; }
}
