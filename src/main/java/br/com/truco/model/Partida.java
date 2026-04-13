package br.com.truco.model;

/**
 * Representa uma partida de Truco Gaúcho.
 * Uma partida vai até 12 pontos (truco gaúcho padrão).
 */
public class Partida {

    private static final int PONTOS_VITORIA = 12;

    private int pontosA;
    private int pontosB;
    private int numeroMao;
    private boolean encerrada;
    private Equipe vencedor;

    public Partida() {
        this.pontosA  = 0;
        this.pontosB  = 0;
        this.numeroMao = 0;
        this.encerrada = false;
        this.vencedor  = null;
    }

    /**
     * Registra o resultado de uma mão encerrada.
     * @param mao a mão que foi disputada
     */
    public void registrarResultadoMao(Mao mao) {
        if (!mao.isEncerrada()) {
            throw new IllegalArgumentException("Mão ainda não foi encerrada");
        }
        if (encerrada) {
            throw new IllegalStateException("Partida já encerrada");
        }

        numeroMao++;
        int pontos = mao.getPontosEmJogo();

        if (mao.getEquipeVencedora() == Equipe.TIME_A) {
            pontosA += pontos;
        } else {
            pontosB += pontos;
        }

        verificarFimDePartida();
    }

    private void verificarFimDePartida() {
        if (pontosA >= PONTOS_VITORIA) {
            encerrada = true;
            vencedor  = Equipe.TIME_A;
        } else if (pontosB >= PONTOS_VITORIA) {
            encerrada = true;
            vencedor  = Equipe.TIME_B;
        }
    }

    public int   getPontosA()    { return pontosA; }
    public int   getPontosB()    { return pontosB; }
    public int   getNumeroMao()  { return numeroMao; }
    public boolean isEncerrada() { return encerrada; }
    public Equipe  getVencedor() { return vencedor; }

    @Override
    public String toString() {
        return String.format("Partida[mão=%d | %s: %d x %s: %d%s]",
            numeroMao,
            Equipe.TIME_A.getNome(), pontosA,
            Equipe.TIME_B.getNome(), pontosB,
            encerrada ? " | Vencedor: " + vencedor : "");
    }
}
