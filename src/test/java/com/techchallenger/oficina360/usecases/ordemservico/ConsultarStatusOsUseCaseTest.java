package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarStatusOsUseCaseTest {

	@Mock
	private OrdemServicoFinder ordemServicoFinder;

	@InjectMocks
	private ConsultarStatusOsUseCase useCase;

	@Test
	void deveRetornarStatusDaOrdemServico() {

		UUID id = UUID.randomUUID();

		OrdemServico ordemServico = new OrdemServico(id, "12345678901", "ABC1234", LocalDateTime.now(), null,
				"Problema no veículo", OrdemDeServicoStatus.EM_EXECUCAO, null, List.of(), List.of(), null, null);

		when(ordemServicoFinder.obterOuFalhar(id)).thenReturn(ordemServico);

		OrdemDeServicoStatus resultado = useCase.executar(id);

		assertEquals(OrdemDeServicoStatus.EM_EXECUCAO, resultado);

		verify(ordemServicoFinder).obterOuFalhar(id);
	}

	@Test
	void devePropagarExcecaoQuandoOrdemServicoNaoForEncontrada() {

		UUID id = UUID.randomUUID();

		when(ordemServicoFinder.obterOuFalhar(id)).thenThrow(
				new RecursoNaoEncontradoException("Ordem de serviço não encontrada."));

		assertThrows(RecursoNaoEncontradoException.class, () -> useCase.executar(id));

		verify(ordemServicoFinder).obterOuFalhar(id);
	}
}