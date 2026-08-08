package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarOrdemServicoPorIdUseCaseTest {

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String PLACA_VEICULO = "ABC1D23";

	private static final String DESCRICAO_PROBLEMA = "Barulho no motor";

	@Mock
	private OrdemServicoFinder ordemServicoFinder;

	@InjectMocks
	private BuscarOrdemServicoPorIdUseCase useCase;

	@Test
	void deveRetornarOrdemServicoQuandoEncontrada() {
		UUID ordemServicoId = UUID.randomUUID();

		LocalDateTime dataHoraAbertura = LocalDateTime.of(2026, 7, 25, 10, 0);

		OrdemServico ordemServico = new OrdemServico(ordemServicoId, DOCUMENTO_CLIENTE, PLACA_VEICULO, dataHoraAbertura,
				null, DESCRICAO_PROBLEMA, OrdemDeServicoStatus.RECEBIDA, null, new ArrayList<>(), new ArrayList<>(),
				null, null);

		when(ordemServicoFinder.obterOuFalhar(ordemServicoId)).thenReturn(ordemServico);

		OrdemServicoRespCommand resultado = useCase.findById(ordemServicoId);

		assertAll(
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente()),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo()),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema()));

		verify(ordemServicoFinder).obterOuFalhar(ordemServicoId);

		verifyNoMoreInteractions(ordemServicoFinder);
	}

	@Test
	void devePropagarExcecaoQuandoOrdemServicoNaoForEncontrada() {
		UUID ordemServicoId = UUID.randomUUID();

		RuntimeException excecaoEsperada = new RuntimeException("Ordem de serviço não encontrada");

		when(ordemServicoFinder.obterOuFalhar(ordemServicoId)).thenThrow(excecaoEsperada);

		RuntimeException excecaoObtida = assertThrows(RuntimeException.class, () -> useCase.findById(ordemServicoId));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals("Ordem de serviço não encontrada", excecaoObtida.getMessage()));

		verify(ordemServicoFinder).obterOuFalhar(ordemServicoId);

		verifyNoMoreInteractions(ordemServicoFinder);
	}
}