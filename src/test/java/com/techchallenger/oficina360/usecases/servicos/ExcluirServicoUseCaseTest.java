package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.SERVICO_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirServicoUseCaseTest {

	private static final String CODIGO = "TROCA-DE-OLEO";

	@Mock
	private ServicoGateway servicoGateway;

	@Mock
	private Servico servico;

	@InjectMocks
	private ExcluirServicoUseCase useCase;

	@Test
	void deveExcluirServicoComSucesso() {
		when(servicoGateway.findByCodigo(CODIGO)).thenReturn(Optional.of(servico));

		assertDoesNotThrow(() -> useCase.delete(CODIGO));

		verify(servicoGateway).findByCodigo(CODIGO);

		verify(servicoGateway).deleteByCodigo(CODIGO);

		verifyNoMoreInteractions(servicoGateway);
	}

	@Test
	void deveBuscarServicoAntesDeExcluir() {
		when(servicoGateway.findByCodigo(CODIGO)).thenReturn(Optional.of(servico));

		useCase.delete(CODIGO);

		InOrder ordemDasChamadas = inOrder(servicoGateway);

		ordemDasChamadas.verify(servicoGateway).findByCodigo(CODIGO);

		ordemDasChamadas.verify(servicoGateway).deleteByCodigo(CODIGO);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void deveLancarExcecaoQuandoServicoNaoForEncontrado() {
		when(servicoGateway.findByCodigo(CODIGO)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException excecao = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.delete(CODIGO));

		assertEquals(SERVICO_NAO_ENCONTRADO, excecao.getMessage());

		verify(servicoGateway).findByCodigo(CODIGO);

		verify(servicoGateway, never()).deleteByCodigo(CODIGO);

		verifyNoMoreInteractions(servicoGateway);
	}

	@Test
	void devePropagarAExcecaoDeRecursoNaoEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO);

		when(servicoGateway.findByCodigo(CODIGO)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.delete(CODIGO));

		assertSame(excecaoEsperada, excecaoObtida);

		verify(servicoGateway).findByCodigo(CODIGO);

		verify(servicoGateway, never()).deleteByCodigo(CODIGO);

		verifyNoMoreInteractions(servicoGateway);
	}
}