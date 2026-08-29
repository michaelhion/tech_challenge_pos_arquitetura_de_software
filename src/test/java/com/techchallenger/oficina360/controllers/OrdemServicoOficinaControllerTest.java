package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.dtos.consultarstatus.ConsultarStatusDTO;
import com.techchallenger.oficina360.frameworks.dtos.ordemservico.CriarOrdemServicoRequestDTO;
import com.techchallenger.oficina360.frameworks.dtos.ordemservico.CriarOrdemServicoResponseDTO;
import com.techchallenger.oficina360.frameworks.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.frameworks.dtos.ordemservico.OrdemServicoDetailDTO;
import com.techchallenger.oficina360.frameworks.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.frameworks.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.frameworks.dtos.ordemservico.listagem.OrdemServicoFiltroDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.OrdemServicoOficinaController;
import com.techchallenger.oficina360.usecases.ordemservico.AbrirOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.ConsultarStatusOsUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DeletarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DiagnosticarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.EditarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.FinalizarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.IniciarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.ListarOrdensServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.command.DadosFinanceirosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoDiagnosticoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoReqCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.PecasInsumosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ServicosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.output.OrdemServicoResumoOutput;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import com.techchallenger.oficina360.usecases.ordemservico.query.OrdemServicoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.DirecaoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.ResultadoPaginado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoDTOMapper.commandToDTO;
import static com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoDTOMapper.diagnosticoDTOTOCommand;
import static com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.domainToCommand;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoOficinaControllerTest {

	private static final String CPF = "12345678901";
	private static final String CPF_MASCARADO = "***8901";
	private static final String PLACA = "ABC1D23";
	private static final String PLACA_MASCARADO = "ABC***23";

	private static final String RECLAMACAO_CLIENTE = "Veículo apresenta ruído ao frear e vibração no volante.";

	private static final String DESCRICAO_PROBLEMA = "Veículo apresenta falha na partida.";

	@Mock
	private AbrirOrdemServicoUseCase abrirOrdemServicoUseCase;

	@Mock
	private DiagnosticarOrdemServicoUseCase diagnosticarOrdemServicoUseCase;

	@Mock
	private IniciarExecucaoUseCase iniciarExecucaoUseCase;

	@Mock
	private FinalizarExecucaoUseCase finalizarExecucaoUseCase;

	@Mock
	private ListarOrdensServicoUseCase listarOrdensServicoUseCase;

	@Mock
	private BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;

	@Mock
	private EditarOrdemServicoUseCase editarOrdemServicoUseCase;

	@Mock
	private DeletarOrdemServicoUseCase deletarOrdemServicoUseCase;

	@Mock
	private ConsultarStatusOsUseCase consultarStatusOsUseCase;

	@InjectMocks
	private OrdemServicoOficinaController controller;

	private UUID ordemServicoId;
	private LocalDateTime dataAbertura;

	@BeforeEach
	void setUp() {
		ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

		dataAbertura = LocalDateTime.of(2026, 7, 16, 1, 30);

	}

	@Test
	void deveListarOrdensServicoComSucesso() {
		OrdemServicoFiltroDTO filtro = filtroVazio();

		ListarOrdensServicoQuery queryEsperada = queryPadrao(filtro);

		OrdemServicoResumoOutput primeiraOrdem = new OrdemServicoResumoOutput(ordemServicoId, CPF, PLACA,
				RECLAMACAO_CLIENTE, OrdemDeServicoStatus.RECEBIDA, dataAbertura, BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO);

		OrdemServicoResumoOutput segundaOrdem = new OrdemServicoResumoOutput(
				UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe"), "98765432100", "DEF2G34", DESCRICAO_PROBLEMA,
				OrdemDeServicoStatus.EM_DIAGNOSTICO, dataAbertura.plusDays(1), BigDecimal.valueOf(100),
				BigDecimal.valueOf(400), BigDecimal.valueOf(500));

		ResultadoPaginado<OrdemServicoResumoOutput> resultado = new ResultadoPaginado<>(
				List.of(primeiraOrdem, segundaOrdem), 0, 10, 2, 1, true, true, false);

		when(listarOrdensServicoUseCase.executar(queryEsperada)).thenReturn(resultado);

		ResponseEntity<Page<OrdemServicoDTO>> response = controller.listarOrdensServico(filtro, 0, 10,
				OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		Page<OrdemServicoDTO> pagina = response.getBody();

		assertEquals(2, pagina.getTotalElements());
		assertEquals(1, pagina.getTotalPages());
		assertEquals(0, pagina.getNumber());
		assertEquals(10, pagina.getSize());
		assertEquals(2, pagina.getNumberOfElements());
		assertTrue(pagina.isFirst());
		assertTrue(pagina.isLast());

		OrdemServicoDTO primeiraResposta = pagina.getContent().getFirst();

		assertEquals(CPF_MASCARADO, primeiraResposta.documentoCliente());
		assertEquals(PLACA_MASCARADO, primeiraResposta.placaVeiculo());
		assertEquals(RECLAMACAO_CLIENTE, primeiraResposta.descricaoProblema());
		assertEquals(OrdemDeServicoStatus.RECEBIDA, primeiraResposta.ordemDeServicoStatus());

		OrdemServicoDTO segundaResposta = pagina.getContent().get(1);

		assertAll(
			()->assertEquals("***2100", segundaResposta.documentoCliente()),
			()->assertEquals("DEF***34", segundaResposta.placaVeiculo()),
			()->assertEquals(DESCRICAO_PROBLEMA, segundaResposta.descricaoProblema()),
			()->assertEquals(OrdemDeServicoStatus.EM_DIAGNOSTICO, segundaResposta.ordemDeServicoStatus())
		);


		verify(listarOrdensServicoUseCase, times(1)).executar(queryEsperada);

		verifyNoMoreInteractions(listarOrdensServicoUseCase);
	}

	@Test
	void deveMontarQueryComTodosOsFiltros() {
		LocalDateTime aberturaInicial = LocalDateTime.of(2026, 7, 1, 0, 0);

		LocalDateTime aberturaFinal = LocalDateTime.of(2026, 7, 31, 23, 59);

		OrdemServicoFiltroDTO filtro = new OrdemServicoFiltroDTO(OrdemDeServicoStatus.RECEBIDA, CPF, PLACA,
				aberturaInicial, aberturaFinal, BigDecimal.valueOf(100), BigDecimal.valueOf(1000));

		ListarOrdensServicoQuery queryEsperada = new ListarOrdensServicoQuery(OrdemDeServicoStatus.RECEBIDA, CPF, PLACA,
				aberturaInicial, aberturaFinal, BigDecimal.valueOf(100), BigDecimal.valueOf(1000), 2, 5,
				OrdemServicoOrdenacao.VALOR_TOTAL, DirecaoOrdenacao.DESC);

		ResultadoPaginado<OrdemServicoResumoOutput> resultado = paginaVazia(2, 5);

		when(listarOrdensServicoUseCase.executar(queryEsperada)).thenReturn(resultado);

		ResponseEntity<Page<OrdemServicoDTO>> response = controller.listarOrdensServico(filtro, 2, 5,
				OrdemServicoOrdenacao.VALOR_TOTAL, DirecaoOrdenacao.DESC);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().getNumber());
		assertEquals(5, response.getBody().getSize());

		verify(listarOrdensServicoUseCase).executar(queryEsperada);
	}

	@Test
	void deveRetornarPaginaVaziaQuandoNaoExistiremOrdens() {
		OrdemServicoFiltroDTO filtro = filtroVazio();

		ListarOrdensServicoQuery queryEsperada = queryPadrao(filtro);

		ResultadoPaginado<OrdemServicoResumoOutput> resultado = paginaVazia(0, 10);

		when(listarOrdensServicoUseCase.executar(queryEsperada)).thenReturn(resultado);

		ResponseEntity<Page<OrdemServicoDTO>> response = controller.listarOrdensServico(filtro, 0, 10,
				OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());
		assertTrue(response.getBody().isEmpty());
		assertEquals(0, response.getBody().getTotalElements());
		assertEquals(0, response.getBody().getTotalPages());
		assertEquals(0, response.getBody().getNumber());
		assertEquals(10, response.getBody().getSize());

		verify(listarOrdensServicoUseCase, times(1)).executar(queryEsperada);

		verifyNoMoreInteractions(listarOrdensServicoUseCase);
	}

	@Test
	void deveBuscarOrdemServicoPorIdComSucesso() {
		OrdemServico ordemServico = criarOrdemServico(ordemServicoId);

		OrdemServicoRespCommand command = domainToCommand(ordemServico);

		OrdemServicoDTO dtoEsperado = commandToDTO(command);

		when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId)).thenReturn(command);

		ResponseEntity<OrdemServicoDTO> response = controller.buscarPorId(ordemServicoId);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertAll(() -> assertEquals(dtoEsperado.documentoCliente(), response.getBody().documentoCliente()),
				() -> assertEquals(dtoEsperado.placaVeiculo(), response.getBody().placaVeiculo()),
				() -> assertEquals(dtoEsperado.descricaoProblema(), response.getBody().descricaoProblema()),
				() -> assertEquals(dtoEsperado.ordemDeServicoStatus(), response.getBody().ordemDeServicoStatus()));

		verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);

		verifyNoMoreInteractions(buscarOrdemServicoPorIdUseCase);
	}

	@Test
	void devePropagarExcecaoQuandoOrdemNaoForEncontrada() {
		RuntimeException excecaoEsperada = new RuntimeException("Ordem de serviço não encontrada");

		when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId)).thenThrow(excecaoEsperada);

		RuntimeException excecaoObtida = assertThrows(RuntimeException.class,
				() -> controller.buscarPorId(ordemServicoId));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals("Ordem de serviço não encontrada", excecaoObtida.getMessage()));

		verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);

		verifyNoMoreInteractions(buscarOrdemServicoPorIdUseCase);
	}

	@Test
	void deveSalvarOrdemServicoComSucesso() {
		CriarOrdemServicoRequestDTO request = new CriarOrdemServicoRequestDTO(CPF, PLACA, RECLAMACAO_CLIENTE);
		OrdemServicoReqCommand commandReqEsperado = new OrdemServicoReqCommand(CPF, PLACA, RECLAMACAO_CLIENTE);
		OrdemServicoRespCommand commandEsperado = new OrdemServicoRespCommand(ordemServicoId, CPF, PLACA,
				RECLAMACAO_CLIENTE, OrdemDeServicoStatus.RECEBIDA);
		when(abrirOrdemServicoUseCase.abrirOrdemServico(commandReqEsperado)).thenReturn(commandEsperado);

		ResponseEntity<CriarOrdemServicoResponseDTO> response = controller.salvar(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ordemServicoId, response.getBody().id());
		assertEquals(CPF_MASCARADO, response.getBody().documentoCliente());
		assertEquals(PLACA_MASCARADO, response.getBody().placaVeiculo());
		assertEquals(RECLAMACAO_CLIENTE, response.getBody().descricaoProblema());
		assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

		verify(abrirOrdemServicoUseCase, times(1)).abrirOrdemServico(commandReqEsperado);
	}

	@Test
	void deveEditarOrdemServicoComSucesso() {
		String novaDescricao = "Veículo apresenta ruído ao frear após " + "troca recente de pastilhas.";

		OrdemServicoDTO request = new OrdemServicoDTO(CPF, PLACA, novaDescricao, OrdemDeServicoStatus.RECEBIDA);

		OrdemServicoReqCommand commandEsperado = new OrdemServicoReqCommand(CPF, PLACA, novaDescricao);

		OrdemServico ordemServicoAtualizada = new OrdemServico(ordemServicoId, CPF, PLACA,
				LocalDateTime.of(2026, 7, 20, 8, 0), null, novaDescricao, OrdemDeServicoStatus.RECEBIDA, null,
				new ArrayList<>(), new ArrayList<>(), null, null);

		when(editarOrdemServicoUseCase.edit(ordemServicoId, commandEsperado)).thenReturn(ordemServicoAtualizada);

		ResponseEntity<OrdemServicoDTO> response = controller.editar(ordemServicoId, request);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertAll(() -> assertEquals(CPF_MASCARADO, response.getBody().documentoCliente()),
				() -> assertEquals(PLACA_MASCARADO, response.getBody().placaVeiculo()),
				() -> assertEquals(novaDescricao, response.getBody().descricaoProblema()),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus()));

		verify(editarOrdemServicoUseCase, times(1)).edit(ordemServicoId, commandEsperado);

		verifyNoMoreInteractions(editarOrdemServicoUseCase);
	}

	@Test
	void deveDeletarOrdemServicoComSucesso() {
		doNothing().when(deletarOrdemServicoUseCase).deleteById(ordemServicoId);

		ResponseEntity<Void> response = controller.deletar(ordemServicoId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());

		verify(deletarOrdemServicoUseCase, times(1)).deleteById(ordemServicoId);
	}

	@Test
	void deveDiagnosticarOrdemServicoComSucesso() {
		DiagnosticoDTO diagnosticoDTO = new DiagnosticoDTO(List.of("SRV-TROCA-OLEO", "SRV-ALINHAMENTO"),
				List.of(new DiagnosticoEstoqueDTO("EST-FILTRO-OLEO", 1),
						new DiagnosticoEstoqueDTO("EST-OLEO-5W30", 4)));

		var diagnosticoCommand = diagnosticoDTOTOCommand(diagnosticoDTO);

		DadosFinanceirosCommand dadosFinanceiros = new DadosFinanceirosCommand(
				List.of(new ServicosAdicionadosCommand("Troca de óleo", new BigDecimal("100.00")),
						new ServicosAdicionadosCommand("Alinhamento", new BigDecimal("80.00"))),
				List.of(new PecasInsumosAdicionadosCommand("Filtro de óleo", new BigDecimal("45.90"), 1,
								new BigDecimal("45.90")),
						new PecasInsumosAdicionadosCommand("Óleo 5W30", new BigDecimal("50.00"), 4,
								new BigDecimal("200.00"))), new BigDecimal("180.00"), new BigDecimal("245.90"),
				new BigDecimal("425.90"));

		OrdemServicoDiagnosticoRespCommand retornoUseCase = new OrdemServicoDiagnosticoRespCommand(ordemServicoId, CPF,
				PLACA, RECLAMACAO_CLIENTE, OrdemDeServicoStatus.AGUARDANDO_APROVACAO, dadosFinanceiros);

		when(diagnosticarOrdemServicoUseCase.diagnosticar(ordemServicoId, diagnosticoCommand)).thenReturn(
				retornoUseCase);

		ResponseEntity<OrdemServicoDetailDTO> response = controller.diagnosticar(ordemServicoId, diagnosticoDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		OrdemServicoDetailDTO responseBody = response.getBody();

		assertAll(
				() -> assertEquals(CPF_MASCARADO, responseBody.documentoCliente()),
				() -> assertEquals(PLACA_MASCARADO, responseBody.placaVeiculo()),
				() -> assertEquals(RECLAMACAO_CLIENTE, responseBody.descricaoProblema()),
				() -> assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, responseBody.ordemDeServicoStatus()),
				() -> assertNotNull(responseBody.dadosFinanceiros()), () -> assertEquals(0,
						new BigDecimal("180.00").compareTo(responseBody.dadosFinanceiros().valorServicos())),
				() -> assertEquals(0,
						new BigDecimal("245.90").compareTo(responseBody.dadosFinanceiros().valorPecasInsumos())),
				() -> assertEquals(0, new BigDecimal("425.90").compareTo(responseBody.dadosFinanceiros().valorTotal())),
				() -> assertEquals(2, responseBody.dadosFinanceiros().servicos().size()),
				() -> assertEquals(2, responseBody.dadosFinanceiros().pecasInsumos().size()));

		verify(diagnosticarOrdemServicoUseCase, times(1)).diagnosticar(ordemServicoId, diagnosticoCommand);

		verifyNoMoreInteractions(diagnosticarOrdemServicoUseCase);
	}

	@Test
	void deveIniciarExecucaoComSucesso() {

		ResponseEntity<OrdemServicoDTO> response = controller.iniciarExecucao(ordemServicoId);

		assertNotNull(response);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
		assertFalse(response.hasBody());
		assertNull(response.getBody());

		verify(iniciarExecucaoUseCase, times(1)).iniciarExecucao(ordemServicoId);

		verifyNoMoreInteractions(iniciarExecucaoUseCase);
	}

	@Test
	void deveFinalizarExecucaoComSucesso() {
		ResponseEntity<OrdemServicoDTO> response = controller.finalizarExecucao(ordemServicoId);

		assertNotNull(response);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
		assertFalse(response.hasBody());
		assertNull(response.getBody());

		verify(finalizarExecucaoUseCase, times(1)).finalizarExecucao(ordemServicoId);

		verifyNoMoreInteractions(finalizarExecucaoUseCase);
	}

	@Test
	void deveConsultarStatusDaOrdemServico() {
		when(consultarStatusOsUseCase.executar(ordemServicoId)).thenReturn(OrdemDeServicoStatus.RECEBIDA);

		ResponseEntity<ConsultarStatusDTO> response = controller.consultarStatus(ordemServicoId);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().status());

		verify(consultarStatusOsUseCase, times(1)).executar(ordemServicoId);

		verifyNoMoreInteractions(consultarStatusOsUseCase);
	}

	private OrdemServico criarOrdemServico(UUID id) {
		return new OrdemServico(id, CPF, PLACA, dataAbertura, null,
				OrdemServicoOficinaControllerTest.RECLAMACAO_CLIENTE, OrdemDeServicoStatus.AGUARDANDO_APROVACAO, null,
				new ArrayList<>(), new ArrayList<>(), null, null);
	}

	private OrdemServicoFiltroDTO filtroVazio() {
		return new OrdemServicoFiltroDTO(null, null, null, null, null, null, null);
	}

	private ListarOrdensServicoQuery queryPadrao(OrdemServicoFiltroDTO filtro) {
		return new ListarOrdensServicoQuery(filtro.status(), filtro.documentoCliente(), filtro.placa(),
				filtro.aberturaInicial(), filtro.aberturaFinal(), filtro.valorMinimo(), filtro.valorMaximo(), 0, 10,
				OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);
	}

	private ResultadoPaginado<OrdemServicoResumoOutput> paginaVazia(int pagina, int tamanho) {
		return new ResultadoPaginado<>(List.of(), pagina, tamanho, 0, 0, pagina == 0, true, false);
	}
}