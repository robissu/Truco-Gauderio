package br.com.truco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mao - Lógica de encerramento da mão")
class MaoTest {

    private Jogador jogadorA;
    private Jogador jogadorB;
    private Mao mao;

    @BeforeEach
    void setUp() {
        jogadorA = new Jogador("Gaúcho",  Equipe.TIME_A);
        jogadorB = new Jogador("Peão",    Equipe.TIME_B);
        mao = new Mao();
    }

    // ── Vitória simples (2-0) ────────────────────────────────────────────────

    @Test
    @DisplayName("Vencer 2 rodadas seguidas deve encerrar a mão para Time A")
    void vencer2RodasSeguidas_TimeA_DeveEncerrar() {
        Carta c3 = new Carta("3", Naipe.OUROS);
        Carta c2 = new Carta("2", Naipe.OUROS);

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, c3, c2));
        assertFalse(mao.isEncerrada(), "Após 1 rodada não deve estar encerrada");

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, c3, c2));
        assertTrue(mao.isEncerrada());
        assertEquals(Equipe.TIME_A, mao.getEquipeVencedora());
    }

    @Test
    @DisplayName("Time B vencer 2 rodadas deve encerrar a mão para Time B")
    void vencer2Rodadas_TimeB_DeveEncerrar() {
        Carta c3 = new Carta("3", Naipe.OUROS);
        Carta c2 = new Carta("2", Naipe.OUROS);

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorB, c3, c2));
        mao.registrarRodada(ResultadoRodada.vitoria(jogadorB, c3, c2));

        assertTrue(mao.isEncerrada());
        assertEquals(Equipe.TIME_B, mao.getEquipeVencedora());
    }

    // ── Empate na 1ª + vitória na 2ª ────────────────────────────────────────

    @Test
    @DisplayName("Empate na 1ª + vitória A na 2ª → Time A vence após 2 rodadas")
    void empate1a_VitoriaA2a_EncerraComVitoriaA() {
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("3", Naipe.COPAS);

        mao.registrarRodada(ResultadoRodada.empate(ca, cb));
        assertFalse(mao.isEncerrada());

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));
        assertTrue(mao.isEncerrada());
        assertEquals(Equipe.TIME_A, mao.getEquipeVencedora());
    }

    @Test
    @DisplayName("Empate na 1ª + vitória B na 2ª → Time B vence")
    void empate1a_VitoriaB2a_EncerraComVitoriaB() {
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("3", Naipe.COPAS);

        mao.registrarRodada(ResultadoRodada.empate(ca, cb));
        mao.registrarRodada(ResultadoRodada.vitoria(jogadorB, cb, ca));

        assertTrue(mao.isEncerrada());
        assertEquals(Equipe.TIME_B, mao.getEquipeVencedora());
    }

    // ── Vitória na 1ª + empate na 2ª ────────────────────────────────────────

    @Test
    @DisplayName("Vitória A na 1ª + empate na 2ª → Time A vence (regra gaúcha)")
    void vitoriaA1a_Empate2a_EncerraComVitoriaA() {
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("2", Naipe.COPAS);
        Carta ce = new Carta("K", Naipe.PAUS);

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));
        mao.registrarRodada(ResultadoRodada.empate(ce, ce));

        assertTrue(mao.isEncerrada());
        assertEquals(Equipe.TIME_A, mao.getEquipeVencedora());
    }

    // ── 1 a 1 decide na 3ª ──────────────────────────────────────────────────

    @Test
    @DisplayName("1 x 1 deve ir para a 3ª rodada e decidir vencedor")
    void umAUm_DeveIrParaTerceiraRodada() {
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("2", Naipe.OUROS);

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));
        mao.registrarRodada(ResultadoRodada.vitoria(jogadorB, ca, cb));

        assertFalse(mao.isEncerrada(), "Com 1 a 1 ainda não deve estar encerrada");
        assertEquals(2, mao.getNumeroRodadas());

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));
        assertTrue(mao.isEncerrada());
        assertEquals(Equipe.TIME_A, mao.getEquipeVencedora());
    }

    // ── Truco ───────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Estado inicial deve ser SEM_TRUCO com 1 ponto em jogo")
    void estadoInicialSemTruco() {
        assertEquals(EstadoTruco.SEM_TRUCO, mao.getEstadoTruco());
        assertEquals(1, mao.getPontosEmJogo());
    }

    @Test
    @DisplayName("Aceitar truco deve mudar estado para TRUCO com 2 pontos")
    void aceitarTrucoDeveMudarEstado() {
        mao.aceitarTruco(EstadoTruco.TRUCO);
        assertEquals(EstadoTruco.TRUCO, mao.getEstadoTruco());
        assertEquals(2, mao.getPontosEmJogo());
    }

    @Test
    @DisplayName("Escalada completa deve chegar a DOZE (12 pontos)")
    void escaladaCompletaDeveChegar12() {
        mao.aceitarTruco(EstadoTruco.TRUCO);
        mao.aceitarTruco(EstadoTruco.SEIS);
        mao.aceitarTruco(EstadoTruco.NOVE);
        mao.aceitarTruco(EstadoTruco.DOZE);

        assertEquals(EstadoTruco.DOZE, mao.getEstadoTruco());
        assertEquals(12, mao.getPontosEmJogo());
    }

    // ── Proteções ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Registrar rodada em mão encerrada deve lançar IllegalStateException")
    void registrarRodadaEmMaoEncerradaDeveLancarExcecao() {
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("2", Naipe.OUROS);

        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));
        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));

        assertTrue(mao.isEncerrada());
        assertThrows(IllegalStateException.class,
            () -> mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb)));
    }

    @Test
    @DisplayName("getRodadas deve retornar cópia imutável")
    void getRodasDeveRetornarCopiaImutavel() {
        Carta ca = new Carta("3", Naipe.OUROS);
        Carta cb = new Carta("2", Naipe.OUROS);
        mao.registrarRodada(ResultadoRodada.vitoria(jogadorA, ca, cb));

        var rodadas = mao.getRodadas();
        assertThrows(UnsupportedOperationException.class, () -> rodadas.remove(0));
    }
}
