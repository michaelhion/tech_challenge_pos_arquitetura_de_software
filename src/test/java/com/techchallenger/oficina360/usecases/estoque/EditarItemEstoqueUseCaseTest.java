package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EditarItemEstoqueUseCaseTest {

	private static final UUID ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String CODIGO_ATUAL = "FILTRO-DE-OLEO";

	private static final String NOME_ATUAL = "Filtro de óleo";

	private static final BigDecimal VALOR_ATUAL = new BigDecimal("45.90");

	private static final int QUANTIDADE_ATUAL = 20;

	private static final int RESERVADOS_ATUAL = 5;

	private static final String CODIGO_ATUALIZADO = "FILTRO-DE-OLEO-PREMIUM";

	private static final String NOME_ATUALIZADO = "Filtro de óleo premium";

	private static final BigDecimal VALOR_ATUALIZADO = new BigDecimal("60.00");

	private static final int QUANTIDADE_ATUALIZADA = 30;

	private static final int RESERVADOS_ATUALIZADOS = 10;

	private static final int DISPONIVEIS_ATUALIZADOS = QUANTIDADE_ATUALIZADA - RESERVADOS_ATUALIZADOS;

	private static final String ITEM_NAO_ENCONTRADO = "Item de estoque não encontrado";

	@Mock
	private EstoqueFinder estoqueFinder;

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private EditarItemEstoqueUseCase useCase;

	private Estoque estoque;
	private EstoqueCommand command;

	@BeforeEach
	void setUp() {
		estoque = new Estoque(ESTOQUE_ID, NOME_ATUAL, VALOR_ATUAL, QUANTIDADE_ATUAL, RESERVADOS_ATUAL, CODIGO_ATUAL);

		command = new EstoqueCommand(CODIGO_ATUALIZADO, NOME_ATUALIZADO, VALOR_ATUALIZADO,
				QUANTIDADE_ATUALIZADA, RESERVADOS_ATUALIZADOS, DISPONIVEIS_ATUALIZADOS);
	}

	@Test
	void deveEditarItemEstoqueComSucesso() {
		when(estoqueFinder.obterOuFalhar(CODIGO_ATUAL)).thenReturn(estoque);

		when(estoqueGateway.save(estoque)).thenAnswer(invocation -> invocation.getArgument(0));

		EstoqueCommand resultado = useCase.edit(CODIGO_ATUAL, command);

		assertNotNull(resultado);

		assertAll(
				() -> assertEquals(CODIGO_ATUALIZADO, resultado.codigo()),
				() -> assertEquals(NOME_ATUALIZADO, resultado.nome()),
				() -> assertEquals(0, VALOR_ATUALIZADO.compareTo(resultado.valor())),
				() -> assertEquals(QUANTIDADE_ATUALIZADA, resultado.quantidade()),
				() -> assertEquals(RESERVADOS_ATUALIZADOS, resultado.reservados()),
				() -> assertEquals(DISPONIVEIS_ATUALIZADOS, resultado.disponiveis()));

		verify(estoqueFinder, times(1)).obterOuFalhar(CODIGO_ATUAL);

		verify(estoqueGateway, times(1)).save(estoque);

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	@Test
	void deveEnviarItemAtualizadoParaPersistencia() {
		when(estoqueFinder.obterOuFalhar(CODIGO_ATUAL)).thenReturn(estoque);

		when(estoqueGateway.save(any(Estoque.class))).thenAnswer(invocation -> invocation.getArgument(0));

		useCase.edit(CODIGO_ATUAL, command);

		ArgumentCaptor<Estoque> captor = ArgumentCaptor.forClass(Estoque.class);

		verify(estoqueGateway).save(captor.capture());

		Estoque estoqueEnviado = captor.getValue();

		assertNotNull(estoqueEnviado);

		assertAll(() -> assertEquals(ESTOQUE_ID, estoqueEnviado.getId()),
				() -> assertEquals(CODIGO_ATUALIZADO, estoqueEnviado.getCodigo()),
				() -> assertEquals(NOME_ATUALIZADO, estoqueEnviado.getNome()),
				() -> assertEquals(0, VALOR_ATUALIZADO.compareTo(estoqueEnviado.getValor())),
				() -> assertEquals(QUANTIDADE_ATUALIZADA, estoqueEnviado.getQuantidade()),
				() -> assertEquals(RESERVADOS_ATUALIZADOS, estoqueEnviado.getReservados()),
				() -> assertEquals(DISPONIVEIS_ATUALIZADOS, estoqueEnviado.getDisponiveis()));

		verify(estoqueFinder).obterOuFalhar(CODIGO_ATUAL);

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	@Test
	void devePropagarExcecaoQuandoItemEstoqueNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException(ITEM_NAO_ENCONTRADO);

		when(estoqueFinder.obterOuFalhar(CODIGO_ATUAL)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.edit(CODIGO_ATUAL, command));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals(ITEM_NAO_ENCONTRADO, excecaoObtida.getMessage()));

		verify(estoqueFinder, times(1)).obterOuFalhar(CODIGO_ATUAL);

		verify(estoqueGateway, never()).save(any(Estoque.class));

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}
}