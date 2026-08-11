package com.techchallenger.oficina360.services.validators;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.DIAGNOSTICO_INVALIDO;
import static org.junit.jupiter.api.Assertions.*;

class OrdemServicoEstoqueValidatorTest {

	private final OrdemServicoEstoqueValidator validator = new OrdemServicoEstoqueValidator();

	@Test
	void deveIgnorarQuandoMapaSolicitadoForNulo() {

		assertDoesNotThrow(() -> validator.validar(null, Map.of()));
	}

	@Test
	void deveIgnorarQuandoMapaSolicitadoEstiverVazio() {

		assertDoesNotThrow(() -> validator.validar(Map.of(), Map.of()));
	}

	@Test
	void deveValidarComSucessoQuandoTodosItensExistiremEPossuiremEstoque() {

		Estoque estoque = criarEstoque(2); // disponíveis = 8

		Map<String, Integer> solicitado = Map.of("OLEO01", 5);

		Map<String, Estoque> banco = Map.of("OLEO01", estoque);

		assertDoesNotThrow(() -> validator.validar(solicitado, banco));
	}

	@Test
	void deveLancarExcecaoQuandoItemNaoForEncontrado() {

		Map<String, Integer> solicitado = Map.of("OLEO01", 2);

		Map<String, Estoque> banco = Map.of();

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
				() -> validator.validar(solicitado, banco));

		assertEquals(DIAGNOSTICO_INVALIDO, exception.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoEstoqueForInsuficiente() {

		Estoque estoque = criarEstoque(8); // disponíveis = 2

		Map<String, Integer> solicitado = Map.of("OLEO01", 5);

		Map<String, Estoque> banco = Map.of("OLEO01", estoque);

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
				() -> validator.validar(solicitado, banco));

		assertEquals(DIAGNOSTICO_INVALIDO, exception.getMessage());
	}

	@Test
	void devePermitirQuandoQuantidadeSolicitadaForIgualAoDisponivel() {

		Estoque estoque = criarEstoque(5); // disponíveis = 5

		Map<String, Integer> solicitado = Map.of("OLEO01", 5);

		Map<String, Estoque> banco = Map.of("OLEO01", estoque);

		assertDoesNotThrow(() -> validator.validar(solicitado, banco));
	}

	private Estoque criarEstoque(int reservados) {

		return new Estoque(UUID.randomUUID(), "Óleo Motor", BigDecimal.TEN, 10, reservados, "OLEO01");
	}
}