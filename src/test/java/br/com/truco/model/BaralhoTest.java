package br.com.truco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Baralho - Deck de 40 cartas")
class BaralhoTest {

    private Baralho baralho;

    @BeforeEach
    void setUp() {
        baralho = new Baralho();
    }

    @Test
    @DisplayName("Baralho novo deve ter 40 cartas")
    void baralhoNovoDeveTer40Cartas() {
        assertEquals(40, baralho.tamanho());
    }

    @Test
    @DisplayName("Baralho não deve ter cartas duplicadas")
    void baralhoNaoDeveTerDuplicatas() {
        List<Carta> todasCartas = baralho.distribuir(40);
        Set<Carta> unicos = new HashSet<>(todasCartas);
        assertEquals(40, unicos.size(), "Não deve haver cartas duplicadas");
    }

    @Test
    @DisplayName("Distribuir 3 cartas deve reduzir o baralho para 37")
    void distribuirDeveRemoverCartasDoBaralho() {
        baralho.distribuir(3);
        assertEquals(37, baralho.tamanho());
    }

    @Test
    @DisplayName("Distribuir deve retornar a quantidade solicitada")
    void distribuirDeveRetornarQuantidadeSolicitada() {
        List<Carta> cartas = baralho.distribuir(5);
        assertEquals(5, cartas.size());
    }

    @Test
    @DisplayName("Distribuir mais cartas que o disponível deve lançar exceção")
    void distribuirAlemDoDisponivelDeveLancarExcecao() {
        baralho.distribuir(38); // restam 2
        assertThrows(IllegalStateException.class, () -> baralho.distribuir(5));
    }

    @Test
    @DisplayName("Baralho vazio deve retornar isEmpty = true")
    void baralhoVazioDeveRetornarIsEmpty() {
        baralho.distribuir(40);
        assertTrue(baralho.isEmpty());
    }

    @Test
    @DisplayName("Reiniciar deve restaurar o baralho para 40 cartas")
    void reiniciarDeveRestaurarBaralho() {
        baralho.distribuir(30);
        assertEquals(10, baralho.tamanho());
        baralho.reiniciar();
        assertEquals(40, baralho.tamanho());
    }

    @Test
    @DisplayName("Embaralhar não deve alterar a quantidade de cartas")
    void embaralharNaoDeveAlterarQuantidade() {
        baralho.embaralhar();
        assertEquals(40, baralho.tamanho());
    }

    @Test
    @DisplayName("Baralho deve conter o 4 de Paus (Zap)")
    void baralhoDeveConterZap() {
        List<Carta> cartas = baralho.distribuir(40);
        Carta zap = new Carta("4", Naipe.PAUS);
        assertTrue(cartas.contains(zap), "Baralho deve conter o Zap (4 de Paus)");
    }

    @Test
    @DisplayName("Baralho deve conter as 4 manilhas fixas")
    void baralhoDeveConterAs4Manilhas() {
        List<Carta> cartas = baralho.distribuir(40);
        long manilhas = cartas.stream().filter(Carta::isManilha).count();
        assertEquals(4, manilhas, "Deve haver exatamente 4 manilhas");
    }
}
