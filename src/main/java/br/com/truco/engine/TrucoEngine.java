package br.com.truco.engine;

import br.com.truco.model.*;
import br.com.truco.service.DistribuidorService;

import java.util.List;

/**
 * Motor principal do Truco Gaúcho.
 * Orquestra uma mão completa (até 3 rodadas) entre dois jogadores.
 */
public class TrucoEngine {

    private final RodadaEngine rodadaEngine;
    private final DistribuidorService distribuidor;

    public TrucoEngine(RodadaEngine rodadaEngine, DistribuidorService distribuidor) {
        this.rodadaEngine = rodadaEngine;
        this.distribuidor = distribuidor;
    }

    /**
     * Executa uma mão completa de truco entre dois jogadores (modo automático/IA).
     *
     * @param jogadorA primeiro jogador
     * @param jogadorB segundo jogador
     * @param baralho  baralho já embaralhado
     * @return mão encerrada com todos os resultados registrados
     */
    public Mao executarMao(Jogador jogadorA, Jogador jogadorB, Baralho baralho) {
        distribuidor.distribuirMao(baralho, jogadorA, jogadorB);

        Mao mao = new Mao();

        // Até 3 rodadas
        while (!mao.isEncerrada() && mao.getNumeroRodadas() < 3) {
            if (!jogadorA.temCartas() || !jogadorB.temCartas()) break;

            Carta cartaA = jogadorA.jogarMelhorCarta();
            Carta cartaB = jogadorB.jogarMelhorCarta();

            ResultadoRodada resultado = rodadaEngine.resolverRodada(jogadorA, cartaA, jogadorB, cartaB);
            mao.registrarRodada(resultado);
        }

        return mao;
    }

    /**
     * Executa uma rodada manual (carta escolhida externamente).
     */
    public ResultadoRodada executarRodada(
            Jogador jogadorA, Carta cartaA,
            Jogador jogadorB, Carta cartaB) {
        return rodadaEngine.resolverRodada(jogadorA, cartaA, jogadorB, cartaB);
    }

    /**
     * Processa pedido de truco.
     * @param mao       mão em andamento
     * @param pedinte   equipe que pediu
     * @param aceito    se o adversário aceitou
     * @return true se o truco foi aceito e o estado foi atualizado
     */
    public boolean processarTruco(Mao mao, EstadoTruco nivelPedido, boolean aceito) {
        if (mao.isEncerrada()) return false;
        if (nivelPedido == null) return false;

        if (aceito) {
            mao.aceitarTruco(nivelPedido);
            return true;
        }
        // Se recusado, a mão é encerrada com 1 ponto para o adversário
        // (tratado na lógica superior da partida)
        return false;
    }
}
