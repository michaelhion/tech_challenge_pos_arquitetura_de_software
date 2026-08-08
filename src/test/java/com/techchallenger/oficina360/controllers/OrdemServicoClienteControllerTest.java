package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.web.controllers.OrdemServicoClienteController;
import com.techchallenger.oficina360.usecases.ordemservico.AprovarOrcamentoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoDTOMapper.aprovacaoDTOToCommand;
import static com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoDTOMapper.commandToDTO;
import static com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.domainToCommand;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoClienteControllerTest {

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String PLACA_VEICULO = "ABC1D23";

	private static final String DESCRICAO_PROBLEMA = "Veículo apresenta ruído ao frear " + "e vibração no volante.";

	@Mock
	private AprovarOrcamentoUseCase aprovarOrcamentoUseCase;

	@Mock
	private BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;

	private OrdemServicoClienteController controller;

	private UUID ordemServicoId;

	@BeforeEach
	void setUp() {
		controller = new OrdemServicoClienteController(aprovarOrcamentoUseCase, buscarOrdemServicoPorIdUseCase);

		ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");
	}

	@Test
	void deveAprovarOrdemServicoComSucesso() {
		AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(true, null);

		var commandEsperado = aprovacaoDTOToCommand(aprovacaoDTO);

		ResponseEntity<Void> response = controller.aprovar(ordemServicoId, aprovacaoDTO);

		assertAll(() -> assertNotNull(response), () -> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
				() -> assertFalse(response.hasBody()), () -> assertNull(response.getBody()));

		verify(aprovarOrcamentoUseCase, times(1)).aprovar(ordemServicoId, commandEsperado);

		verifyNoMoreInteractions(aprovarOrcamentoUseCase);
	}

	@Test
	void deveReprovarOrdemServicoComSucesso() {
		AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(false, "Orçamento acima do esperado.");

		var commandEsperado = aprovacaoDTOToCommand(aprovacaoDTO);

		ResponseEntity<Void> response = controller.aprovar(ordemServicoId, aprovacaoDTO);

		assertAll(() -> assertNotNull(response), () -> assertEquals(HttpStatus.ACCEPTED, response.getStatusCode()),
				() -> assertFalse(response.hasBody()), () -> assertNull(response.getBody()));

		verify(aprovarOrcamentoUseCase, times(1)).aprovar(ordemServicoId, commandEsperado);

		verifyNoMoreInteractions(aprovarOrcamentoUseCase);
	}

	@Test
	void deveBuscarOrdemServicoPorIdComSucesso() {
		OrdemServico ordemServico = criarOrdemServicoRecebida();

		OrdemServicoRespCommand command = domainToCommand(ordemServico);

		OrdemServicoDTO dtoEsperado = commandToDTO(command);

		when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId)).thenReturn(command);

		ResponseEntity<OrdemServicoDTO> response = controller.buscarPorId(ordemServicoId);

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()), () -> assertNotNull(response.getBody()),
				() -> assertEquals(dtoEsperado.documentoCliente(), response.getBody().documentoCliente()),
				() -> assertEquals(dtoEsperado.placaVeiculo(), response.getBody().placaVeiculo()),
				() -> assertEquals(dtoEsperado.descricaoProblema(), response.getBody().descricaoProblema()),
				() -> assertEquals(dtoEsperado.ordemDeServicoStatus(), response.getBody().ordemDeServicoStatus()));

		verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);

		verifyNoMoreInteractions(buscarOrdemServicoPorIdUseCase);
	}

	@Test
	void devePropagarExcecaoQuandoOrdemServicoNaoExistir() {
		RuntimeException excecaoEsperada = new RuntimeException("Ordem de serviço não encontrada.");

		when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId)).thenThrow(excecaoEsperada);

		RuntimeException excecaoObtida = assertThrows(RuntimeException.class,
				() -> controller.buscarPorId(ordemServicoId));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals("Ordem de serviço não encontrada.", excecaoObtida.getMessage()));

		verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);

		verifyNoMoreInteractions(buscarOrdemServicoPorIdUseCase);
	}

	private OrdemServico criarOrdemServicoRecebida() {
		return new OrdemServico(ordemServicoId, DOCUMENTO_CLIENTE, PLACA_VEICULO, LocalDateTime.of(2026, 7, 25, 10, 0),
				null, DESCRICAO_PROBLEMA, OrdemDeServicoStatus.RECEBIDA, null, new ArrayList<>(), new ArrayList<>(),
				null, null);
	}
}