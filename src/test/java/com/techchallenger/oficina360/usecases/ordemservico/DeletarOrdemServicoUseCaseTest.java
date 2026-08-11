package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletarOrdemServicoUseCaseTest {

	@Mock
	private OrdemServicoGateway gateway;

	@InjectMocks
	private DeletarOrdemServicoUseCase useCase;

	@Test
	void deveDeletarOrdemServicoQuandoExistir() {

		UUID id = UUID.randomUUID();

		OrdemServico ordemServico = new OrdemServico(id, "12345678901", "ABC1234", LocalDateTime.now(), null,
				"Problema no veículo", null, null, List.of(), List.of(), null, null);

		when(gateway.findById(id)).thenReturn(Optional.of(ordemServico));

		useCase.deleteById(id);

		verify(gateway).findById(id);
		verify(gateway).deleteById(id);
	}

	@Test
	void deveLancarExcecaoQuandoOrdemServicoNaoExistir() {

		UUID id = UUID.randomUUID();

		when(gateway.findById(id)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.deleteById(id));

		assertEquals("Ordem de serviço não encontrada", exception.getMessage());

		verify(gateway).findById(id);
		verify(gateway, never()).deleteById(any());
	}
}