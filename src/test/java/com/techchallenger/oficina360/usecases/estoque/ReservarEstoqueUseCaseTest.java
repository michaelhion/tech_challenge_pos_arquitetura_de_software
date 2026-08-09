package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ReservaEstoqueCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservarEstoqueUseCaseTest {

	private static final UUID ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String CODIGO = "FILTRO-DE-OLEO";

	private static final String NOME = "Filtro de óleo";

	private static final BigDecimal VALOR = new BigDecimal("45.90");

	private static final int QUANTIDADE_TOTAL = 20;

	private static final int RESERVADOS_INICIAIS = 5;

	private static final int QUANTIDADE_A_RESERVAR = 3;

	private static final String ITEM_NAO_ENCONTRADO = "Item de estoque não encontrado";

	@Mock
	private EstoqueFinder estoqueFinder;

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private ReservarEstoqueUseCase useCase;

	private Estoque estoque;

	@BeforeEach
	void setUp() {
		estoque = new Estoque(ESTOQUE_ID, NOME, VALOR, QUANTIDADE_TOTAL, RESERVADOS_INICIAIS, CODIGO);
	}

	@Test
	void deveReservarEstoqueComSucesso() {
		ReservaEstoqueCommand command = new ReservaEstoqueCommand(QUANTIDADE_A_RESERVAR);

		when(estoqueFinder.obterOuFalhar(CODIGO)).thenReturn(estoque);

		when(estoqueGateway.save(estoque)).thenReturn(estoque);

		EstoqueCommand resultado = useCase.reservar(CODIGO, command);

		assertNotNull(resultado);

		assertAll( () -> assertEquals(CODIGO, resultado.codigo()),
				() -> assertEquals(NOME, resultado.nome()), () -> assertEquals(0, VALOR.compareTo(resultado.valor())),
				() -> assertEquals(20, resultado.quantidade()), () -> assertEquals(8, resultado.reservados()),
				() -> assertEquals(12, resultado.disponiveis()));

		verify(estoqueFinder).obterOuFalhar(CODIGO);

		verify(estoqueGateway).save(estoque);

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	@Test
	void devePropagarExcecaoQuandoItemNaoForEncontrado() {
		ReservaEstoqueCommand command = new ReservaEstoqueCommand(QUANTIDADE_A_RESERVAR);

		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException(ITEM_NAO_ENCONTRADO);

		when(estoqueFinder.obterOuFalhar(CODIGO)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.reservar(CODIGO, command));

		assertSame(excecaoEsperada, excecaoObtida);

		assertEquals(ITEM_NAO_ENCONTRADO, excecaoObtida.getMessage());

		verify(estoqueFinder).obterOuFalhar(CODIGO);

		verify(estoqueGateway, never()).save(any(Estoque.class));

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	@Test
	void naoDeveSalvarQuandoQuantidadeForIndisponivel() {
		ReservaEstoqueCommand command = new ReservaEstoqueCommand(16);

		when(estoqueFinder.obterOuFalhar(CODIGO)).thenReturn(estoque);

		assertThrows(RuntimeException.class, () -> useCase.reservar(CODIGO, command));

		verify(estoqueFinder).obterOuFalhar(CODIGO);

		verify(estoqueGateway, never()).save(any(Estoque.class));

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}
}