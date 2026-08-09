package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirItemEstoqueUseCaseTest {

	private static final UUID ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String CODIGO = "FILTRO-DE-OLEO";

	private static final String NOME = "Filtro de óleo";

	private static final BigDecimal VALOR = new BigDecimal("45.90");

	private static final int QUANTIDADE = 20;

	private static final int RESERVADOS = 5;

	private static final String ITEM_NAO_ENCONTRADO = "Item de estoque não encontrado";

	@Mock
	private EstoqueFinder estoqueFinder;

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private ExcluirItemEstoqueUseCase useCase;

	@Test
	void deveExcluirItemEstoqueComSucesso() {
		Estoque estoque = criarEstoque();

		when(estoqueFinder.obterOuFalhar(CODIGO)).thenReturn(estoque);

		assertDoesNotThrow(() -> useCase.delete(CODIGO));

		verify(estoqueFinder).obterOuFalhar(CODIGO);

		verify(estoqueGateway).deleteByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	@Test
	void deveVerificarExistenciaAntesDeExcluirItemEstoque() {
		Estoque estoque = criarEstoque();

		when(estoqueFinder.obterOuFalhar(CODIGO)).thenReturn(estoque);

		useCase.delete(CODIGO);

		InOrder ordemDasChamadas = inOrder(estoqueFinder, estoqueGateway);

		ordemDasChamadas.verify(estoqueFinder).obterOuFalhar(CODIGO);

		ordemDasChamadas.verify(estoqueGateway).deleteByCodigo(CODIGO);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void devePropagarExcecaoQuandoItemEstoqueNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException(ITEM_NAO_ENCONTRADO);

		when(estoqueFinder.obterOuFalhar(CODIGO)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.delete(CODIGO));

		assertSame(excecaoEsperada, excecaoObtida);

		assertEquals(ITEM_NAO_ENCONTRADO, excecaoObtida.getMessage());

		verify(estoqueFinder).obterOuFalhar(CODIGO);

		verify(estoqueGateway, never()).deleteByCodigo(CODIGO);

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	private Estoque criarEstoque() {
		return new Estoque(ESTOQUE_ID, NOME, VALOR, QUANTIDADE, RESERVADOS, CODIGO);
	}
}