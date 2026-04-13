package br.com.truco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Partida - Controle de placar e encerramento")
class PartidaTest {

    private Partida partida;
    private Jogador jogadorA;
    private Jogador jogadorB;

    @BeforeEach
    void setUp() {
        partida  = new Partida();
        jogadorA = new Jogador("Gaúcho", Equipe.TIME_A);
        jogadorB = new Jogador("Peão",   Equipe.TIME_B);
    }

    /** Cria uma mão já encerrada com vencedor e pontos específicos. */
    private Mao criarMaoEncerrada(Jogador vencedor, int pontos) {
        Mao mao = new Mao();
        if (pontos > 1) {
            // Sobe o estado do truco até o nível correto
            EstadoTruco estado = EstadoTruco.SEM_TRUCO;
            while (estado.getPontos() < pontos && estado.proximoNivel() != null) {
                estado = estado.proximoNivel();
                mao.aceitarTruco(estado);
            }
        }
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("2", Naipe.OUROS);
        mao.registrarRodada(ResultadoRodada.vitoria(vencedor, ca, cb));
        mao.registrarRodada(ResultadoRodada.vitoria(vencedor, ca, cb));
        return mao;
    }

    // ── Estado inicial ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Partida nova deve ter placar 0 x 0")
    void partidaNovaTevePlacarZero() {
        assertEquals(0, partida.getPontosA());
        assertEquals(0, partida.getPontosB());
        assertFalse(partida.isEncerrada());
        assertNull(partida.getVencedor());
    }

    // ── Acumulação de pontos ─────────────────────────────────────────────────

    @Test
    @DisplayName("Vitória sem truco deve adicionar 1 ponto")
    void vitoriaSimples_DeveAdicionar1Ponto() {
        Mao mao = criarMaoEncerrada(jogadorA, 1);
        partida.registrarResultadoMao(mao);
        assertEquals(1, partida.getPontosA());
        assertEquals(0, partida.getPontosB());
    }

    @Test
    @DisplayName("Vitória com truco deve adicionar 2 pontos")
    void vitoriaComTrucoDeveAdicionar2Pontos() {
        Mao mao = criarMaoEncerrada(jogadorA, 2);
        partida.registrarResultadoMao(mao);
        assertEquals(2, partida.getPontosA());
    }

    @Test
    @DisplayName("Vitória com seis deve adicionar 6 pontos")
    void vitoriaComSeisDeveAdicionar6Pontos() {
        Mao mao = criarMaoEncerrada(jogadorB, 6);
        partida.registrarResultadoMao(mao);
        assertEquals(6, partida.getPontosB());
    }

    // ── Encerramento da partida ──────────────────────────────────────────────

    @Test
    @DisplayName("Partida deve encerrar quando Time A atingir 12 pontos")
    void partidaDeveEncerrarComTimeAVencendo() {
        // 2 vitórias com seis (6+6=12)
        partida.registrarResultadoMao(criarMaoEncerrada(jogadorA, 6));
        assertFalse(partida.isEncerrada());

        partida.registrarResultadoMao(criarMaoEncerrada(jogadorA, 6));
        assertTrue(partida.isEncerrada());
        assertEquals(Equipe.TIME_A, partida.getVencedor());
    }

    @Test
    @DisplayName("Partida deve encerrar quando Time B atingir 12 pontos")
    void partidaDeveEncerrarComTimeBVencendo() {
        partida.registrarResultadoMao(criarMaoEncerrada(jogadorB, 6));
        partida.registrarResultadoMao(criarMaoEncerrada(jogadorB, 6));

        assertTrue(partida.isEncerrada());
        assertEquals(Equipe.TIME_B, partida.getVencedor());
    }

    @Test
    @DisplayName("Partida deve encerrar com mão de doze (12 pts de uma vez)")
    void partidaDeveEncerrarComDoze() {
        Mao mao = criarMaoEncerrada(jogadorA, 12);
        partida.registrarResultadoMao(mao);

        assertTrue(partida.isEncerrada());
        assertEquals(Equipe.TIME_A, partida.getVencedor());
        assertEquals(12, partida.getPontosA());
    }

    // ── Proteções ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registrar mão não encerrada deve lançar IllegalArgumentException")
    void registrarMaoAbertaDeveLancarExcecao() {
        Mao maoAberta = new Mao(); // sem rodadas
        assertThrows(IllegalArgumentException.class,
            () -> partida.registrarResultadoMao(maoAberta));
    }

    @Test
    @DisplayName("Registrar mão após partida encerrada deve lançar IllegalStateException")
    void registrarMaoAposEncerramentoDeveLancarExcecao() {
        partida.registrarResultadoMao(criarMaoEncerrada(jogadorA, 12));
        assertTrue(partida.isEncerrada());

        assertThrows(IllegalStateException.class,
            () -> partida.registrarResultadoMao(criarMaoEncerrada(jogadorA, 1)));
    }

    @Test
    @DisplayName("Contador de mãos deve incrementar a cada mão registrada")
    void contadorDeMaosDeveIncrementar() {
        assertEquals(0, partida.getNumeroMao());
        partida.registrarResultadoMao(criarMaoEncerrada(jogadorA, 1));
        assertEquals(1, partida.getNumeroMao());
        partida.registrarResultadoMao(criarMaoEncerrada(jogadorB, 1));
        assertEquals(2, partida.getNumeroMao());
    }
}
