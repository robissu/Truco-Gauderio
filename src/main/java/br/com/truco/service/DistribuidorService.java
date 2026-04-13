package br.com.truco.service;

import br.com.truco.model.Baralho;
import br.com.truco.model.Jogador;

/**
 * Serviço responsável por distribuir cartas para os jogadores.
 */
public class DistribuidorService {

    private static final int CARTAS_POR_JOGADOR = 3;

    /**
     * Distribui 3 cartas para cada jogador a partir do baralho.
     * O baralho deve estar embaralhado antes de chamar este método.
     */
    public void distribuirMao(Baralho baralho, Jogador... jogadores) {
        for (Jogador jogador : jogadores) {
            jogador.receberCartas(baralho.distribuir(CARTAS_POR_JOGADOR));
        }
    }

    /**
     * Reinicia o baralho e distribui para todos os jogadores.
     */
    public void novaRodada(Baralho baralho, Jogador... jogadores) {
        baralho.reiniciar();
        distribuirMao(baralho, jogadores);
    }
}
