package br.com.truco.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa o baralho do Truco Gaúcho.
 * Usa 40 cartas (sem 8, 9 e 10).
 */
public class Baralho {

    private static final String[] VALORES = {"A", "2", "3", "4", "5", "6", "7", "J", "Q", "K"};

    private final List<Carta> cartas;

    public Baralho() {
        this.cartas = new ArrayList<>();
        inicializar();
    }

    private void inicializar() {
        cartas.clear();
        for (Naipe naipe : Naipe.values()) {
            for (String valor : VALORES) {
                cartas.add(new Carta(valor, naipe));
            }
        }
    }

    public void embaralhar() {
        Collections.shuffle(cartas);
    }

    /**
     * Distribui mão de cartas para um jogador.
     * @param quantidade número de cartas a distribuir
     * @return lista de cartas removidas do topo do baralho
     * @throws IllegalStateException se não houver cartas suficientes
     */
    public List<Carta> distribuir(int quantidade) {
        if (cartas.size() < quantidade) {
            throw new IllegalStateException(
                "Cartas insuficientes no baralho. Restam: " + cartas.size()
            );
        }
        List<Carta> mao = new ArrayList<>();
        for (int i = 0; i < quantidade; i++) {
            mao.add(cartas.remove(0));
        }
        return mao;
    }

    public int tamanho()       { return cartas.size(); }
    public boolean isEmpty()   { return cartas.isEmpty(); }

    /** Reinicia e embaralha o baralho (para nova rodada). */
    public void reiniciar() {
        inicializar();
        embaralhar();
    }

    @Override
    public String toString() {
        return "Baralho{cartas=" + cartas.size() + "}";
    }
}
