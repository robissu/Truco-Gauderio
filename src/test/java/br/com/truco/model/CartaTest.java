package br.com.truco.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Carta - Hierarquia do Truco Gaúcho")
class CartaTest {

    // ── Manilhas ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("4 de Paus (Zap) deve ser a carta mais forte (força 14)")
    void zapDeveSerMaisForte() {
        Carta zap = new Carta("4", Naipe.PAUS);
        assertEquals(14, zap.getForca());
        assertTrue(zap.isManilha());
    }

    @Test
    @DisplayName("7 de Copas deve ter força 13")
    void seteDeCopasDeveSerSegundo() {
        Carta copas = new Carta("7", Naipe.COPAS);
        assertEquals(13, copas.getForca());
        assertTrue(copas.isManilha());
    }

    @Test
    @DisplayName("Ás de Espadas (Espadilha) deve ter força 12")
    void espadilhaDeveTerForca12() {
        Carta espadilha = new Carta("A", Naipe.ESPADAS);
        assertEquals(12, espadilha.getForca());
        assertTrue(espadilha.isManilha());
    }

    @Test
    @DisplayName("7 de Ouros deve ter força 11")
    void seteDeOurosDeveTerForca11() {
        Carta seteOuros = new Carta("7", Naipe.OUROS);
        assertEquals(11, seteOuros.getForca());
        assertTrue(seteOuros.isManilha());
    }

    // ── Ordem das manilhas ──────────────────────────────────────────────────

    @Test
    @DisplayName("Zap deve vencer todas as outras manilhas")
    void zapVenceTodasManilhas() {
        Carta zap        = new Carta("4", Naipe.PAUS);
        Carta copas      = new Carta("7", Naipe.COPAS);
        Carta espadilha  = new Carta("A", Naipe.ESPADAS);
        Carta seteOuros  = new Carta("7", Naipe.OUROS);

        assertTrue(zap.compareTo(copas)     > 0);
        assertTrue(zap.compareTo(espadilha) > 0);
        assertTrue(zap.compareTo(seteOuros) > 0);
    }

    @Test
    @DisplayName("7 de Copas vence Espadilha e 7 de Ouros")
    void seteCopasVenceEspadilhaESeteOuros() {
        Carta copas     = new Carta("7", Naipe.COPAS);
        Carta espadilha = new Carta("A", Naipe.ESPADAS);
        Carta seteOuros = new Carta("7", Naipe.OUROS);

        assertTrue(copas.compareTo(espadilha) > 0);
        assertTrue(copas.compareTo(seteOuros) > 0);
    }

    // ── Cartas normais ──────────────────────────────────────────────────────

    @Test
    @DisplayName("3 deve ter força 10 (mais forte carta normal)")
    void tresDeveSerMaisForteCartaNormal() {
        Carta tres = new Carta("3", Naipe.OUROS);
        assertEquals(10, tres.getForca());
        assertFalse(tres.isManilha());
    }

    @ParameterizedTest
    @DisplayName("Hierarquia das cartas normais")
    @CsvSource({
        "3, OUROS,   10",
        "2, OUROS,    9",
        "K, OUROS,    7",
        "J, OUROS,    6",
        "Q, OUROS,    5",
        "6, OUROS,    3",
        "5, OUROS,    2",
    })
    void hierarquiaCartasNormais(String valor, String naipeStr, int forcaEsperada) {
        Naipe naipe = Naipe.valueOf(naipeStr);
        Carta carta = new Carta(valor, naipe);
        assertEquals(forcaEsperada, carta.getForca(),
            () -> "Força incorreta para " + valor + " de " + naipe);
    }

    @Test
    @DisplayName("3 normal deve ser mais forte que 2")
    void tresVenceDois() {
        Carta tres = new Carta("3", Naipe.COPAS);
        Carta dois = new Carta("2", Naipe.PAUS);
        assertTrue(tres.compareTo(dois) > 0);
    }

    // ── Casos de não-manilha vs manilha ────────────────────────────────────

    @Test
    @DisplayName("3 (carta mais forte normal) perde para 7 de Ouros (manilha)")
    void cartaNormalPerdePraManilha() {
        Carta tres      = new Carta("3", Naipe.COPAS);
        Carta seteOuros = new Carta("7", Naipe.OUROS);
        assertTrue(tres.compareTo(seteOuros) < 0);
    }

    @Test
    @DisplayName("4 normal (sem ser paus) NÃO é manilha")
    void quatroSemSerPausNaoEManilha() {
        Carta quatroOuros  = new Carta("4", Naipe.OUROS);
        Carta quatroEspada = new Carta("4", Naipe.ESPADAS);
        Carta quatroCopas  = new Carta("4", Naipe.COPAS);

        assertFalse(quatroOuros.isManilha());
        assertFalse(quatroEspada.isManilha());
        assertFalse(quatroCopas.isManilha());
        assertEquals(1, quatroOuros.getForca());
    }

    @Test
    @DisplayName("7 de Espadas NÃO é manilha")
    void seteDeEspadaNaoEManilha() {
        Carta seteEspada = new Carta("7", Naipe.ESPADAS);
        assertFalse(seteEspada.isManilha());
        assertEquals(4, seteEspada.getForca()); // força de 7 normal
    }

    // ── equals e hashCode ──────────────────────────────────────────────────

    @Test
    @DisplayName("Duas cartas com mesmo valor e naipe devem ser iguais")
    void cartasIguaisDevemSerEquals() {
        Carta c1 = new Carta("3", Naipe.OUROS);
        Carta c2 = new Carta("3", Naipe.OUROS);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    @DisplayName("Cartas com naipes diferentes não são iguais")
    void cartasComNaipesDiferentesNaoSaoIguais() {
        Carta c1 = new Carta("3", Naipe.OUROS);
        Carta c2 = new Carta("3", Naipe.PAUS);
        assertNotEquals(c1, c2);
    }

    // ── toString e validação ────────────────────────────────────────────────

    @Test
    @DisplayName("toString deve conter valor e naipe")
    void toStringShouldContainValueAndSuit() {
        Carta c = new Carta("K", Naipe.COPAS);
        String str = c.toString();
        assertTrue(str.contains("K"));
        assertTrue(str.contains("Copas"));
    }

    @Test
    @DisplayName("Construtor deve lançar NullPointerException para valor nulo")
    void construtorNaoDeveAceitarValorNulo() {
        assertThrows(NullPointerException.class, () -> new Carta(null, Naipe.OUROS));
    }

    @Test
    @DisplayName("Construtor deve lançar NullPointerException para naipe nulo")
    void construtorNaoDeveAceitarNaipeNulo() {
        assertThrows(NullPointerException.class, () -> new Carta("A", null));
    }
}
