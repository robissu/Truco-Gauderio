package br.com.truco.service;

import br.com.truco.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PlacarService - Gerenciamento de Placar")
class PlacarServiceTest {

    private PlacarService placar;
    private Jogador jogadorA;
    private Jogador jogadorB;

    @BeforeEach
    void setUp() {
        placar   = new PlacarService();
        jogadorA = new Jogador("Gaúcho", Equipe.TIME_A);
        jogadorB = new Jogador("Peão",   Equipe.TIME_B);
    }

    private Mao criarMaoVencidaPor(Jogador vencedor) {
        Mao mao = new Mao();
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("2", Naipe.OUROS);
        mao.registrarRodada(ResultadoRodada.vitoria(vencedor, ca, cb));
        mao.registrarRodada(ResultadoRodada.vitoria(vencedor, ca, cb));
        return mao;
    }

    @Test
    @DisplayName("Placar inicial deve ser 0 x 0")
    void placarInicialDeveSerZeroAZero() {
        assertEquals(0, placar.getPontosA());
        assertEquals(0, placar.getPontosB());
        assertFalse(placar.isPartidaEncerrada());
    }

    @Test
    @DisplayName("Registrar vitória do Time A deve incrementar pontos de A")
    void registrarVitoriaTimaADeveIncrementarPontosA() {
        placar.registrarMao(criarMaoVencidaPor(jogadorA));
        assertEquals(1, placar.getPontosA());
        assertEquals(0, placar.getPontosB());
    }

    @Test
    @DisplayName("Registrar vitória do Time B deve incrementar pontos de B")
    void registrarVitoriaTimeBDeveIncrementarPontosB() {
        placar.registrarMao(criarMaoVencidaPor(jogadorB));
        assertEquals(0, placar.getPontosA());
        assertEquals(1, placar.getPontosB());
    }

    @Test
    @DisplayName("getPlacar deve retornar string legível")
    void getPlacarDeveRetornarStringLegivel() {
        placar.registrarMao(criarMaoVencidaPor(jogadorA));
        String placarStr = placar.getPlacar();
        assertNotNull(placarStr);
        assertTrue(placarStr.contains("1"), "Placar deve conter o ponto de A");
        assertTrue(placarStr.contains("0"), "Placar deve conter os pontos de B");
    }

    @Test
    @DisplayName("Partida deve encerrar ao atingir 12 pontos")
    void partidaDeveEncerrarAo12Pontos() {
        // Cria mãos com truco (2 pts) e registra até 12
        for (int i = 0; i < 6; i++) {
            Mao mao = new Mao();
            mao.aceitarTruco(EstadoTruco.TRUCO); // 2 pts
            Carta ca = new Carta("3", Naipe.OUROS);
            Carta cb = new Carta("2", Naipe.OUROS);
            mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));
            mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));
            placar.registrarMao(mao);
            if (placar.isPartidaEncerrada()) break;
        }

        assertTrue(placar.isPartidaEncerrada());
        assertEquals(Equipe.TIME_A, placar.getVencedorPartida());
        assertEquals(12, placar.getPontosA());
    }

    @Test
    @DisplayName("getPartida não deve retornar null")
    void getPartidaNaoDeveRetornarNull() {
        assertNotNull(placar.getPartida());
    }
}
