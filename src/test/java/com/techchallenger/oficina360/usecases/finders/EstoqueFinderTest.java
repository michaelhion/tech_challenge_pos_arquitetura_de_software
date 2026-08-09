package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueFinderTest {

	private static final UUID ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String CODIGO = "FILTRO-DE-OLEO";

	private static final String NOME = "Filtro de óleo";

	private static final BigDecimal VALOR = new BigDecimal("45.90");

	private static final int QUANTIDADE = 20;

	private static final int RESERVADOS = 5;

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private EstoqueFinder estoqueFinder;

	private Estoque estoque;

	@BeforeEach
	void setUp() {
		estoque = new Estoque(ESTOQUE_ID, NOME, VALOR, QUANTIDADE, RESERVADOS, CODIGO);
	}

	@Test
	void deveObterItemEstoquePorCodigoComSucesso() {
		when(estoqueGateway.findByCodigo(CODIGO)).thenReturn(Optional.of(estoque));

		Estoque resultado = estoqueFinder.obterOuFalhar(CODIGO);

		assertSame(estoque, resultado);

		assertEquals(ESTOQUE_ID, resultado.getId());

		assertEquals(CODIGO, resultado.getCodigo());

		verify(estoqueGateway).findByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void deveLancarExcecaoQuandoItemEstoqueNaoForEncontrado() {
		when(estoqueGateway.findByCodigo(CODIGO)).thenReturn(Optional.empty());

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
				() -> estoqueFinder.obterOuFalhar(CODIGO));

		assertEquals(ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA, exception.getMessage());

		verify(estoqueGateway).findByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void deveRetornarOptionalPreenchidoQuandoItemExistir() {
		when(estoqueGateway.findByCodigo(CODIGO)).thenReturn(Optional.of(estoque));

		Optional<Estoque> resultado = estoqueFinder.obter(CODIGO);

		assertTrue(resultado.isPresent());

		assertSame(estoque, resultado.orElseThrow());

		verify(estoqueGateway).findByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void deveRetornarOptionalVazioQuandoItemNaoExistir() {
		when(estoqueGateway.findByCodigo(CODIGO)).thenReturn(Optional.empty());

		Optional<Estoque> resultado = estoqueFinder.obter(CODIGO);

		assertFalse(resultado.isPresent());

		verify(estoqueGateway).findByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void deveEncaminharCodigoRecebidoAoGateway() {
		String outroCodigo = "PASTILHA-DE-FREIO";

		when(estoqueGateway.findByCodigo(outroCodigo)).thenReturn(Optional.empty());

		Optional<Estoque> resultado = estoqueFinder.obter(outroCodigo);

		assertTrue(resultado.isEmpty());

		verify(estoqueGateway).findByCodigo(outroCodigo);

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void deveEncaminharCodigoNuloAoGateway() {
		when(estoqueGateway.findByCodigo(null)).thenReturn(Optional.empty());

		Optional<Estoque> resultado = estoqueFinder.obter(null);

		assertTrue(resultado.isEmpty());

		verify(estoqueGateway).findByCodigo(null);

		verifyNoMoreInteractions(estoqueGateway);
	}
}