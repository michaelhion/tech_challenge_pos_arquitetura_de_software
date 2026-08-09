package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarItemEstoqueUseCaseTest {

	private static final UUID ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String CODIGO = "FILTRO-DE-OLEO";

	private static final String NOME = "Filtro de óleo";

	private static final BigDecimal VALOR = new BigDecimal("45.90");

	private static final int QUANTIDADE = 20;

	private static final String MENSAGEM_ITEM_NAO_ENCONTRADO = "Item de estoque não encontrado";

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private BuscarItemEstoqueUseCase useCase;

	@Test
	void deveBuscarItemEstoquePorCodigoComSucesso() {
		Estoque estoque = criarEstoque();

		when(estoqueGateway.findByCodigo(CODIGO)).thenReturn(Optional.of(estoque));

		EstoqueCommand resultado = useCase.findByCodigo(CODIGO);

		assertNotNull(resultado);

		assertAll(
				() -> assertEquals(CODIGO, resultado.codigo()),
				() -> assertEquals(NOME, resultado.nome()),
				() -> assertEquals(0, VALOR.compareTo(resultado.valor())),
				() -> assertEquals(QUANTIDADE, resultado.quantidade()));

		verify(estoqueGateway, times(1)).findByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void deveLancarExcecaoQuandoItemEstoqueNaoForEncontrado() {
		when(estoqueGateway.findByCodigo(CODIGO)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.findByCodigo(CODIGO));

		assertEquals(MENSAGEM_ITEM_NAO_ENCONTRADO, exception.getMessage());

		verify(estoqueGateway, times(1)).findByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueGateway);
	}

	private Estoque criarEstoque() {
		return new Estoque(ESTOQUE_ID,  NOME, VALOR, QUANTIDADE, 5,CODIGO);
	}
}