package br.com.truco.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma mão completa de Truco Gaúcho (até 3 rodadas/vazas).
 *
 * <p>Regras de vencimento da mão:
 * <ul>
 *   <li>Vence quem ganhar 2 rodadas.</li>
 *   <li>Empate na 1ª + vitória na 2ª → vence quem ganhou a 2ª (encerra na 2ª).</li>
 *   <li>Vitória na 1ª + empate na 2ª → vence quem ganhou a 1ª (encerra na 2ª).</li>
 *   <li>Empate em todas as rodadas → vence o time que fez a 1ª rodada (mão).</li>
 * </ul>
 */
public class Mao {

    private final List<ResultadoRodada> rodadas;
    private EstadoTruco estadoTruco;
    private Equipe  equipeQueGanhouMao;
    private boolean encerrada;

    public Mao() {
        this.rodadas           = new ArrayList<>();
        this.estadoTruco       = EstadoTruco.SEM_TRUCO;
        this.equipeQueGanhouMao = null;
        this.encerrada         = false;
    }

    /**
     * Registra o resultado de uma rodada e verifica se a mão está encerrada.
     */
    public void registrarRodada(ResultadoRodada resultado) {
        if (encerrada) {
            throw new IllegalStateException("Mão já encerrada");
        }
        rodadas.add(resultado);
        avaliarEncerramento();
    }

    private void avaliarEncerramento() {
        int vitoriasA = 0;
        int vitoriasB = 0;

        for (ResultadoRodada r : rodadas) {
            if (r.isVitoriaA()) vitoriasA++;
            else if (r.isVitoriaB()) vitoriasB++;
        }

        // Venceu 2 rodadas → mão encerrada
        if (vitoriasA >= 2) {
            encerrada = true;
            equipeQueGanhouMao = Equipe.TIME_A;
            return;
        }
        if (vitoriasB >= 2) {
            encerrada = true;
            equipeQueGanhouMao = Equipe.TIME_B;
            return;
        }

        // Após 2 rodadas: casos especiais que encerram sem precisar da 3ª
        if (rodadas.size() == 2) {
            ResultadoRodada r1 = rodadas.get(0);
            ResultadoRodada r2 = rodadas.get(1);

            // Empate na 1ª + vitória na 2ª → quem ganhou a 2ª vence
            if (r1.isEmpate() && !r2.isEmpate()) {
                encerrada = true;
                equipeQueGanhouMao = r2.isVitoriaA() ? Equipe.TIME_A : Equipe.TIME_B;
                return;
            }

            // Vitória na 1ª + empate na 2ª → quem ganhou a 1ª vence
            if (!r1.isEmpate() && r2.isEmpate()) {
                encerrada = true;
                equipeQueGanhouMao = r1.isVitoriaA() ? Equipe.TIME_A : Equipe.TIME_B;
                return;
            }

            // Empate na 1ª + empate na 2ª → ainda vai para a 3ª
            // 1 x 1 → vai para a 3ª (já tratado pelo >= 2 acima)
        }

        // Após 3 rodadas sem vencedor claro (ex: 1x1 com 3ª empatada, ou 3 empates)
        if (rodadas.size() == 3) {
            encerrada = true;
            equipeQueGanhouMao = resolverDesempate(vitoriasA, vitoriasB);
        }
    }

    /**
     * Resolve casos especiais de empate após 3 rodadas.
     */
    private Equipe resolverDesempate(int vitoriasA, int vitoriasB) {
        // 1-1 com 3ª empatada → quem ganhou a 1ª vence
        // 3 empates → TIME_A por padrão (quem tem a "mão")
        for (ResultadoRodada r : rodadas) {
            if (!r.isEmpate()) {
                return r.isVitoriaA() ? Equipe.TIME_A : Equipe.TIME_B;
            }
        }
        // Todas empatadas → TIME_A (mão)
        return Equipe.TIME_A;
    }

    /** Aceita um pedido de truco — avança o estado. */
    public void aceitarTruco(EstadoTruco novoEstado) {
        this.estadoTruco = novoEstado;
    }

    public int getNumeroRodadas()             { return rodadas.size(); }
    public List<ResultadoRodada> getRodadas() { return List.copyOf(rodadas); }
    public EstadoTruco getEstadoTruco()       { return estadoTruco; }
    public int getPontosEmJogo()              { return estadoTruco.getPontos(); }
    public boolean isEncerrada()              { return encerrada; }
    public Equipe getEquipeVencedora()        { return equipeQueGanhouMao; }

    @Override
    public String toString() {
        return "Mao{rodadas=" + rodadas.size() + ", truco=" + estadoTruco
               + ", encerrada=" + encerrada
               + (encerrada ? ", vencedor=" + equipeQueGanhouMao : "")
               + "}";
    }
}