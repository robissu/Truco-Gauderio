package br.com.truco;

import br.com.truco.engine.RodadaEngine;
import br.com.truco.engine.TrucoEngine;
import br.com.truco.model.*;
import br.com.truco.service.DistribuidorService;
import br.com.truco.service.PlacarService;

/**
 * Entrada principal do Truco Gaúcho - simulação automática de uma partida.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║     TRUCO GAÚCHO - Simulação     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.println();

        // Criação dos jogadores
        Jogador gaucho  = new Jogador("Gaúcho",  Equipe.TIME_A);
        Jogador peão    = new Jogador("Peão",    Equipe.TIME_B);

        // Infraestrutura
        Baralho baralho   = new Baralho();
        RodadaEngine re   = new RodadaEngine();
        DistribuidorService dist = new DistribuidorService();
        TrucoEngine engine = new TrucoEngine(re, dist);
        PlacarService placar = new PlacarService();

        int maoNum = 0;

        // Joga até alguém vencer
        while (!placar.isPartidaEncerrada()) {
            maoNum++;
            baralho.reiniciar();

            System.out.printf("━━━ MÃO %d ━━━%n", maoNum);

            Mao mao = engine.executarMao(gaucho, peão, baralho);

            // Exibe rodadas
            for (int i = 0; i < mao.getRodadas().size(); i++) {
                ResultadoRodada r = mao.getRodadas().get(i);
                System.out.printf("  Rodada %d: %s%n", i + 1, r);
            }

            System.out.printf("  Resultado: %s venceu a mão (%d ponto(s))%n",
                mao.getEquipeVencedora().getNome(),
                mao.getPontosEmJogo());

            placar.registrarMao(mao);
            System.out.printf("  Placar: %s%n%n", placar.getPlacar());

            if (maoNum > 50) {
                System.out.println("[Limite de segurança atingido]");
                break;
            }
        }

        System.out.println("══════════════════════════════════");
        System.out.printf("🏆 Vencedor: %s%n", placar.getVencedorPartida().getNome());
        System.out.printf("   Placar final: %s%n", placar.getPlacar());
    }
}
