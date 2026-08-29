package com.techchallenger.oficina360.dominio;

import com.techchallenger.oficina360.dominio.shared.exception.DecisaoOrcamentoObrigatoriaException;
import com.techchallenger.oficina360.dominio.shared.exception.DiagnosticoSemServicoException;
import com.techchallenger.oficina360.dominio.shared.exception.InicioExecucaoNaoRegistradoException;
import com.techchallenger.oficina360.dominio.shared.exception.ItemEstoqueInvalidoException;
import com.techchallenger.oficina360.dominio.shared.exception.TransicaoStatusInvalidaException;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.techchallenger.oficina360.enums.OrdemDeServicoStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrdemServicoTest {

	private static final LocalDateTime DATA_ABERTURA = LocalDateTime.of(2026, 7, 19, 11, 0);

	private static final LocalDateTime DATA_INICIO_EXECUCAO = LocalDateTime.of(2026, 7, 19, 12, 0);

	private static final LocalDateTime DATA_FIM_EXECUCAO = LocalDateTime.of(2026, 7, 19, 14, 30);

	private static final LocalDateTime DATA_FECHAMENTO = LocalDateTime.of(2026, 7, 19, 15, 0);

	private OrdemServico ordemServico;

	@BeforeEach
	void setUp() {
		ordemServico = criarOrdemServico(RECEBIDA);
	}

	private OrdemServico criarOrdemServico(OrdemDeServicoStatus status) {
		return criarOrdemServico(status, List.of(), List.of(), null, null);
	}

	private OrdemServico criarOrdemServico(OrdemDeServicoStatus status, LocalDateTime inicioExecucao,
			LocalDateTime fimExecucao) {
		return criarOrdemServico(status, List.of(), List.of(), inicioExecucao, fimExecucao);
	}

	private OrdemServico criarOrdemServico(OrdemDeServicoStatus status, List<OrdemServicoServico> servicos,
			List<OrdemServicoItemEstoque> itensEstoque, LocalDateTime inicioExecucao, LocalDateTime fimExecucao) {
		return new OrdemServico(UUID.randomUUID(), "12345678910", "ABC1D23", DATA_ABERTURA, null, "Barulho ao frear",
				status, null, servicos, itensEstoque, inicioExecucao, fimExecucao);
	}

	private OrdemServicoServico criarServico(BigDecimal valor) {
		return new OrdemServicoServico(UUID.randomUUID(), "Serviço de teste", valor);
	}

	private OrdemServicoItemEstoque criarItemEstoqueComValor(BigDecimal valorTotal) {
		OrdemServicoItemEstoque itemEstoque = mock(OrdemServicoItemEstoque.class);

		when(itemEstoque.getValorTotal()).thenReturn(valorTotal);

		return itemEstoque;
	}

	private OrdemServicoItemEstoque criarItemEstoqueSemValor() {
		return mock(OrdemServicoItemEstoque.class);
	}

	@Nested
	class InicioDiagnostico {

		@Test
		void deveIniciarDiagnosticoQuandoOrdemEstiverRecebida() {
			assertDoesNotThrow(ordemServico::iniciarDiagnostico);

			assertEquals(EM_DIAGNOSTICO, ordemServico.getOrdemDeServicoStatus());
		}

		@Test
		void deveFalharAoIniciarDiagnosticoEmStatusDiferenteDeRecebida() {
			OrdemServico ordem = criarOrdemServico(AGUARDANDO_APROVACAO);

			assertThrows(TransicaoStatusInvalidaException.class, ordem::iniciarDiagnostico);

			assertEquals(AGUARDANDO_APROVACAO, ordem.getOrdemDeServicoStatus());
		}

		@Test
		void naoDeveAlterarEstadoQuandoInicioDoDiagnosticoFalhar() {
			OrdemServico ordem = criarOrdemServico(FINALIZADA);

			assertThrows(TransicaoStatusInvalidaException.class, ordem::iniciarDiagnostico);

			assertEquals(FINALIZADA, ordem.getOrdemDeServicoStatus());
		}
	}

	@Nested
	class AdicaoServicos {

		@Test
		void deveAdicionarServicosNaOrdemEmDiagnostico() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			ordemServico.adicionarServicos(List.of(servico));

			assertAll(() -> assertEquals(1, ordemServico.getServicos().size()),
					() -> assertEquals(servico, ordemServico.getServicos().getFirst()),
					() -> assertEquals(new BigDecimal("10"), ordemServico.getValorServicos()),
					() -> assertEquals(BigDecimal.ZERO, ordemServico.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("10"), ordemServico.getValorOs()));
		}

		@Test
		void deveFalharAoAdicionarListaDeServicosVazia() {
			ordemServico.iniciarDiagnostico();

			List<OrdemServicoServico> servicos = List.of();

			assertThrows(DiagnosticoSemServicoException.class, () -> ordemServico.adicionarServicos(servicos));

			assertTrue(ordemServico.getServicos().isEmpty());

			assertEquals(BigDecimal.ZERO, ordemServico.getValorOs());
		}

		@Test
		void deveFalharAoAdicionarListaDeServicosNula() {
			ordemServico.iniciarDiagnostico();

			assertThrows(DiagnosticoSemServicoException.class, () -> ordemServico.adicionarServicos(null));

			assertTrue(ordemServico.getServicos().isEmpty());

			assertEquals(BigDecimal.ZERO, ordemServico.getValorOs());
		}

		@Test
		void deveFalharQuandoListaDeServicosContiverElementoNulo() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			List<OrdemServicoServico> servicos = Arrays.asList(servico, null);

			assertThrows(DiagnosticoSemServicoException.class, () -> ordemServico.adicionarServicos(servicos));

			assertTrue(ordemServico.getServicos().isEmpty());

			assertEquals(BigDecimal.ZERO, ordemServico.getValorOs());
		}

		@Test
		void deveSomarValoresAoAdicionarMaisDeUmServico() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico primeiroServico = criarServico(new BigDecimal("50.25"));

			OrdemServicoServico segundoServico = criarServico(new BigDecimal("30.75"));

			ordemServico.adicionarServicos(List.of(primeiroServico, segundoServico));

			assertAll(() -> assertEquals(2, ordemServico.getServicos().size()),
					() -> assertEquals(new BigDecimal("81.00"), ordemServico.getValorServicos()),
					() -> assertEquals(new BigDecimal("81.00"), ordemServico.getValorOs()));
		}

		@Test
		void naoDevePermitirAlterarServicosForaDoDiagnostico() {
			OrdemServico ordem = criarOrdemServico(FINALIZADA);

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			List<OrdemServicoServico> servicos = List.of(servico);

			assertThrows(TransicaoStatusInvalidaException.class, () -> ordem.adicionarServicos(servicos));

			assertTrue(ordem.getServicos().isEmpty());
		}
	}

	@Nested
	class AdicaoItensEstoque {

		@Test
		void deveAdicionarItensDeEstoqueValidos() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueComValor(new BigDecimal("45.90"));

			ordemServico.adicionarItensEstoque(List.of(itemEstoque));

			assertAll(() -> assertEquals(1, ordemServico.getItensEstoque().size()),
					() -> assertEquals(itemEstoque, ordemServico.getItensEstoque().getFirst()),
					() -> assertEquals(new BigDecimal("45.90"), ordemServico.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("45.90"), ordemServico.getValorOs()));
		}

		@Test
		void deveFalharAoAdicionarListaDeEstoqueNula() {
			ordemServico.iniciarDiagnostico();

			assertThrows(ItemEstoqueInvalidoException.class, () -> ordemServico.adicionarItensEstoque(null));

			assertTrue(ordemServico.getItensEstoque().isEmpty());
		}

		@Test
		void deveFalharAoAdicionarListaDeEstoqueVazia() {
			ordemServico.iniciarDiagnostico();

			List<OrdemServicoItemEstoque> itensEstoque = List.of();

			assertThrows(ItemEstoqueInvalidoException.class, () -> ordemServico.adicionarItensEstoque(itensEstoque));

			assertTrue(ordemServico.getItensEstoque().isEmpty());
		}

		@Test
		void deveFalharQuandoListaDeEstoqueContiverElementoNulo() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueSemValor();

			List<OrdemServicoItemEstoque> itensEstoque = Arrays.asList(itemEstoque, null);

			assertThrows(ItemEstoqueInvalidoException.class, () -> ordemServico.adicionarItensEstoque(itensEstoque));

			assertTrue(ordemServico.getItensEstoque().isEmpty());
		}

		@Test
		void naoDevePermitirAlterarEstoqueForaDoDiagnostico() {
			OrdemServico ordem = criarOrdemServico(FINALIZADA);

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueSemValor();

			List<OrdemServicoItemEstoque> itensEstoque = List.of(itemEstoque);

			assertThrows(TransicaoStatusInvalidaException.class, () -> ordem.adicionarItensEstoque(itensEstoque));

			assertTrue(ordem.getItensEstoque().isEmpty());
		}

		@Test
		void deveFalharQuandoEstoqueInformadoContiverElementoNulo() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueSemValor();

			List<OrdemServicoItemEstoque> itensEstoque = Arrays.asList(itemEstoque, null);

			List<OrdemServicoServico> servicos = List.of(servico);

			assertThrows(ItemEstoqueInvalidoException.class,
					() -> ordemServico.adicionarDiagnostico(servicos, itensEstoque));

			assertAll(() -> assertTrue(ordemServico.getServicos().isEmpty()),
					() -> assertTrue(ordemServico.getItensEstoque().isEmpty()),
					() -> assertEquals(BigDecimal.ZERO, ordemServico.getValorOs()));
		}

		@Test
		void deveManterDiagnosticoAtualQuandoNovoDiagnosticoContiverItemInvalido() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servicoAtual = criarServico(new BigDecimal("100.00"));

			OrdemServicoItemEstoque itemEstoqueAtual = criarItemEstoqueComValor(new BigDecimal("50.00"));

			ordemServico.adicionarDiagnostico(List.of(servicoAtual), List.of(itemEstoqueAtual));

			OrdemServicoServico novoServico = criarServico(new BigDecimal("200.00"));

			OrdemServicoItemEstoque novoItemEstoque = criarItemEstoqueSemValor();

			List<OrdemServicoItemEstoque> novosItensInvalidos = Arrays.asList(novoItemEstoque, null);

			List<OrdemServicoServico> novosServicos = List.of(novoServico);

			assertThrows(ItemEstoqueInvalidoException.class,
					() -> ordemServico.adicionarDiagnostico(novosServicos, novosItensInvalidos));

			assertAll(() -> assertEquals(List.of(servicoAtual), ordemServico.getServicos()),
					() -> assertEquals(List.of(itemEstoqueAtual), ordemServico.getItensEstoque()),
					() -> assertEquals(new BigDecimal("100.00"), ordemServico.getValorServicos()),
					() -> assertEquals(new BigDecimal("50.00"), ordemServico.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("150.00"), ordemServico.getValorOs()));
		}
	}

	@Nested
	class AdicaoDiagnostico {

		@Test
		void deveAdicionarDiagnosticoComServicosEEstoque() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(new BigDecimal("100.00"));

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueComValor(new BigDecimal("40.00"));

			ordemServico.adicionarDiagnostico(List.of(servico), List.of(itemEstoque));

			assertAll(() -> assertEquals(1, ordemServico.getServicos().size()),
					() -> assertEquals(1, ordemServico.getItensEstoque().size()),
					() -> assertEquals(new BigDecimal("100.00"), ordemServico.getValorServicos()),
					() -> assertEquals(new BigDecimal("40.00"), ordemServico.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("140.00"), ordemServico.getValorOs()));
		}

		@Test
		void deveAdicionarDiagnosticoSemEstoque() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(new BigDecimal("100.00"));

			List<OrdemServicoServico> servicos = List.of(servico);

			assertDoesNotThrow(() -> ordemServico.adicionarDiagnostico(servicos, null));

			assertAll(() -> assertEquals(1, ordemServico.getServicos().size()),
					() -> assertTrue(ordemServico.getItensEstoque().isEmpty()),
					() -> assertEquals(new BigDecimal("100.00"), ordemServico.getValorOs()));
		}

		@Test
		void deveAdicionarDiagnosticoComListaDeEstoqueVazia() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(new BigDecimal("100.00"));

			List<OrdemServicoServico> servicos = List.of(servico);

			List<OrdemServicoItemEstoque> itensEstoque = List.of();

			assertDoesNotThrow(() -> ordemServico.adicionarDiagnostico(servicos, itensEstoque));

			assertTrue(ordemServico.getItensEstoque().isEmpty());

			assertEquals(new BigDecimal("100.00"), ordemServico.getValorOs());
		}

		@Test
		void deveFalharQuandoEstoqueOpcionalContiverElementoInvalido() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueSemValor();

			List<OrdemServicoItemEstoque> itensEstoque = Arrays.asList(itemEstoque, null);

			List<OrdemServicoServico> servicos = List.of(servico);

			assertThrows(ItemEstoqueInvalidoException.class,
					() -> ordemServico.adicionarDiagnostico(servicos, itensEstoque));

			assertAll(() -> assertTrue(ordemServico.getServicos().isEmpty()),
					() -> assertTrue(ordemServico.getItensEstoque().isEmpty()),
					() -> assertEquals(BigDecimal.ZERO, ordemServico.getValorOs()));
		}

		@Test
		void deveFalharAoAdicionarDiagnosticoSemServico() {
			ordemServico.iniciarDiagnostico();

			List<OrdemServicoServico> servicos = List.of();

			assertThrows(DiagnosticoSemServicoException.class, () -> ordemServico.adicionarDiagnostico(servicos, null));

			assertTrue(ordemServico.getServicos().isEmpty());
		}

		@Test
		void deveFalharAoAdicionarDiagnosticoForaDoStatusCorreto() {
			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			List<OrdemServicoServico> servicos = List.of(servico);

			assertThrows(TransicaoStatusInvalidaException.class,
					() -> ordemServico.adicionarDiagnostico(servicos, null));

			assertTrue(ordemServico.getServicos().isEmpty());
		}

		@Test
		void deveSubstituirDiagnosticoExistente() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servicoAnterior = criarServico(new BigDecimal("50.00"));

			OrdemServicoItemEstoque itemEstoqueAnterior = criarItemEstoqueComValor(new BigDecimal("20.00"));

			ordemServico.adicionarDiagnostico(List.of(servicoAnterior), List.of(itemEstoqueAnterior));

			OrdemServicoServico novoServico = criarServico(new BigDecimal("150.00"));

			ordemServico.adicionarDiagnostico(List.of(novoServico), null);

			assertAll(() -> assertEquals(List.of(novoServico), ordemServico.getServicos()),
					() -> assertTrue(ordemServico.getItensEstoque().isEmpty()),
					() -> assertEquals(new BigDecimal("150.00"), ordemServico.getValorServicos()),
					() -> assertEquals(BigDecimal.ZERO, ordemServico.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("150.00"), ordemServico.getValorOs()));
		}

		@Test
		void deveManterDiagnosticoAtualQuandoSubstituicaoForInvalida() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servicoAtual = criarServico(new BigDecimal("100.00"));

			OrdemServicoItemEstoque itemEstoqueAtual = criarItemEstoqueComValor(new BigDecimal("50.00"));

			ordemServico.adicionarDiagnostico(List.of(servicoAtual), List.of(itemEstoqueAtual));

			OrdemServicoServico novoServico = criarServico(new BigDecimal("999.00"));

			OrdemServicoItemEstoque novoItemEstoque = criarItemEstoqueSemValor();

			List<OrdemServicoItemEstoque> itensInvalidos = Arrays.asList(novoItemEstoque, null);

			List<OrdemServicoServico> novosServicos = List.of(novoServico);

			assertThrows(ItemEstoqueInvalidoException.class,
					() -> ordemServico.adicionarDiagnostico(novosServicos, itensInvalidos));

			assertAll(() -> assertEquals(List.of(servicoAtual), ordemServico.getServicos()),
					() -> assertEquals(List.of(itemEstoqueAtual), ordemServico.getItensEstoque()),
					() -> assertEquals(new BigDecimal("100.00"), ordemServico.getValorServicos()),
					() -> assertEquals(new BigDecimal("50.00"), ordemServico.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("150.00"), ordemServico.getValorOs()));
		}
	}

	@Nested
	class FinalizacaoDiagnostico {

		@Test
		void deveFinalizarDiagnosticoComServicoDefinido() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(new BigDecimal("120.00"));

			ordemServico.adicionarDiagnostico(List.of(servico), null);

			ordemServico.finalizarDiagnostico();

			assertEquals(AGUARDANDO_APROVACAO, ordemServico.getOrdemDeServicoStatus());

			assertEquals(new BigDecimal("120.00"), ordemServico.getValorOs());
		}

		@Test
		void deveFalharAoFinalizarDiagnosticoSemServico() {
			ordemServico.iniciarDiagnostico();

			assertThrows(DiagnosticoSemServicoException.class, ordemServico::finalizarDiagnostico);

			assertEquals(EM_DIAGNOSTICO, ordemServico.getOrdemDeServicoStatus());
		}

		@Test
		void deveFalharAoFinalizarDiagnosticoForaDoStatusCorreto() {
			assertThrows(TransicaoStatusInvalidaException.class, ordemServico::finalizarDiagnostico);

			assertEquals(RECEBIDA, ordemServico.getOrdemDeServicoStatus());
		}
	}

	@Nested
	class AprovacaoOrcamento {

		@Test
		void deveAprovarOrcamento() {
			OrdemServico ordem = criarOrdemServico(AGUARDANDO_APROVACAO);

			ordem.registrarAprovacao(true);

			assertEquals(ORCAMENTO_APROVADO, ordem.getOrdemDeServicoStatus());
		}

		@Test
		void deveReprovarOrcamento() {
			OrdemServico ordem = criarOrdemServico(AGUARDANDO_APROVACAO);

			ordem.registrarAprovacao(false);

			assertEquals(ORCAMENTO_REPROVADO, ordem.getOrdemDeServicoStatus());
		}

		@Test
		void deveFalharQuandoDecisaoForNula() {
			OrdemServico ordem = criarOrdemServico(AGUARDANDO_APROVACAO);

			assertThrows(DecisaoOrcamentoObrigatoriaException.class, () -> ordem.registrarAprovacao(null));

			assertEquals(AGUARDANDO_APROVACAO, ordem.getOrdemDeServicoStatus());
		}

		@Test
		void deveFalharAoRegistrarAprovacaoForaDoStatusCorreto() {
			assertThrows(TransicaoStatusInvalidaException.class, () -> ordemServico.registrarAprovacao(true));

			assertEquals(RECEBIDA, ordemServico.getOrdemDeServicoStatus());
		}
	}

	@Nested
	class Execucao {

		@Test
		void deveIniciarExecucaoComOrcamentoAprovado() {
			OrdemServico ordem = criarOrdemServico(ORCAMENTO_APROVADO);

			ordem.iniciarExecucao(DATA_INICIO_EXECUCAO);

			assertAll(() -> assertEquals(EM_EXECUCAO, ordem.getOrdemDeServicoStatus()),
					() -> assertEquals(DATA_INICIO_EXECUCAO, ordem.getDtHoraInicioExecucao()),
					() -> assertNull(ordem.getDtHoraFimExecucao()));
		}

		@Test
		void deveFalharAoIniciarExecucaoSemOrcamentoAprovado() {
			OrdemServico ordem = criarOrdemServico(AGUARDANDO_APROVACAO);

			assertThrows(TransicaoStatusInvalidaException.class, () -> ordem.iniciarExecucao(DATA_INICIO_EXECUCAO));

			assertAll(() -> assertEquals(AGUARDANDO_APROVACAO, ordem.getOrdemDeServicoStatus()),
					() -> assertNull(ordem.getDtHoraInicioExecucao()));
		}

		@Test
		void deveFinalizarExecucaoIniciada() {
			OrdemServico ordem = criarOrdemServico(ORCAMENTO_APROVADO);

			ordem.iniciarExecucao(DATA_INICIO_EXECUCAO);

			ordem.finalizarExecucao(DATA_FIM_EXECUCAO);

			assertAll(() -> assertEquals(FINALIZADA, ordem.getOrdemDeServicoStatus()),
					() -> assertEquals(DATA_INICIO_EXECUCAO, ordem.getDtHoraInicioExecucao()),
					() -> assertEquals(DATA_FIM_EXECUCAO, ordem.getDtHoraFimExecucao()),
					() -> assertFalse(ordem.getDtHoraFimExecucao().isBefore(ordem.getDtHoraInicioExecucao())));
		}

		@Test
		void deveFalharAoFinalizarExecucaoForaDoStatusCorreto() {
			OrdemServico ordem = criarOrdemServico(ORCAMENTO_APROVADO);

			assertThrows(TransicaoStatusInvalidaException.class, () -> ordem.finalizarExecucao(DATA_FIM_EXECUCAO));

			assertEquals(ORCAMENTO_APROVADO, ordem.getOrdemDeServicoStatus());
		}

		@Test
		void deveFalharQuandoExecucaoNaoPossuirDataDeInicio() {
			OrdemServico ordem = criarOrdemServico(EM_EXECUCAO, null, null);

			assertThrows(InicioExecucaoNaoRegistradoException.class, () -> ordem.finalizarExecucao(DATA_FIM_EXECUCAO));

			assertEquals(EM_EXECUCAO, ordem.getOrdemDeServicoStatus());

			assertNull(ordem.getDtHoraFimExecucao());
		}
	}

	@Nested
	class Entrega {

		@Test
		void deveEntregarOrdemFinalizada() {
			OrdemServico ordem = criarOrdemServico(FINALIZADA);

			ordem.entregar(DATA_FECHAMENTO);

			assertAll(() -> assertEquals(ENTREGUE, ordem.getOrdemDeServicoStatus()),
					() -> assertEquals(DATA_FECHAMENTO, ordem.getDtHoraFechamento()));
		}

		@Test
		void deveFalharAoEntregarOrdemNaoFinalizada() {
			OrdemServico ordem = criarOrdemServico(EM_EXECUCAO);

			assertThrows(TransicaoStatusInvalidaException.class, () -> ordem.entregar(DATA_FECHAMENTO));

			assertAll(() -> assertEquals(EM_EXECUCAO, ordem.getOrdemDeServicoStatus()),
					() -> assertNull(ordem.getDtHoraFechamento()));
		}
	}

	@Nested
	class CalculoValores {

		@Test
		void deveCalcularTotalDeServicosEEstoque() {
			ordemServico.iniciarDiagnostico();

			List<OrdemServicoServico> servicos = List.of(criarServico(new BigDecimal("100.00")),
					criarServico(new BigDecimal("75.50")));

			List<OrdemServicoItemEstoque> itensEstoque = List.of(criarItemEstoqueComValor(new BigDecimal("20.25")),
					criarItemEstoqueComValor(new BigDecimal("4.25")));

			ordemServico.adicionarDiagnostico(servicos, itensEstoque);

			assertAll(() -> assertEquals(new BigDecimal("175.50"), ordemServico.getValorServicos()),
					() -> assertEquals(new BigDecimal("24.50"), ordemServico.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("200.00"), ordemServico.getValorOs()));
		}

		@Test
		void deveCalcularZeroQuandoNaoExistiremItens() {
			assertAll(() -> assertEquals(BigDecimal.ZERO, ordemServico.getValorServicos()),
					() -> assertEquals(BigDecimal.ZERO, ordemServico.getValorPecasInsumos()),
					() -> assertEquals(BigDecimal.ZERO, ordemServico.getValorOs()));
		}

		@Test
		void deveRecalcularValoresAoReconstituirOrdem() {
			OrdemServicoServico servico = criarServico(new BigDecimal("80.00"));

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueComValor(new BigDecimal("20.00"));

			OrdemServico ordem = criarOrdemServico(EM_DIAGNOSTICO, List.of(servico), List.of(itemEstoque), null, null);

			assertAll(() -> assertEquals(new BigDecimal("80.00"), ordem.getValorServicos()),
					() -> assertEquals(new BigDecimal("20.00"), ordem.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("100.00"), ordem.getValorOs()));
		}
	}

	@Nested
	class Encapsulamento {

		@Test
		void naoDevePermitirAlterarListaDeServicosPeloGetter() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			ordemServico.adicionarServicos(List.of(servico));

			List<OrdemServicoServico> servicos = ordemServico.getServicos();

			assertThrows(UnsupportedOperationException.class, servicos::clear);

			assertEquals(1, ordemServico.getServicos().size());
		}

		@Test
		void naoDevePermitirAlterarListaDeEstoquePeloGetter() {
			ordemServico.iniciarDiagnostico();

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueComValor(BigDecimal.TEN);

			ordemServico.adicionarItensEstoque(List.of(itemEstoque));

			List<OrdemServicoItemEstoque> itensEstoque = ordemServico.getItensEstoque();

			assertThrows(UnsupportedOperationException.class, itensEstoque::clear);

			assertEquals(1, ordemServico.getItensEstoque().size());
		}

		@Test
		void construtorDeveFazerCopiaDefensivaDasColecoes() {
			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueComValor(BigDecimal.ONE);

			List<OrdemServicoServico> servicos = new ArrayList<>(List.of(servico));

			List<OrdemServicoItemEstoque> itensEstoque = new ArrayList<>(List.of(itemEstoque));

			OrdemServico ordem = criarOrdemServico(EM_DIAGNOSTICO, servicos, itensEstoque, null, null);

			servicos.clear();
			itensEstoque.clear();

			assertAll(() -> assertEquals(1, ordem.getServicos().size()),
					() -> assertEquals(1, ordem.getItensEstoque().size()),
					() -> assertEquals(new BigDecimal("11"), ordem.getValorOs()));
		}
	}

	@Nested
	class CicloDeVida {

		@Test
		void deveExecutarCicloCompletoDaOrdemDeServico() {
			OrdemServicoServico servico = criarServico(new BigDecimal("150.00"));

			OrdemServicoItemEstoque itemEstoque = criarItemEstoqueComValor(new BigDecimal("50.00"));

			assertEquals(RECEBIDA, ordemServico.getOrdemDeServicoStatus());

			ordemServico.iniciarDiagnostico();

			assertEquals(EM_DIAGNOSTICO, ordemServico.getOrdemDeServicoStatus());

			ordemServico.adicionarDiagnostico(List.of(servico), List.of(itemEstoque));

			ordemServico.finalizarDiagnostico();

			assertEquals(AGUARDANDO_APROVACAO, ordemServico.getOrdemDeServicoStatus());

			ordemServico.registrarAprovacao(true);

			assertEquals(ORCAMENTO_APROVADO, ordemServico.getOrdemDeServicoStatus());

			ordemServico.iniciarExecucao(DATA_INICIO_EXECUCAO);

			assertEquals(EM_EXECUCAO, ordemServico.getOrdemDeServicoStatus());

			ordemServico.finalizarExecucao(DATA_FIM_EXECUCAO);

			assertEquals(FINALIZADA, ordemServico.getOrdemDeServicoStatus());

			ordemServico.entregar(DATA_FECHAMENTO);

			assertAll(() -> assertEquals(ENTREGUE, ordemServico.getOrdemDeServicoStatus()),
					() -> assertEquals(new BigDecimal("200.00"), ordemServico.getValorOs()),
					() -> assertEquals(DATA_INICIO_EXECUCAO, ordemServico.getDtHoraInicioExecucao()),
					() -> assertEquals(DATA_FIM_EXECUCAO, ordemServico.getDtHoraFimExecucao()),
					() -> assertEquals(DATA_FECHAMENTO, ordemServico.getDtHoraFechamento()));
		}
	}
}