package br.com.truco.engine;

import br.com.truco.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RodadaEngine - Resolução de Vazas")
class RodadaEngineTest {

    private RodadaEngine engine;
    private Jogador jogadorA;
    private Jogador jogadorB;

    @BeforeEach
    void setUp() {
        engine   = new RodadaEngine();
        jogadorA = new Jogador("Gaúcho", Equipe.TIME_A);
        jogadorB = new Jogador("Peão",   Equipe.TIME_B);
    }

    // ── Vitória clara ────────────────────────────────────────────────────────

    @Test
    @DisplayName("Carta mais forte vence a rodada")
    void cartaMaisForteDeveVencer() {
        Carta zap  = new Carta("4", Naipe.PAUS);  // força 14
        Carta tres = new Carta("3", Naipe.COPAS); // força 10

        ResultadoRodada resultado = engine.resolverRodada(jogadorA, zap, jogadorB, tres);

        assertFalse(resultado.isEmpate());
        assertTrue(resultado.isVitoriaA());
        assertEquals(jogadorA, resultado.getVencedor());
    }

    @Test
    @DisplayName("Jogador B vence quando tem carta mais forte")
    void jogadorBVenceComCartaMaisForte() {
        Carta tres = new Carta("3", Naipe.COPAS); // força 10
        Carta zap  = new Carta("4", Naipe.PAUS);  // força 14

        ResultadoRodada resultado = engine.resolverRodada(jogadorA, tres, jogadorB, zap);

        assertFalse(resultado.isEmpate());
        assertTrue(resultado.isVitoriaB());
        assertEquals(jogadorB, resultado.getVencedor());
    }

    // ── Empate ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Cartas de mesma força resultam em empate")
    void cartasDeMesmaForcaResultamEmEmpate() {
        Carta tresOuros = new Carta("3", Naipe.OUROS);
        Carta tresCopas = new Carta("3", Naipe.COPAS);

        ResultadoRodada resultado = engine.resolverRodada(jogadorA, tresOuros, jogadorB, tresCopas);

        assertTrue(resultado.isEmpate());
        assertNull(resultado.getVencedor());
        assertFalse(resultado.isVitoriaA());
        assertFalse(resultado.isVitoriaB());
    }

    // ── Confrontos específicos de manilhas ───────────────────────────────────

    @Test
    @DisplayName("Zap vence 7 de Copas")
    void zapVenceSeteDeCopas() {
        Carta zap   = new Carta("4", Naipe.PAUS);
        Carta copas = new Carta("7", Naipe.COPAS);

        ResultadoRodada r = engine.resolverRodada(jogadorA, zap, jogadorB, copas);
        assertTrue(r.isVitoriaA());
    }

    @Test
    @DisplayName("7 de Copas vence Espadilha")
    void seteCopaVenceEspadilha() {
        Carta copas     = new Carta("7", Naipe.COPAS);
        Carta espadilha = new Carta("A", Naipe.ESPADAS);

        ResultadoRodada r = engine.resolverRodada(jogadorA, copas, jogadorB, espadilha);
        assertTrue(r.isVitoriaA());
    }

    @Test
    @DisplayName("Espadilha vence 7 de Ouros")
    void espadilhaVenceSeteDeOuros() {
        Carta espadilha = new Carta("A", Naipe.ESPADAS);
        Carta seteOuros = new Carta("7", Naipe.OUROS);

        ResultadoRodada r = engine.resolverRodada(jogadorA, espadilha, jogadorB, seteOuros);
        assertTrue(r.isVitoriaA());
    }

    @Test
    @DisplayName("Qualquer manilha vence o 3 (carta normal mais forte)")
    void manilhaVenceTres() {
        Carta seteOuros = new Carta("7", Naipe.OUROS); // manilha mais fraca
        Carta tres      = new Carta("3", Naipe.COPAS); // carta normal mais forte

        ResultadoRodada r = engine.resolverRodada(jogadorA, seteOuros, jogadorB, tres);
        assertTrue(r.isVitoriaA());
    }

    // ── Método auxiliar cartaVence ───────────────────────────────────────────

    @ParameterizedTest
    @DisplayName("cartaVence deve ser consistente com resolverRodada")
    @CsvSource({
        "4,PAUS,  7,COPAS,  true",   // Zap vence Copas
        "7,COPAS, A,ESPADAS,true",   // Copas vence Espadilha
        "3,OUROS, 2,OUROS,  true",   // 3 vence 2
        "2,OUROS, 3,OUROS,  false",  // 2 perde pro 3
        "K,OUROS, K,PAUS,   false",  // K empata com K (não vence)
    })
    void cartaVenceDeveSerConsistente(
            String v1, String n1,
            String v2, String n2,
            boolean esperado) {
        Carta ca = new Carta(v1, Naipe.valueOf(n1));
        Carta cb = new Carta(v2, Naipe.valueOf(n2));
        assertEquals(esperado, engine.cartaVence(ca, cb));
    }

    // ── Resultado preserva as cartas ─────────────────────────────────────────

    @Test
    @DisplayName("Resultado deve preservar as cartas jogadas")
    void resultadoDevePreservarCartas() {
        Carta cartaA = new Carta("3", Naipe.OUROS);
        Carta cartaB = new Carta("2", Naipe.COPAS);

        ResultadoRodada resultado = engine.resolverRodada(jogadorA, cartaA, jogadorB, cartaB);

        assertNotNull(resultado.getCartaA());
        assertNotNull(resultado.getCartaB());
    }
}
