package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.listagem.OrdemServicoFiltroDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoOficinaControllerTest {

	private static final String CPF = "12345678901";
	private static final String PLACA = "ABC1D23";

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
	private OrdemServicoDTO ordemServicoDTO;
	private CriarOrdemServicoDTO criarOrdemServicoDTO;
	private LocalDateTime dataAbertura;

	@BeforeEach
	void setUp() {
		ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

		dataAbertura = LocalDateTime.of(2026, 7, 16, 1, 30);

		ordemServicoDTO = new OrdemServicoDTO(ordemServicoId, CPF, PLACA, RECLAMACAO_CLIENTE,
				OrdemDeServicoStatus.RECEBIDA, null);

		criarOrdemServicoDTO = new CriarOrdemServicoDTO(ordemServicoId, CPF, PLACA, RECLAMACAO_CLIENTE,
				OrdemDeServicoStatus.RECEBIDA);
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

		OrdemServicoDTO primeiraResposta = pagina.getContent().get(0);

		assertEquals(ordemServicoId, primeiraResposta.id());
		assertEquals(CPF, primeiraResposta.documentoCliente());
		assertEquals(PLACA, primeiraResposta.placaVeiculo());
		assertEquals(RECLAMACAO_CLIENTE, primeiraResposta.descricaoProblema());
		assertEquals(OrdemDeServicoStatus.RECEBIDA, primeiraResposta.ordemDeServicoStatus());

		OrdemServicoDTO segundaResposta = pagina.getContent().get(1);

		assertEquals("98765432100", segundaResposta.documentoCliente());
		assertEquals("DEF2G34", segundaResposta.placaVeiculo());
		assertEquals(DESCRICAO_PROBLEMA, segundaResposta.descricaoProblema());
		assertEquals(OrdemDeServicoStatus.EM_DIAGNOSTICO, segundaResposta.ordemDeServicoStatus());

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
		when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId)).thenReturn(Optional.of(ordemServicoDTO));

		ResponseEntity<OrdemServicoDTO> response = controller.buscarPorId(ordemServicoId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ordemServicoId, response.getBody().id());
		assertEquals(CPF, response.getBody().documentoCliente());
		assertEquals(PLACA, response.getBody().placaVeiculo());
		assertEquals(RECLAMACAO_CLIENTE, response.getBody().descricaoProblema());
		assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

		verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);
	}

	@Test
	void deveRetornarNotFoundQuandoOrdemNaoForEncontrada() {
		when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId)).thenReturn(Optional.empty());

		ResponseEntity<OrdemServicoDTO> response = controller.buscarPorId(ordemServicoId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());

		verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);
	}

	@Test
	void deveSalvarOrdemServicoComSucesso() {
		CriarOrdemServicoDTO request = new CriarOrdemServicoDTO(null, CPF, PLACA, RECLAMACAO_CLIENTE, null);

		when(abrirOrdemServicoUseCase.abrirOrdemServico(request)).thenReturn(criarOrdemServicoDTO);

		ResponseEntity<CriarOrdemServicoDTO> response = controller.salvar(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ordemServicoId, response.getBody().id());
		assertEquals(CPF, response.getBody().documentoCliente());
		assertEquals(PLACA, response.getBody().placaVeiculo());
		assertEquals(RECLAMACAO_CLIENTE, response.getBody().descricaoProblema());
		assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

		verify(abrirOrdemServicoUseCase, times(1)).abrirOrdemServico(request);
	}

	@Test
	void deveEditarOrdemServicoComSucesso() {
		String novaDescricao = "Veículo apresenta ruído ao frear após " + "troca recente de pastilhas.";

		OrdemServicoDTO atualizada = new OrdemServicoDTO(ordemServicoId, CPF, PLACA, novaDescricao,
				OrdemDeServicoStatus.RECEBIDA, null);

		when(editarOrdemServicoUseCase.edit(ordemServicoId, atualizada)).thenReturn(atualizada);

		ResponseEntity<OrdemServicoDTO> response = controller.editar(ordemServicoId, atualizada);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ordemServicoId, response.getBody().id());
		assertEquals(novaDescricao, response.getBody().descricaoProblema());

		verify(editarOrdemServicoUseCase, times(1)).edit(ordemServicoId, atualizada);
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

		OrdemServicoDTO diagnosticada = new OrdemServicoDTO(ordemServicoId, CPF, PLACA, RECLAMACAO_CLIENTE,
				OrdemDeServicoStatus.AGUARDANDO_APROVACAO, null);

		when(diagnosticarOrdemServicoUseCase.diagnosticar(ordemServicoId, diagnosticoDTO)).thenReturn(diagnosticada);

		ResponseEntity<OrdemServicoDTO> response = controller.diagnosticar(ordemServicoId, diagnosticoDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(ordemServicoId, response.getBody().id());
		assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, response.getBody().ordemDeServicoStatus());

		verify(diagnosticarOrdemServicoUseCase, times(1)).diagnosticar(ordemServicoId, diagnosticoDTO);
	}

	@Test
	void deveIniciarExecucaoComSucesso() {
		when(iniciarExecucaoUseCase.iniciarExecucao(ordemServicoId)).thenReturn(ordemServicoDTO);

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