package com.techchallenger.oficina360.dominio;

import com.techchallenger.oficina360.dominio.shared.exception.ItemEstoqueInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EstoqueTest {

	private static final UUID ID = UUID.fromString("2cabf85b-31bc-4ad7-b35d-d22beeb05969");

	private static final String NOME = "Óleo 5W30";
	private static final String CODIGO = "OLEO-5W30";
	private static final BigDecimal VALOR = new BigDecimal("49.90");

	private Estoque estoque;

	@BeforeEach
	void setUp() {
		estoque = criarEstoque(10, 2);
	}

	@Nested
	class Disponibilidade {

		@Test
		void deveCalcularQuantidadeDisponivel() {
			assertEquals(8, estoque.getDisponiveis());
		}

		@Test
		void deveConsiderarQuantidadeNulaComoZero() {
			Estoque estoqueComQuantidadeNula = criarEstoque(null, 0);

			assertEquals(0, estoqueComQuantidadeNula.getDisponiveis());
		}

		@Test
		void deveConsiderarReservadosNuloComoZero() {
			Estoque estoqueComReservadosNulo = criarEstoque(10, null);

			assertEquals(10, estoqueComReservadosNulo.getDisponiveis());
		}

		@ParameterizedTest
		@NullSource
		@ValueSource(ints = {0, -1, -10})
		void deveFalharQuandoQuantidadeParaReservaNaoForPositiva(
				Integer quantidadeSolicitada
		) {
			Estoque estoque = criarEstoque(10, 2);

			assertThrows(
					ItemEstoqueInvalidoException.class,
					() -> estoque.reservar(quantidadeSolicitada)
			);

			assertAll(
					() -> assertEquals(2, estoque.getReservados()),
					() -> assertEquals(8, estoque.getDisponiveis())
			);
		}

		@Test
		void deveNormalizarQuantidadeEReservadosNulosParaZero() {
			Estoque estoque = criarEstoque(null, null);

			assertAll(
					() -> assertEquals(
							0,
							estoque.getQuantidade()
					),
					() -> assertEquals(
							0,
							estoque.getReservados()
					),
					() -> assertEquals(
							0,
							estoque.getDisponiveis()
					)
			);
		}

		@Test
		void deveReservarQuandoReservadosForNulo() {
			Estoque estoque = criarEstoque(10, null);

			assertDoesNotThrow(() -> estoque.reservar(2));

			assertAll(
					() -> assertEquals(
							2,
							estoque.getReservados()
					),
					() -> assertEquals(
							8,
							estoque.getDisponiveis()
					)
			);
		}

		@Test
		void devePermitirCriarItemComEstoqueZerado() {
			Estoque estoque = criarEstoque(0, 0);

			assertAll(
					() -> assertEquals(0, estoque.getQuantidade()),
					() -> assertEquals(0, estoque.getReservados()),
					() -> assertEquals(0, estoque.getDisponiveis())
			);
		}
	}

	@Nested
	class Reserva {

		@Test
		void deveReservarQuantidadeDisponivel() {
			assertDoesNotThrow(() -> estoque.reservar(3));

			assertAll(() -> assertEquals(5, estoque.getReservados()), () -> assertEquals(5, estoque.getDisponiveis()),
					() -> assertEquals(10, estoque.getQuantidade()));
		}

		@Test
		void devePermitirReservarExatamenteTodaQuantidadeDisponivel() {
			estoque.reservar(8);

			assertAll(() -> assertEquals(10, estoque.getReservados()), () -> assertEquals(0, estoque.getDisponiveis()));
		}

		@Test
		void deveAcumularReservasSucessivas() {
			estoque.reservar(2);
			estoque.reservar(3);

			assertAll(() -> assertEquals(7, estoque.getReservados()), () -> assertEquals(3, estoque.getDisponiveis()));
		}

		@ParameterizedTest
		@NullSource
		@ValueSource(ints = { 0, -1, -10 })
		void deveFalharQuandoQuantidadeParaReservaForInvalida(Integer quantidade) {
			assertThrows(ItemEstoqueInvalidoException.class, () -> estoque.reservar(quantidade));

			assertAll(() -> assertEquals(2, estoque.getReservados()), () -> assertEquals(8, estoque.getDisponiveis()));
		}

		@Test
		void deveFalharQuandoQuantidadeSolicitadaForMaiorQueDisponivel() {
			assertThrows(ItemEstoqueInvalidoException.class, () -> estoque.reservar(9));

			assertAll(() -> assertEquals(2, estoque.getReservados()), () -> assertEquals(8, estoque.getDisponiveis()));
		}

		@Test
		void deveFalharAoReservarQuandoNaoHouverDisponibilidade() {
			Estoque estoqueSemDisponibilidade = criarEstoque(10, 10);

			assertThrows(ItemEstoqueInvalidoException.class, () -> estoqueSemDisponibilidade.reservar(1));

			assertAll(() -> assertEquals(10, estoqueSemDisponibilidade.getReservados()),
					() -> assertEquals(0, estoqueSemDisponibilidade.getDisponiveis()));
		}

		@Test
		void deveReservarQuandoReservadosForNulo() {
			Estoque estoqueComReservadosNulo = criarEstoque(10, null);

			assertDoesNotThrow(() -> estoqueComReservadosNulo.reservar(2));

			assertAll(() -> assertEquals(2, estoqueComReservadosNulo.getReservados()),
					() -> assertEquals(8, estoqueComReservadosNulo.getDisponiveis()));
		}

		@Test
		void naoDeveAlterarEstadoQuandoReservaFalhar() {
			Integer reservadosAntes = estoque.getReservados();
			Integer disponiveisAntes = estoque.getDisponiveis();

			assertThrows(ItemEstoqueInvalidoException.class, () -> estoque.reservar(100));

			assertAll(() -> assertEquals(reservadosAntes, estoque.getReservados()),
					() -> assertEquals(disponiveisAntes, estoque.getDisponiveis()));
		}

		@Test
		void deveLiberarReserva() {
			Estoque estoque =
					criarEstoque(10, 4);

			estoque.liberarReserva(3);

			assertAll(
					() -> assertEquals(1, estoque.getReservados()),
					() -> assertEquals(9, estoque.getDisponiveis()),
					() -> assertEquals(10, estoque.getQuantidade())
			);
		}

		@Test
		void deveConsumirReserva() {
			Estoque estoque =
					criarEstoque(10, 4);

			estoque.consumirReserva(4);

			assertAll(
					() -> assertEquals(6, estoque.getQuantidade()),
					() -> assertEquals(0, estoque.getReservados()),
					() -> assertEquals(6, estoque.getDisponiveis())
			);
		}

		@Test
		void deveFalharAoLiberarMaisQueOReservado() {
			Estoque estoque =
					criarEstoque(10, 2);

			assertThrows(
					ItemEstoqueInvalidoException.class,
					() -> estoque.liberarReserva(3)
			);

			assertAll(
					() -> assertEquals(10, estoque.getQuantidade()),
					() -> assertEquals(2, estoque.getReservados()),
					() -> assertEquals(8, estoque.getDisponiveis())
			);
		}

		@Test
		void deveFalharAoConsumirMaisQueOReservado() {
			Estoque estoque =
					criarEstoque(10, 2);

			assertThrows(
					ItemEstoqueInvalidoException.class,
					() -> estoque.consumirReserva(3)
			);

			assertAll(
					() -> assertEquals(10, estoque.getQuantidade()),
					() -> assertEquals(2, estoque.getReservados())
			);
		}
	}

	@Nested
	class Construcao {

		@Test
		void deveManterOsDadosInformados() {
			assertAll(() -> assertEquals(ID, estoque.getId()), () -> assertEquals(NOME, estoque.getNome()),
					() -> assertEquals(VALOR, estoque.getValor()), () -> assertEquals(10, estoque.getQuantidade()),
					() -> assertEquals(2, estoque.getReservados()), () -> assertEquals(CODIGO, estoque.getCodigo()));
		}
	}

	private Estoque criarEstoque(Integer quantidade, Integer reservados) {
		return new Estoque(ID, NOME, VALOR, quantidade, reservados, CODIGO);
	}
}