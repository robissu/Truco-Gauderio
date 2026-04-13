package br.com.truco.service;

import br.com.truco.model.Equipe;
import br.com.truco.model.Mao;
import br.com.truco.model.Partida;

/**
 * Serviço de gestão de placar da partida.
 */
public class PlacarService {

    private final Partida partida;

    public PlacarService() {
        this.partida = new Partida();
    }

    public PlacarService(Partida partida) {
        this.partida = partida;
    }

    /**
     * Registra o resultado de uma mão no placar.
     */
    public void registrarMao(Mao mao) {
        partida.registrarResultadoMao(mao);
    }

    /**
     * Verifica se a partida está encerrada.
     */
    public boolean isPartidaEncerrada() {
        return partida.isEncerrada();
    }

    public Equipe getVencedorPartida() {
        return partida.getVencedor();
    }

    public int getPontosA() { return partida.getPontosA(); }
    public int getPontosB() { return partida.getPontosB(); }

    public String getPlacar() {
        return String.format("%s %d x %d %s",
            Equipe.TIME_A.getNome(), partida.getPontosA(),
            partida.getPontosB(), Equipe.TIME_B.getNome());
    }

    public Partida getPartida() { return partida; }
}
