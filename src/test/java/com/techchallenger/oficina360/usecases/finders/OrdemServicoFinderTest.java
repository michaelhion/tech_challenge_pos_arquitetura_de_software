package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoFinderTest {

	private static final UUID ORDEM_SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	@Mock
	private OrdemServicoGateway ordemServicoGateway;

	@Mock
	private OrdemServico ordemServico;

	@InjectMocks
	private OrdemServicoFinder ordemServicoFinder;

	@Test
	void deveRetornarOrdemServicoQuandoEncontrada() {
		when(ordemServicoGateway.findById(ORDEM_SERVICO_ID)).thenReturn(Optional.of(ordemServico));

		OrdemServico resultado = ordemServicoFinder.obterOuFalhar(ORDEM_SERVICO_ID);

		assertSame(ordemServico, resultado);

		verify(ordemServicoGateway).findById(ORDEM_SERVICO_ID);

		verifyNoMoreInteractions(ordemServicoGateway);
	}

	@Test
	void deveLancarExcecaoQuandoOrdemServicoNaoForEncontrada() {
		when(ordemServicoGateway.findById(ORDEM_SERVICO_ID)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> ordemServicoFinder.obterOuFalhar(ORDEM_SERVICO_ID));

		assertEquals(OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA, exception.getMessage());

		verify(ordemServicoGateway).findById(ORDEM_SERVICO_ID);

		verifyNoMoreInteractions(ordemServicoGateway);
	}

	@Test
	void deveEncaminharIdentificadorRecebidoAoGateway() {
		UUID outroId = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

		when(ordemServicoGateway.findById(outroId)).thenReturn(Optional.of(ordemServico));

		OrdemServico resultado = ordemServicoFinder.obterOuFalhar(outroId);

		assertSame(ordemServico, resultado);

		verify(ordemServicoGateway).findById(outroId);

		verifyNoMoreInteractions(ordemServicoGateway);
	}

	@Test
	void devePropagarExcecaoLancadaPeloGateway() {
		RuntimeException excecaoEsperada = new RuntimeException("Falha ao consultar a Ordem de Serviço");

		when(ordemServicoGateway.findById(ORDEM_SERVICO_ID)).thenThrow(excecaoEsperada);

		RuntimeException excecaoObtida = assertThrows(RuntimeException.class,
				() -> ordemServicoFinder.obterOuFalhar(ORDEM_SERVICO_ID));

		assertSame(excecaoEsperada, excecaoObtida);

		assertEquals("Falha ao consultar a Ordem de Serviço", excecaoObtida.getMessage());

		verify(ordemServicoGateway).findById(ORDEM_SERVICO_ID);

		verifyNoMoreInteractions(ordemServicoGateway);
	}
}