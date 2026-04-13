package br.com.truco.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Jogador - Comportamento do Jogador")
class JogadorTest {

    private Jogador jogador;

    @BeforeEach
    void setUp() {
        jogador = new Jogador("Gaúcho", Equipe.TIME_A);
        jogador.receberCartas(List.of(
            new Carta("3", Naipe.COPAS),
            new Carta("2", Naipe.OUROS),
            new Carta("K", Naipe.PAUS)
        ));
    }

    @Test
    @DisplayName("Jogador deve receber 3 cartas corretamente")
    void jogadorDeveReceberCartasCorretamente() {
        assertEquals(3, jogador.qtdCartas());
        assertTrue(jogador.temCartas());
    }

    @Test
    @DisplayName("Jogar por índice deve remover a carta da mão")
    void jogarCartaPorIndiceDeveRemoverDaMao() {
        int qtdAntes = jogador.qtdCartas();
        jogador.jogarCarta(0);
        assertEquals(qtdAntes - 1, jogador.qtdCartas());
    }

    @Test
    @DisplayName("jogarMelhorCarta deve retornar a de maior força")
    void jogarMelhorCartaDeveRetornarMaisForte() {
        Carta melhor = jogador.jogarMelhorCarta();
        // 3 de Copas tem força 10, é a maior
        assertEquals("3", melhor.getValor());
        assertEquals(Naipe.COPAS, melhor.getNaipe());
        assertEquals(2, jogador.qtdCartas());
    }

    @Test
    @DisplayName("jogarPiorCarta deve retornar a de menor força")
    void jogarPiorCartaDeveRetornarMenorForca() {
        Carta pior = jogador.jogarPiorCarta();
        // K tem força 7, é a menor entre 3(10), 2(9), K(7)
        assertEquals("K", pior.getValor());
        assertEquals(2, jogador.qtdCartas());
    }

    @Test
    @DisplayName("Jogar índice inválido deve lançar IllegalArgumentException")
    void jogadorComIndiceInvalidoDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () -> jogador.jogarCarta(10));
        assertThrows(IllegalArgumentException.class, () -> jogador.jogarCarta(-1));
    }

    @Test
    @DisplayName("Jogar carta sem ter mão deve lançar IllegalStateException")
    void jogarSemCartasDeveLancarExcecao() {
        Jogador semCartas = new Jogador("Vazio", Equipe.TIME_B);
        assertThrows(IllegalStateException.class, () -> semCartas.jogarCarta(0));
        assertThrows(IllegalStateException.class, semCartas::jogarMelhorCarta);
        assertThrows(IllegalStateException.class, semCartas::jogarPiorCarta);
    }

    @Test
    @DisplayName("Mão deve ser imutável (retorna cópia)")
    void maoDeveSerImutavel() {
        List<Carta> mao = jogador.getMao();
        assertThrows(UnsupportedOperationException.class, () -> mao.remove(0));
    }

    @Test
    @DisplayName("Jogador deve pertencer à equipe correta")
    void jogadorDevePertencerAEquipeCorreta() {
        assertEquals(Equipe.TIME_A, jogador.getEquipe());
        assertEquals("Gaúcho", jogador.getNome());
    }

    @Test
    @DisplayName("Receber novas cartas deve substituir a mão anterior")
    void receberNovasCartasDeveSubstituirMaoAnterior() {
        jogador.receberCartas(List.of(new Carta("7", Naipe.PAUS)));
        assertEquals(1, jogador.qtdCartas());
    }

    @Test
    @DisplayName("Após jogar todas as cartas, temCartas deve ser false")
    void semCartasDeveRetornarFalse() {
        jogador.jogarCarta(0);
        jogador.jogarCarta(0);
        jogador.jogarCarta(0);
        assertFalse(jogador.temCartas());
        assertEquals(0, jogador.qtdCartas());
    }
}
