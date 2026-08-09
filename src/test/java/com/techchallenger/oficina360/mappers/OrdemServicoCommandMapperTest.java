package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.ordemservico.command.DadosFinanceirosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoDiagnosticoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.PecasInsumosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ServicosAdicionadosCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemServicoCommandMapperTest {

	private static final UUID ORDEM_SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String PLACA_VEICULO = "ABC1D23";

	private static final String DESCRICAO_PROBLEMA = "Veículo apresenta falha na partida";

	private static final String DESCRICAO_SERVICO = "Substituição do motor de partida";

	private static final String NOME_PECA = "Motor de partida premium";

	private static final BigDecimal VALOR_SERVICO = new BigDecimal("275.43");

	private static final BigDecimal VALOR_UNITARIO_PECA = new BigDecimal("489.71");

	private static final Integer QUANTIDADE_PECA = 2;

	private static final BigDecimal VALOR_TOTAL_PECA = new BigDecimal("979.42");

	private static final BigDecimal VALOR_SERVICOS = new BigDecimal("275.43");

	private static final BigDecimal VALOR_PECAS_INSUMOS = new BigDecimal("979.42");

	private static final BigDecimal VALOR_TOTAL_OS = new BigDecimal("1254.85");

	@Mock
	private OrdemServico ordemServico;

	@Mock
	private OrdemServicoServico ordemServicoServico;

	@Mock
	private OrdemServicoItemEstoque ordemServicoItemEstoque;

	@Test
	void deveConverterDominioParaCommandMantendoOrdemDosCampos() {
		configurarDadosBasicos();

		OrdemServicoRespCommand resultado = OrdemServicoCommandMapper.domainToCommand(ordemServico);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ORDEM_SERVICO_ID, resultado.id(), "O ID deve ocupar o campo id"),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente(),
						"O documento deve ocupar documentoCliente"),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo(), "A placa deve ocupar placaVeiculo"),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema(),
						"A descrição deve ocupar descricaoProblema"),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.status(),
						"O status deve ocupar o campo status"));

		verify(ordemServico).getId();
		verify(ordemServico).getDocumentoCliente();
		verify(ordemServico).getPlacaVeiculo();
		verify(ordemServico).getDescricaoProblema();
		verify(ordemServico).getOrdemDeServicoStatus();
	}

	@Test
	void deveConverterDiagnosticoMantendoDadosBasicosNasPosicoesCorretas() {
		configurarDadosBasicos();
		configurarDadosFinanceiros();

		OrdemServicoDiagnosticoRespCommand resultado = OrdemServicoCommandMapper.domainToDiagnosticoCommand(
				ordemServico);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ORDEM_SERVICO_ID, resultado.id(), "O ID deve ocupar o campo id"),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documentoCliente(),
						"O documento não pode ser confundido com a placa"),
				() -> assertEquals(PLACA_VEICULO, resultado.placaVeiculo(),
						"A placa não pode ser confundida com o documento"),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.descricaoProblema(),
						"A descrição do problema deve ocupar seu campo"),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.status(),
						"O status deve ocupar o campo status"),
				() -> assertNotNull(resultado.dadosFinanceirosCommand(), "Os dados financeiros devem ser preenchidos"));
	}

	@Test
	void deveMapearValoresFinanceirosNasPosicoesCorretas() {
		configurarDadosBasicos();
		configurarDadosFinanceiros();

		OrdemServicoDiagnosticoRespCommand resultado = OrdemServicoCommandMapper.domainToDiagnosticoCommand(
				ordemServico);

		DadosFinanceirosCommand dadosFinanceiros = resultado.dadosFinanceirosCommand();

		assertNotNull(dadosFinanceiros);

		assertAll(() -> assertEquals(0, VALOR_SERVICOS.compareTo(dadosFinanceiros.valorServicos()),
						"O valor de serviços deve ocupar valorServicos"),
				() -> assertEquals(0, VALOR_PECAS_INSUMOS.compareTo(dadosFinanceiros.valorPecasInsumos()),
						"O valor das peças deve ocupar valorPecasInsumos"),
				() -> assertEquals(0, VALOR_TOTAL_OS.compareTo(dadosFinanceiros.valorTotal()),
						"O valor total deve ocupar valorTotal"));
	}

	@Test
	void deveMapearServicoAdicionadoMantendoDescricaoEValor() {
		configurarDadosBasicos();
		configurarDadosFinanceiros();

		OrdemServicoDiagnosticoRespCommand resultado = OrdemServicoCommandMapper.domainToDiagnosticoCommand(
				ordemServico);

		DadosFinanceirosCommand dadosFinanceiros = resultado.dadosFinanceirosCommand();

		assertEquals(1, dadosFinanceiros.servicos().size());

		ServicosAdicionadosCommand servico = dadosFinanceiros.servicos().get(0);

		assertAll(() -> assertEquals(DESCRICAO_SERVICO, servico.nome(),
						"A descrição do serviço deve ocupar o campo nome"),
				() -> assertEquals(0, VALOR_SERVICO.compareTo(servico.valor()),
						"O valor do serviço deve ocupar o campo valor"));
	}

	@Test
	void deveMapearPecaInsumoMantendoOrdemDosCampos() {
		configurarDadosBasicos();
		configurarDadosFinanceiros();

		OrdemServicoDiagnosticoRespCommand resultado = OrdemServicoCommandMapper.domainToDiagnosticoCommand(
				ordemServico);

		DadosFinanceirosCommand dadosFinanceiros = resultado.dadosFinanceirosCommand();

		assertEquals(1, dadosFinanceiros.pecasInsumos().size());

		PecasInsumosAdicionadosCommand peca = dadosFinanceiros.pecasInsumos().get(0);

		assertAll(() -> assertEquals(NOME_PECA, peca.nome(), "O nome da peça deve ocupar o campo nome"),
				() -> assertEquals(0, VALOR_UNITARIO_PECA.compareTo(peca.valorUnitario()),
						"O valor unitário não pode ocupar o valor total"),
				() -> assertEquals(QUANTIDADE_PECA, peca.quantidade(), "A quantidade deve ocupar o campo quantidade"),
				() -> assertEquals(0, VALOR_TOTAL_PECA.compareTo(peca.valorTotal()),
						"O valor total não pode ocupar o valor unitário"));
	}

	@Test
	void devePreservarOrdemDosServicosEItensEstoque() {
		OrdemServicoServico primeiroServico = org.mockito.Mockito.mock(OrdemServicoServico.class);

		OrdemServicoServico segundoServico = org.mockito.Mockito.mock(OrdemServicoServico.class);

		OrdemServicoItemEstoque primeiroItem = org.mockito.Mockito.mock(OrdemServicoItemEstoque.class);

		OrdemServicoItemEstoque segundoItem = org.mockito.Mockito.mock(OrdemServicoItemEstoque.class);

		when(primeiroServico.getDescricao()).thenReturn("Primeiro serviço");

		when(primeiroServico.getValor()).thenReturn(new BigDecimal("100.11"));

		when(segundoServico.getDescricao()).thenReturn("Segundo serviço");

		when(segundoServico.getValor()).thenReturn(new BigDecimal("200.22"));

		when(primeiroItem.getNome()).thenReturn("Primeira peça");

		when(primeiroItem.getValorUnitario()).thenReturn(new BigDecimal("50.13"));

		when(primeiroItem.getQuantidade()).thenReturn(1);

		when(primeiroItem.getValorTotal()).thenReturn(new BigDecimal("50.13"));

		when(segundoItem.getNome()).thenReturn("Segunda peça");

		when(segundoItem.getValorUnitario()).thenReturn(new BigDecimal("75.17"));

		when(segundoItem.getQuantidade()).thenReturn(2);

		when(segundoItem.getValorTotal()).thenReturn(new BigDecimal("150.34"));

		configurarDadosBasicos();

		when(ordemServico.getServicos()).thenReturn(List.of(primeiroServico, segundoServico));

		when(ordemServico.getItensEstoque()).thenReturn(List.of(primeiroItem, segundoItem));

		when(ordemServico.getValorServicos()).thenReturn(new BigDecimal("300.33"));

		when(ordemServico.getValorPecasInsumos()).thenReturn(new BigDecimal("200.47"));

		when(ordemServico.getValorOs()).thenReturn(new BigDecimal("500.80"));

		OrdemServicoDiagnosticoRespCommand resultado = OrdemServicoCommandMapper.domainToDiagnosticoCommand(
				ordemServico);

		DadosFinanceirosCommand dadosFinanceiros = resultado.dadosFinanceirosCommand();

		assertAll(() -> assertEquals("Primeiro serviço", dadosFinanceiros.servicos().get(0).nome()),
				() -> assertEquals("Segundo serviço", dadosFinanceiros.servicos().get(1).nome()),
				() -> assertEquals("Primeira peça", dadosFinanceiros.pecasInsumos().get(0).nome()),
				() -> assertEquals("Segunda peça", dadosFinanceiros.pecasInsumos().get(1).nome()));
	}

	@Test
	void deveRetornarListasVaziasQuandoOrdemNaoPossuirDiagnostico() {
		configurarDadosBasicos();

		when(ordemServico.getServicos()).thenReturn(List.of());

		when(ordemServico.getItensEstoque()).thenReturn(List.of());

		when(ordemServico.getValorServicos()).thenReturn(BigDecimal.ZERO);

		when(ordemServico.getValorPecasInsumos()).thenReturn(BigDecimal.ZERO);

		when(ordemServico.getValorOs()).thenReturn(BigDecimal.ZERO);

		OrdemServicoDiagnosticoRespCommand resultado = OrdemServicoCommandMapper.domainToDiagnosticoCommand(
				ordemServico);

		DadosFinanceirosCommand dadosFinanceiros = resultado.dadosFinanceirosCommand();

		assertAll(() -> assertNotNull(dadosFinanceiros.servicos()),
				() -> assertTrue(dadosFinanceiros.servicos().isEmpty()),
				() -> assertNotNull(dadosFinanceiros.pecasInsumos()),
				() -> assertTrue(dadosFinanceiros.pecasInsumos().isEmpty()),
				() -> assertEquals(0, BigDecimal.ZERO.compareTo(dadosFinanceiros.valorServicos())),
				() -> assertEquals(0, BigDecimal.ZERO.compareTo(dadosFinanceiros.valorPecasInsumos())),
				() -> assertEquals(0, BigDecimal.ZERO.compareTo(dadosFinanceiros.valorTotal())));
	}

	@Test
	void devePossuirConstrutorPrivado() throws Exception {
		Constructor<OrdemServicoCommandMapper> constructor = OrdemServicoCommandMapper.class.getDeclaredConstructor();

		assertTrue(Modifier.isPrivate(constructor.getModifiers()));

		constructor.setAccessible(true);

		OrdemServicoCommandMapper instancia = constructor.newInstance();

		assertNotNull(instancia);
	}

	private void configurarDadosBasicos() {
		when(ordemServico.getId()).thenReturn(ORDEM_SERVICO_ID);

		when(ordemServico.getDocumentoCliente()).thenReturn(DOCUMENTO_CLIENTE);

		when(ordemServico.getPlacaVeiculo()).thenReturn(PLACA_VEICULO);

		when(ordemServico.getDescricaoProblema()).thenReturn(DESCRICAO_PROBLEMA);

		when(ordemServico.getOrdemDeServicoStatus()).thenReturn(OrdemDeServicoStatus.RECEBIDA);
	}

	private void configurarDadosFinanceiros() {
		when(ordemServico.getServicos()).thenReturn(List.of(ordemServicoServico));

		when(ordemServico.getItensEstoque()).thenReturn(List.of(ordemServicoItemEstoque));

		when(ordemServico.getValorServicos()).thenReturn(VALOR_SERVICOS);

		when(ordemServico.getValorPecasInsumos()).thenReturn(VALOR_PECAS_INSUMOS);

		when(ordemServico.getValorOs()).thenReturn(VALOR_TOTAL_OS);

		when(ordemServicoServico.getDescricao()).thenReturn(DESCRICAO_SERVICO);

		when(ordemServicoServico.getValor()).thenReturn(VALOR_SERVICO);

		when(ordemServicoItemEstoque.getNome()).thenReturn(NOME_PECA);

		when(ordemServicoItemEstoque.getValorUnitario()).thenReturn(VALOR_UNITARIO_PECA);

		when(ordemServicoItemEstoque.getQuantidade()).thenReturn(QUANTIDADE_PECA);

		when(ordemServicoItemEstoque.getValorTotal()).thenReturn(VALOR_TOTAL_PECA);
	}
}