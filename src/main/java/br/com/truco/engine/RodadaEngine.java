package br.com.truco.engine;

import br.com.truco.model.Carta;
import br.com.truco.model.Jogador;
import br.com.truco.model.ResultadoRodada;

/**
 * Motor de resolução de rodada (vaza) do Truco Gaúcho.
 *
 * <p>Compara as cartas jogadas pelos dois jogadores e determina o vencedor,
 * aplicando as regras de hierarquia do Truco Gaúcho.
 */
public class RodadaEngine {

    /**
     * Resolve uma rodada comparando as cartas de dois jogadores.
     *
     * @param jogadorA primeiro jogador
     * @param cartaA   carta jogada pelo primeiro jogador
     * @param jogadorB segundo jogador
     * @param cartaB   carta jogada pelo segundo jogador
     * @return resultado da rodada
     */
    public ResultadoRodada resolverRodada(
            Jogador jogadorA, Carta cartaA,
            Jogador jogadorB, Carta cartaB) {

        int comparacao = cartaA.compareTo(cartaB);

        if (comparacao > 0) {
            return ResultadoRodada.vitoria(jogadorA, cartaA, cartaB);
        } else if (comparacao < 0) {
            return ResultadoRodada.vitoria(jogadorB, cartaB, cartaA);
        } else {
            return ResultadoRodada.empate(cartaA, cartaB);
        }
    }

    /**
     * Determina se uma carta bate outra segundo as regras gaúchas.
     * @return true se cartaA for mais forte que cartaB
     */
    public boolean cartaVence(Carta cartaA, Carta cartaB) {
        return cartaA.compareTo(cartaB) > 0;
    }
}
