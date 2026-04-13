package br.com.truco.service;

import br.com.truco.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DistribuidorService - Distribuição de Cartas")
class DistribuidorServiceTest {

    private DistribuidorService distribuidor;
    private Baralho baralho;
    private Jogador jogadorA;
    private Jogador jogadorB;

    @BeforeEach
    void setUp() {
        distribuidor = new DistribuidorService();
        baralho      = new Baralho();
        jogadorA     = new Jogador("Gaúcho", Equipe.TIME_A);
        jogadorB     = new Jogador("Peão",   Equipe.TIME_B);
    }

    @Test
    @DisplayName("Distribuir deve dar 3 cartas para cada jogador")
    void distribuirDeveDar3CartasPorJogador() {
        distribuidor.distribuirMao(baralho, jogadorA, jogadorB);
        assertEquals(3, jogadorA.qtdCartas());
        assertEquals(3, jogadorB.qtdCartas());
    }

    @Test
    @DisplayName("Distribuir deve remover 6 cartas do baralho")
    void distribuirDeveRemover6CartasDoBaralho() {
        distribuidor.distribuirMao(baralho, jogadorA, jogadorB);
        assertEquals(34, baralho.tamanho(), "Devem restar 34 cartas (40 - 6)");
    }

    @Test
    @DisplayName("novaRodada deve reiniciar baralho e distribuir cartas")
    void novaRodadaDeveReiniciarEDistribuir() {
        // Esvazia parte do baralho
        baralho.distribuir(30);
        assertEquals(10, baralho.tamanho());

        distribuidor.novaRodada(baralho, jogadorA, jogadorB);

        assertEquals(34, baralho.tamanho(), "Baralho deve ter 34 após nova rodada");
        assertEquals(3, jogadorA.qtdCartas());
        assertEquals(3, jogadorB.qtdCartas());
    }

    @Test
    @DisplayName("Jogadores não devem receber a mesma carta")
    void jogadoresNaoDevemReceberMesmaCarta() {
        distribuidor.distribuirMao(baralho, jogadorA, jogadorB);

        for (Carta ca : jogadorA.getMao()) {
            for (Carta cb : jogadorB.getMao()) {
                assertNotEquals(ca, cb,
                    "Jogadores não podem ter a mesma carta: " + ca);
            }
        }
    }

    @Test
    @DisplayName("Distribuir para múltiplos jogadores deve funcionar")
    void distribuirParaMultiplosJogadores() {
        Jogador jogadorC = new Jogador("Chimango", Equipe.TIME_A);
        Jogador jogadorD = new Jogador("Gringo",   Equipe.TIME_B);

        distribuidor.distribuirMao(baralho, jogadorA, jogadorB, jogadorC, jogadorD);

        assertEquals(3, jogadorA.qtdCartas());
        assertEquals(3, jogadorB.qtdCartas());
        assertEquals(3, jogadorC.qtdCartas());
        assertEquals(3, jogadorD.qtdCartas());
        assertEquals(28, baralho.tamanho()); // 40 - 12
    }
}
