package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoRequestDTO;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoResponseDTO;
import com.techchallenger.oficina360.dtos.ordemservico.DadosFinanceirosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDetailDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.PecasInsumosAdicionadosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.ServicosAdicionadosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.usecases.ordemservico.command.AprovacaoOrdemServicoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.DadosFinanceirosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.DiagnosticoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.DiagnosticoEstoqueCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoDiagnosticoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoReqCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.PecasInsumosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ServicosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.output.OrdemServicoResumoOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoDTOMapperTest {

	private static final UUID ORDEM_SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String PLACA_VEICULO = "ABC1D23";

	private static final String DESCRICAO_PROBLEMA = "Veículo apresenta ruído metálico ao frear";

	private static final String OBSERVACAO_CLIENTE = "Cliente autorizou contato por telefone";

	private static final LocalDateTime DATA_ABERTURA = LocalDateTime.of(2026, 8, 1, 9, 15);

	private static final LocalDateTime DATA_FECHAMENTO = LocalDateTime.of(2026, 8, 2, 17, 45);

	private static final LocalDateTime DATA_INICIO_EXECUCAO = LocalDateTime.of(2026, 8, 2, 10, 30);

	private static final LocalDateTime DATA_FIM_EXECUCAO = LocalDateTime.of(2026, 8, 2, 16, 20);

	private static final BigDecimal VALOR_SERVICOS = new BigDecimal("287.43");

	private static final BigDecimal VALOR_PECAS = new BigDecimal("514.76");

	private static final BigDecimal VALOR_TOTAL = new BigDecimal("802.19");

	private static final String DESCRICAO_SERVICO = "Substituição das pastilhas de freio";

	private static final BigDecimal VALOR_SERVICO = new BigDecimal("287.43");

	private static final String NOME_PECA = "Jogo de pastilhas premium";

	private static final BigDecimal VALOR_UNITARIO_PECA = new BigDecimal("257.38");

	private static final Integer QUANTIDADE_PECA = 2;

	private static final BigDecimal VALOR_TOTAL_PECA = new BigDecimal("514.76");

	@Mock
	private OrdemServicoItemEstoqueDTOMapper ordemServicoItemEstoqueDTOMapper;

	@Mock
	private OrdemServicoServicoMapper ordemServicoServicoMapper;

	@Mock
	private OrdemServico ordemServico;

	@Mock
	private OrdemServicoEntity ordemServicoEntity;

	@Mock
	private OrdemServicoServico servico;

	@Mock
	private OrdemServicoItemEstoque itemEstoque;

	@Mock
	private OrdemServicoServico servicoConvertido;

	@Mock
	private OrdemServicoItemEstoque itemConvertido;

	private OrdemServicoDTOMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new OrdemServicoDTOMapper(ordemServicoItemEstoqueDTOMapper, ordemServicoServicoMapper);
	}

	@Test
	void deveConverterDomainParaDTOMantendoOrdemDosCampos() {
		when(ordemServico.getDocumentoCliente()).thenReturn(DOCUMENTO_CLIENTE);

		when(ordemServico.getPlacaVeiculo()).thenReturn(PLACA_VEICULO);

		when(ordemServico.getDescricaoProblema()).thenReturn(DESCRICAO_PROBLEMA);

		when(ordemServico.getOrdemDeServicoStatus()).thenReturn(OrdemDeServicoStatus.RECEBIDA);

		OrdemServicoDTO resultado = OrdemServicoDTOMapper.domainToDTO(ordemServico);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente(),
						"O documento deve ocupar documentoCliente"),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo(), "A placa deve ocupar placaVeiculo"),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema(),
						"A descrição deve ocupar descricaoProblema"),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.ordemDeServicoStatus(),
						"O status deve ocupar ordemDeServicoStatus"));
	}

	@Test
	void deveConverterDTOParaCommandMantendoOrdemDosCampos() {
		OrdemServicoDTO dto = new OrdemServicoDTO(DOCUMENTO_CLIENTE, PLACA_VEICULO, DESCRICAO_PROBLEMA,
				OrdemDeServicoStatus.RECEBIDA);

		OrdemServicoReqCommand resultado = OrdemServicoDTOMapper.toCommand(dto);

		assertNotNull(resultado);

		assertDadosRequisicao(resultado);
	}

	@Test
	void deveConverterCriarOrdemServicoRequestParaCommand() {
		CriarOrdemServicoRequestDTO dto = new CriarOrdemServicoRequestDTO(DOCUMENTO_CLIENTE, PLACA_VEICULO,
				DESCRICAO_PROBLEMA);

		OrdemServicoReqCommand resultado = OrdemServicoDTOMapper.criarOsToCommand(dto);

		assertNotNull(resultado);

		assertDadosRequisicao(resultado);
	}

	@Test
	void deveRetornarListaVaziaQuandoOutputsForemNulos() {
		List<OrdemServicoDTO> resultado = OrdemServicoDTOMapper.outputListToDTOList(null);

		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}

	@Test
	void deveRetornarListaVaziaQuandoOutputsForemVazios() {
		List<OrdemServicoDTO> resultado = OrdemServicoDTOMapper.outputListToDTOList(List.of());

		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}

	@Test
	void deveConverterListaDeOutputsMantendoOrdemDosCampos() {
		OrdemServicoResumoOutput output = new OrdemServicoResumoOutput(ORDEM_SERVICO_ID, DOCUMENTO_CLIENTE,
				PLACA_VEICULO, DESCRICAO_PROBLEMA, OrdemDeServicoStatus.RECEBIDA, DATA_ABERTURA, VALOR_SERVICOS,
				VALOR_PECAS, VALOR_TOTAL);

		List<OrdemServicoDTO> resultado = OrdemServicoDTOMapper.outputListToDTOList(List.of(output));

		assertNotNull(resultado);
		assertEquals(1, resultado.size());

		OrdemServicoDTO dto = resultado.get(0);

		assertNotNull(dto);

		assertAll(() -> assertEquals(DOCUMENTO_CLIENTE, dto.documentoCliente(),
						"O documento do cliente deve ocupar o campo documentoCliente"),
				() -> assertEquals(PLACA_VEICULO, dto.placaVeiculo(), "A placa deve ocupar o campo placaVeiculo"),
				() -> assertEquals(DESCRICAO_PROBLEMA, dto.descricaoProblema(),
						"A descrição deve ocupar o campo descricaoProblema"),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, dto.ordemDeServicoStatus(),
						"O status deve ocupar o campo ordemDeServicoStatus"));
	}

	@Test
	void devePreservarElementoNuloNaConversaoDaListaDeOutputs() {
		List<OrdemServicoDTO> resultado = OrdemServicoDTOMapper.outputListToDTOList(
				java.util.Collections.singletonList(null));

		assertEquals(1, resultado.size());

		assertNull(resultado.get(0));
	}

	@Test
	void deveConverterAprovacaoDTOParaCommand() {
		AprovacaoOrdemServicoDTO dto = new AprovacaoOrdemServicoDTO(true, "Orçamento aprovado pelo cliente");

		AprovacaoOrdemServicoCommand resultado = OrdemServicoDTOMapper.aprovacaoDTOToCommand(dto);

		assertNotNull(resultado);

		assertAll(() -> assertTrue(resultado.aprovado()),
				() -> assertEquals("Orçamento aprovado pelo cliente", resultado.observacao()));
	}


	@Test
	void deveRetornarNullAoConverterEntityNulaParaDomain() {
		OrdemServico resultado = mapper.toDomain(null);

		assertNull(resultado);
	}

	@Test
	void deveConverterDomainParaEntityMantendoOrdemDosCampos() {
		configurarDomain();

		when(ordemServicoServicoMapper.toEntity(servico)).thenReturn(anyServicoEntity());

		when(ordemServicoItemEstoqueDTOMapper.toEntity(itemEstoque)).thenReturn(anyItemEntity());

		OrdemServicoEntity resultado = mapper.toEntity(ordemServico);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ORDEM_SERVICO_ID, resultado.getId()),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.getDocumentoCliente()),
				() -> assertEquals(PLACA_VEICULO, resultado.getPlacaVeiculo()),
				() -> assertEquals(DATA_ABERTURA, resultado.getDtHoraAbertura()),
				() -> assertEquals(DATA_FECHAMENTO, resultado.getDtHoraFechamento()),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.getDescricaoProblema()),
				() -> assertEquals(OrdemDeServicoStatus.FINALIZADA, resultado.getOrdemDeServicoStatus()),
				() -> assertEquals(OBSERVACAO_CLIENTE, resultado.getObservacaoCliente()),
				() -> assertBigDecimalEquals(VALOR_SERVICOS, resultado.getValorServicos()),
				() -> assertBigDecimalEquals(VALOR_PECAS, resultado.getValorPecasInsumos()),
				() -> assertBigDecimalEquals(VALOR_TOTAL, resultado.getValorOs()),
				() -> assertEquals(DATA_INICIO_EXECUCAO, resultado.getDtHoraInicioExecucao()),
				() -> assertEquals(DATA_FIM_EXECUCAO, resultado.getDtHoraFimExecucao()),
				() -> assertEquals(1, resultado.getServicos().size()),
				() -> assertEquals(1, resultado.getItensEstoque().size()));

		verify(ordemServicoServicoMapper).toEntity(servico);

		verify(ordemServicoItemEstoqueDTOMapper).toEntity(itemEstoque);
	}

	@Test
	void deveRetornarNullAoConverterDomainNuloParaEntity() {
		OrdemServicoEntity resultado = mapper.toEntity(null);

		assertNull(resultado);

		verify(ordemServicoServicoMapper, never()).toEntity(any());

		verify(ordemServicoItemEstoqueDTOMapper, never()).toEntity(any());
	}

	@Test
	void deveConverterCommandParaDTOComCamposNaOrdemCorreta() {
		OrdemServicoRespCommand command = criarOrdemServicoRespCommand();

		OrdemServicoDTO resultado = OrdemServicoDTOMapper.commandToDTO(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente()),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo()),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema()),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.ordemDeServicoStatus()));
	}

	@Test
	void deveConverterRespostaCriacaoParaDTOComId() {
		OrdemServicoRespCommand command = criarOrdemServicoRespCommand();

		CriarOrdemServicoResponseDTO resultado = OrdemServicoDTOMapper.criarOsRespCommandToDTO(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ORDEM_SERVICO_ID, resultado.id()),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente()),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo()),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema()),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.ordemDeServicoStatus()));
	}

	@Test
	void deveConverterDiagnosticoCommandParaDetailDTO() {
		DadosFinanceirosCommand dadosFinanceiros = criarDadosFinanceirosCommand();

		OrdemServicoDiagnosticoRespCommand command = new OrdemServicoDiagnosticoRespCommand(ORDEM_SERVICO_ID,
				DOCUMENTO_CLIENTE, PLACA_VEICULO, DESCRICAO_PROBLEMA, OrdemDeServicoStatus.AGUARDANDO_APROVACAO,
				dadosFinanceiros);

		OrdemServicoDetailDTO resultado = OrdemServicoDTOMapper.commandDadosFinanceirosToDTO(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente()),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo()),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema()),
				() -> assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, resultado.ordemDeServicoStatus()),
				() -> assertNotNull(resultado.dadosFinanceiros()));

		assertDadosFinanceiros(resultado.dadosFinanceiros());
	}

	@Test
	void deveConverterDiagnosticoEstoqueDTOParaCommand() {
		DiagnosticoEstoqueDTO dto = new DiagnosticoEstoqueDTO("EST-PASTILHA-FREIO", 3);

		DiagnosticoEstoqueCommand resultado = OrdemServicoDTOMapper.itenEstoqueDTOToCommand(dto);

		assertNotNull(resultado);

		assertAll(() -> assertEquals("EST-PASTILHA-FREIO", resultado.codigo(), "O código deve ocupar o campo codigo"),
				() -> assertEquals(3, resultado.quantidade(), "A quantidade deve ocupar quantidade"));
	}

	@Test
	void deveConverterDiagnosticoDTOParaCommandMantendoListas() {
		DiagnosticoDTO dto = new DiagnosticoDTO(List.of("SRV-TROCA-OLEO", "SRV-ALINHAMENTO"),
				List.of(new DiagnosticoEstoqueDTO("EST-FILTRO-OLEO", 1),
						new DiagnosticoEstoqueDTO("EST-OLEO-5W30", 4)));

		DiagnosticoCommand resultado = OrdemServicoDTOMapper.diagnosticoDTOTOCommand(dto);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(List.of("SRV-TROCA-OLEO", "SRV-ALINHAMENTO"), resultado.codigosServicos()),
				() -> assertEquals(2, resultado.itensEstoque().size()),
				() -> assertEquals("EST-FILTRO-OLEO", resultado.itensEstoque().get(0).codigo()),
				() -> assertEquals(1, resultado.itensEstoque().get(0).quantidade()),
				() -> assertEquals("EST-OLEO-5W30", resultado.itensEstoque().get(1).codigo()),
				() -> assertEquals(4, resultado.itensEstoque().get(1).quantidade()));
	}


	private void configurarDomain() {
		when(ordemServico.getId()).thenReturn(ORDEM_SERVICO_ID);

		when(ordemServico.getDocumentoCliente()).thenReturn(DOCUMENTO_CLIENTE);

		when(ordemServico.getPlacaVeiculo()).thenReturn(PLACA_VEICULO);

		when(ordemServico.getDtHoraAbertura()).thenReturn(DATA_ABERTURA);

		when(ordemServico.getDtHoraFechamento()).thenReturn(DATA_FECHAMENTO);

		when(ordemServico.getDescricaoProblema()).thenReturn(DESCRICAO_PROBLEMA);

		when(ordemServico.getOrdemDeServicoStatus()).thenReturn(OrdemDeServicoStatus.FINALIZADA);

		when(ordemServico.getObservacaoCliente()).thenReturn(OBSERVACAO_CLIENTE);

		when(ordemServico.getValorServicos()).thenReturn(VALOR_SERVICOS);

		when(ordemServico.getValorPecasInsumos()).thenReturn(VALOR_PECAS);

		when(ordemServico.getValorOs()).thenReturn(VALOR_TOTAL);

		when(ordemServico.getDtHoraInicioExecucao()).thenReturn(DATA_INICIO_EXECUCAO);

		when(ordemServico.getDtHoraFimExecucao()).thenReturn(DATA_FIM_EXECUCAO);

		when(ordemServico.getServicos()).thenReturn(List.of(servico));

		when(ordemServico.getItensEstoque()).thenReturn(List.of(itemEstoque));
	}

	private OrdemServicoRespCommand criarOrdemServicoRespCommand() {
		return new OrdemServicoRespCommand(ORDEM_SERVICO_ID, DOCUMENTO_CLIENTE, PLACA_VEICULO, DESCRICAO_PROBLEMA,
				OrdemDeServicoStatus.RECEBIDA);
	}

	private DadosFinanceirosCommand criarDadosFinanceirosCommand() {
		return new DadosFinanceirosCommand(List.of(new ServicosAdicionadosCommand(DESCRICAO_SERVICO, VALOR_SERVICO)),
				List.of(new PecasInsumosAdicionadosCommand(NOME_PECA, VALOR_UNITARIO_PECA, QUANTIDADE_PECA,
						VALOR_TOTAL_PECA)), VALOR_SERVICOS, VALOR_PECAS, VALOR_TOTAL);
	}

	private void assertDadosRequisicao(OrdemServicoReqCommand resultado) {
		assertAll(() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente(),
						"O documento deve ocupar documentoCliente"),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo(), "A placa deve ocupar placaVeiculo"),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema(),
						"A descrição deve ocupar descricaoProblema"));
	}

	private void assertDadosFinanceiros(DadosFinanceirosDTO resultado) {
		assertAll(() -> assertBigDecimalEquals(VALOR_SERVICOS, resultado.valorServicos()),
				() -> assertBigDecimalEquals(VALOR_PECAS, resultado.valorPecasInsumos()),
				() -> assertBigDecimalEquals(VALOR_TOTAL, resultado.valorTotal()),
				() -> assertEquals(1, resultado.servicos().size()),
				() -> assertEquals(1, resultado.pecasInsumos().size()));

		ServicosAdicionadosDTO servicoDTO = resultado.servicos().get(0);

		PecasInsumosAdicionadosDTO pecaDTO = resultado.pecasInsumos().get(0);

		assertAll(() -> assertEquals(DESCRICAO_SERVICO, servicoDTO.nome()),
				() -> assertBigDecimalEquals(VALOR_SERVICO, servicoDTO.valor()),
				() -> assertEquals(NOME_PECA, pecaDTO.nome()),
				() -> assertBigDecimalEquals(VALOR_UNITARIO_PECA, pecaDTO.valorUnitario()),
				() -> assertEquals(QUANTIDADE_PECA, pecaDTO.quantidade()),
				() -> assertBigDecimalEquals(VALOR_TOTAL_PECA, pecaDTO.valorTotal()));
	}

	private void assertBigDecimalEquals(BigDecimal esperado, BigDecimal atual) {
		assertNotNull(atual);

		assertEquals(0, esperado.compareTo(atual));
	}

	private com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoServicoEntity anyServicoEntity() {
		return org.mockito.Mockito.mock(
				com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoServicoEntity.class);
	}

	private com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoItemEstoqueEntity anyItemEntity() {
		return org.mockito.Mockito.mock(
				com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoItemEstoqueEntity.class);
	}
}