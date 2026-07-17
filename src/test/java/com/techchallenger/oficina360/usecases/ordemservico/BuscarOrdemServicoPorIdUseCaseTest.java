package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarOrdemServicoPorIdUseCaseTest {

	@Mock
	private OrdemServicoGateway gateway;

	@InjectMocks
	private BuscarOrdemServicoPorIdUseCase useCase;

	@Test
	void deveRetornarOrdemServicoQuandoEncontrada() {

		UUID id = UUID.randomUUID();

		OrdemServico ordemServico =
				new OrdemServico(
						id,
						"12345678901",
						"ABC1234",
						null,
						null,
						"Barulho no motor",
						OrdemDeServicoStatus.RECEBIDA,
						null,
						List.of(),
						List.of(),
						BigDecimal.ZERO,
						BigDecimal.ZERO,
						BigDecimal.ZERO,
						null,
						null
				);

		when(gateway.findById(id))
				.thenReturn(Optional.of(ordemServico));

		Optional<OrdemServicoDTO> resultado =
				useCase.findById(id);

		assertTrue(resultado.isPresent());
		assertEquals(id, resultado.get().id());

		verify(gateway).findById(id);
	}

	@Test
	void deveRetornarOptionalVazioQuandoNaoEncontrada() {

		UUID id = UUID.randomUUID();

		when(gateway.findById(id))
				.thenReturn(Optional.empty());

		Optional<OrdemServicoDTO> resultado =
				useCase.findById(id);

		assertTrue(resultado.isEmpty());

		verify(gateway).findById(id);
	}
}