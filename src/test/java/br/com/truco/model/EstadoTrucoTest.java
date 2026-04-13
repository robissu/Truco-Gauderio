package br.com.truco.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EstadoTruco - Escalada de Apostas")
class EstadoTrucoTest {

    @Test
    @DisplayName("Estado inicial SEM_TRUCO deve valer 1 ponto")
    void semTrucoDeveValer1Ponto() {
        assertEquals(1, EstadoTruco.SEM_TRUCO.getPontos());
    }

    @Test
    @DisplayName("TRUCO deve valer 2 pontos")
    void trucoDeveValer2Pontos() {
        assertEquals(2, EstadoTruco.TRUCO.getPontos());
    }

    @Test
    @DisplayName("SEIS deve valer 6 pontos")
    void seisDeveValer6Pontos() {
        assertEquals(6, EstadoTruco.SEIS.getPontos());
    }

    @Test
    @DisplayName("NOVE deve valer 9 pontos")
    void noveDeveValer9Pontos() {
        assertEquals(9, EstadoTruco.NOVE.getPontos());
    }

    @Test
    @DisplayName("DOZE deve valer 12 pontos e ser máximo")
    void dozeDeveValer12PontosESerMaximo() {
        assertEquals(12, EstadoTruco.DOZE.getPontos());
        assertTrue(EstadoTruco.DOZE.isMaximo());
    }

    @Test
    @DisplayName("Somente DOZE deve ser marcado como máximo")
    void somenteDozeeMaximo() {
        assertFalse(EstadoTruco.SEM_TRUCO.isMaximo());
        assertFalse(EstadoTruco.TRUCO.isMaximo());
        assertFalse(EstadoTruco.SEIS.isMaximo());
        assertFalse(EstadoTruco.NOVE.isMaximo());
        assertTrue(EstadoTruco.DOZE.isMaximo());
    }

    @Test
    @DisplayName("Escalada completa: SEM_TRUCO → TRUCO → SEIS → NOVE → DOZE")
    void escaladaCompletaDevePercorrerTodosOsNiveis() {
        EstadoTruco estado = EstadoTruco.SEM_TRUCO;

        estado = estado.proximoNivel();
        assertEquals(EstadoTruco.TRUCO, estado);

        estado = estado.proximoNivel();
        assertEquals(EstadoTruco.SEIS, estado);

        estado = estado.proximoNivel();
        assertEquals(EstadoTruco.NOVE, estado);

        estado = estado.proximoNivel();
        assertEquals(EstadoTruco.DOZE, estado);
    }

    @Test
    @DisplayName("DOZE.proximoNivel() deve retornar null")
    void dozeProximoNivelDeveRetornarNull() {
        assertNull(EstadoTruco.DOZE.proximoNivel());
    }

    @ParameterizedTest
    @DisplayName("Todos os estados devem ter descrição não-nula")
    @EnumSource(EstadoTruco.class)
    void todosEstadosDevemTerDescricao(EstadoTruco estado) {
        assertNotNull(estado.getDescricao());
        assertFalse(estado.getDescricao().isBlank());
    }

    @ParameterizedTest
    @DisplayName("Todos os estados devem ter pontos positivos")
    @EnumSource(EstadoTruco.class)
    void todosEstadosDevemTerPontosPositivos(EstadoTruco estado) {
        assertTrue(estado.getPontos() > 0,
            estado + " deve ter pontos > 0");
    }
}
