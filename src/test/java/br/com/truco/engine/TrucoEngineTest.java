package br.com.truco.engine;

import br.com.truco.model.*;
import br.com.truco.service.DistribuidorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TrucoEngine - Orquestração da Mão Completa")
class TrucoEngineTest {

    private TrucoEngine engine;
    private Jogador jogadorA;
    private Jogador jogadorB;
    private Baralho baralho;

    @BeforeEach
    void setUp() {
        engine   = new TrucoEngine(new RodadaEngine(), new DistribuidorService());
        jogadorA = new Jogador("Gaúcho", Equipe.TIME_A);
        jogadorB = new Jogador("Peão",   Equipe.TIME_B);
        baralho  = new Baralho();
    }

    // ── executarMao ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("executarMao deve retornar mão encerrada")
    void executarMaoDeveRetornarMaoEncerrada() {
        baralho.reiniciar();
        Mao mao = engine.executarMao(jogadorA, jogadorB, baralho);
        assertTrue(mao.isEncerrada(), "A mão deve estar encerrada após executarMao");
    }

    @Test
    @DisplayName("executarMao deve registrar entre 1 e 3 rodadas")
    void executarMaoDeveRegistrarEntre1e3Rodadas() {
        baralho.reiniciar();
        Mao mao = engine.executarMao(jogadorA, jogadorB, baralho);
        int rodadas = mao.getNumeroRodadas();
        assertTrue(rodadas >= 1 && rodadas <= 3,
            "Número de rodadas deve ser entre 1 e 3, foi: " + rodadas);
    }

    @Test
    @DisplayName("executarMao deve ter um vencedor definido")
    void executarMaoDeveDefinirVencedor() {
        baralho.reiniciar();
        Mao mao = engine.executarMao(jogadorA, jogadorB, baralho);
        assertNotNull(mao.getEquipeVencedora(), "Deve haver vencedor definido");
    }

    @Test
    @DisplayName("executarMao com Zap garantido para A deve resultar em vitória mais frequente de A")
    void executarMaoComZapParaA() {
        // Força uma mão onde jogadorA tem o Zap, Copas e 3 (cartas fortíssimas)
        jogadorA.receberCartas(List.of(
            new Carta("4", Naipe.PAUS),   // Zap - força 14
            new Carta("7", Naipe.COPAS),  // força 13
            new Carta("3", Naipe.OUROS)   // força 10
        ));
        jogadorB.receberCartas(List.of(
            new Carta("4", Naipe.OUROS),  // força 1
            new Carta("4", Naipe.COPAS),  // força 1
            new Carta("5", Naipe.PAUS)    // força 2
        ));

        RodadaEngine re = new RodadaEngine();
        Mao mao = new Mao();

        // Simula as rodadas manualmente com cartas controladas
        Carta zapA    = jogadorA.jogarMelhorCarta();
        Carta quatroB = jogadorB.jogarMelhorCarta(); // 5 de paus (mais forte de B)

        ResultadoRodada r1 = engine.executarRodada(jogadorA, zapA, jogadorB, quatroB);
        mao.registrarRodada(r1);

        assertTrue(r1.isVitoriaA(), "Zap deve vencer qualquer carta de B");
    }

    // ── executarRodada ───────────────────────────────────────────────────────

    @Test
    @DisplayName("executarRodada deve retornar resultado correto")
    void executarRodadaDeveRetornarResultadoCorreto() {
        Carta zapA    = new Carta("4", Naipe.PAUS);
        Carta tresB   = new Carta("3", Naipe.COPAS);

        ResultadoRodada r = engine.executarRodada(jogadorA, zapA, jogadorB, tresB);

        assertNotNull(r);
        assertTrue(r.isVitoriaA());
        assertEquals(jogadorA, r.getVencedor());
    }

    // ── processarTruco ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Processar truco aceito deve atualizar estado da mão")
    void processarTrucoAceitoDeveAtualizarEstado() {
        Mao mao = new Mao();
        assertEquals(EstadoTruco.SEM_TRUCO, mao.getEstadoTruco());

        boolean aceito = engine.processarTruco(mao, EstadoTruco.TRUCO, true);

        assertTrue(aceito);
        assertEquals(EstadoTruco.TRUCO, mao.getEstadoTruco());
        assertEquals(2, mao.getPontosEmJogo());
    }

    @Test
    @DisplayName("Processar truco recusado não deve alterar o estado")
    void processarTrucoRecusadoNaoDeveAlterarEstado() {
        Mao mao = new Mao();
        boolean aceito = engine.processarTruco(mao, EstadoTruco.TRUCO, false);

        assertFalse(aceito);
        assertEquals(EstadoTruco.SEM_TRUCO, mao.getEstadoTruco());
    }

    @Test
    @DisplayName("Processar truco em mão encerrada deve retornar false")
    void processarTrucoEmMaoEncerradaDeveRetornarFalse() {
        baralho.reiniciar();
        Mao mao = engine.executarMao(jogadorA, jogadorB, baralho);
        assertTrue(mao.isEncerrada());

        boolean resultado = engine.processarTruco(mao, EstadoTruco.TRUCO, true);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Escalada de truco até seis deve funcionar")
    void escaladaTrucoAteSeis() {
        Mao mao = new Mao();
        engine.processarTruco(mao, EstadoTruco.TRUCO, true);
        engine.processarTruco(mao, EstadoTruco.SEIS, true);

        assertEquals(EstadoTruco.SEIS, mao.getEstadoTruco());
        assertEquals(6, mao.getPontosEmJogo());
    }

    // ── Integração: múltiplas mãos ───────────────────────────────────────────

    @Test
    @DisplayName("Executar 3 mãos seguidas não deve lançar exceções")
    void executar3MaosSeguidas() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 3; i++) {
                baralho.reiniciar();
                Mao mao = engine.executarMao(jogadorA, jogadorB, baralho);
                assertTrue(mao.isEncerrada());
            }
        });
    }
}
