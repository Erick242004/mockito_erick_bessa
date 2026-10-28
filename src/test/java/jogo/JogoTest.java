package jogo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class JogoTest {

	private Jogador jogador;
	private Dado dado1;
	private Dado dado2;
	private Jogo jogo;

	@BeforeEach
	void setUp() {
		// Dado e Jogador são dependências externas: substituídas por mocks
		jogador = mock(Jogador.class);
		dado1 = mock(Dado.class);
		dado2 = mock(Dado.class);
		jogo = new Jogo(jogador, dado1, dado2);
	}

	// ---------- Primeiro turno ----------

	@ParameterizedTest
	@ValueSource(ints = { 7, 11 })
	void ganhaNoPrimeiroTurnoComSeteOuOnze(int soma) {
		when(jogador.lancar(dado1, dado2)).thenReturn(soma);

		assertTrue(jogo.jogo());
		verify(jogador, times(1)).lancar(dado1, dado2);
	}

	@ParameterizedTest
	@ValueSource(ints = { 2, 3, 12 })
	void perdeNoPrimeiroTurnoComDoisTresOuDoze(int soma) {
		when(jogador.lancar(dado1, dado2)).thenReturn(soma);

		assertFalse(jogo.jogo());
		verify(jogador, times(1)).lancar(dado1, dado2);
	}

	// ---------- Segundo turno ----------

	@ParameterizedTest
	@ValueSource(ints = { 4, 5, 6, 8, 9, 10 })
	void ganhaNoSegundoTurnoQuandoRepeteOPonto(int ponto) {
		when(jogador.lancar(dado1, dado2)).thenReturn(ponto, ponto);

		assertTrue(jogo.jogo());
		verify(jogador, times(2)).lancar(dado1, dado2);
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 5, 6, 8, 9, 10 })
	void perdeNoSegundoTurnoQuandoSaiSete(int ponto) {
		when(jogador.lancar(dado1, dado2)).thenReturn(ponto, 7);

		assertFalse(jogo.jogo());
		verify(jogador, times(2)).lancar(dado1, dado2);
	}

	@Test
	void continuaJogandoAteRepetirOPontoEGanha() {
		// ponto = 6; 3, 9, 2, 12 e 11 não encerram o jogo no segundo turno
		when(jogador.lancar(dado1, dado2)).thenReturn(6, 3, 9, 2, 12, 11, 6);

		assertTrue(jogo.jogo());
		verify(jogador, times(7)).lancar(dado1, dado2);
	}

	@Test
	void continuaJogandoAteSairSeteEPerde() {
		// ponto = 8; 5, 2, 12 e 11 não encerram o jogo no segundo turno
		when(jogador.lancar(dado1, dado2)).thenReturn(8, 5, 2, 12, 11, 7);

		assertFalse(jogo.jogo());
		verify(jogador, times(6)).lancar(dado1, dado2);
	}
}
