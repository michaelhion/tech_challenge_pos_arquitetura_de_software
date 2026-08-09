package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarItemEstoqueUseCaseTest {

	private static final String CODIGO = "FILTRO-DE-OLEO";

	private static final String NOME = "Filtro de óleo";

	private static final BigDecimal VALOR = new BigDecimal("45.90");

	private static final int QUANTIDADE = 20;

	private static final int RESERVADOS = 0;

	private static final int DISPONIVEIS = 20;

	private static final UUID ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	@Mock
	private EstoqueFinder estoqueFinder;

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private CriarItemEstoqueUseCase useCase;

	private EstoqueCommand command;

	@BeforeEach
	void setUp() {
		command = new EstoqueCommand(CODIGO,  NOME, VALOR, QUANTIDADE, RESERVADOS, DISPONIVEIS);
	}

	@Test
	void deveCriarItemEstoqueComSucesso() {
		Estoque estoqueSalvo = criarEstoqueSalvo();

		when(estoqueFinder.obter(CODIGO)).thenReturn(Optional.empty());

		when(estoqueGateway.save(any(Estoque.class))).thenReturn(estoqueSalvo);

		EstoqueCommand resultado = useCase.save(command);

		assertNotNull(resultado);

		assertAll(
				() -> assertEquals(CODIGO, resultado.codigo()),
				() -> assertEquals(NOME, resultado.nome()),
				() -> assertEquals(0, VALOR.compareTo(resultado.valor())),
				() -> assertEquals(QUANTIDADE, resultado.quantidade()),
				() -> assertEquals(RESERVADOS, resultado.reservados()),
				() -> assertEquals(DISPONIVEIS, resultado.disponiveis()));

		verify(estoqueFinder, times(1)).obter(CODIGO);

		verify(estoqueGateway, times(1)).save(any(Estoque.class));

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	@Test
	void deveEnviarItemCorretoParaPersistencia() {
		Estoque estoqueSalvo = criarEstoqueSalvo();

		when(estoqueFinder.obter(CODIGO)).thenReturn(Optional.empty());

		when(estoqueGateway.save(any(Estoque.class))).thenReturn(estoqueSalvo);

		useCase.save(command);

		ArgumentCaptor<Estoque> estoqueCaptor = ArgumentCaptor.forClass(Estoque.class);

		verify(estoqueGateway).save(estoqueCaptor.capture());

		Estoque estoqueEnviado = estoqueCaptor.getValue();

		assertNotNull(estoqueEnviado);

		assertAll(() -> assertEquals(CODIGO, estoqueEnviado.getCodigo()),
				() -> assertEquals(NOME, estoqueEnviado.getNome()),
				() -> assertEquals(0, VALOR.compareTo(estoqueEnviado.getValor())),
				() -> assertEquals(QUANTIDADE, estoqueEnviado.getQuantidade()));

		verify(estoqueFinder).obter(CODIGO);

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	@Test
	void deveLancarExcecaoQuandoCodigoJaExistir() {
		Estoque estoqueExistente = criarEstoqueSalvo();

		when(estoqueFinder.obter(CODIGO)).thenReturn(Optional.of(estoqueExistente));

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class, () -> useCase.save(command));

		assertEquals(ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA, exception.getMessage());

		verify(estoqueFinder, times(1)).obter(CODIGO);

		verify(estoqueGateway, never()).save(any(Estoque.class));

		verifyNoMoreInteractions(estoqueFinder, estoqueGateway);
	}

	private Estoque criarEstoqueSalvo() {
		return new Estoque(ESTOQUE_ID, NOME, VALOR, QUANTIDADE, RESERVADOS,CODIGO);
	}
}