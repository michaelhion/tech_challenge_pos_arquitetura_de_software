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

	private OrdemServico os;

	@BeforeEach
	void setup() {
		os = criarOrdemServico(RECEBIDA);
	}

	@Nested
	class InicioDiagnostico {

		@Test
		void deveIniciarDiagnosticoQuandoOrdemEstiverRecebida() {
			assertDoesNotThrow(os::iniciarDiagnostico);

			assertEquals(EM_DIAGNOSTICO, os.getOrdemDeServicoStatus());
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
			os.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			os.adicionarServicos(List.of(servico));

			assertAll(() -> assertEquals(1, os.getServicos().size()),
					() -> assertEquals(servico, os.getServicos().getFirst()),
					() -> assertEquals(new BigDecimal("10"), os.getValorServicos()),
					() -> assertEquals(BigDecimal.ZERO, os.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("10"), os.getValorOs()));
		}

		@Test
		void deveFalharAoAdicionarListaDeServicosVazia() {
			os.iniciarDiagnostico();

			assertThrows(DiagnosticoSemServicoException.class, () -> os.adicionarServicos(List.of()));

			assertTrue(os.getServicos().isEmpty());
			assertEquals(BigDecimal.ZERO, os.getValorOs());
		}

		@Test
		void deveFalharAoAdicionarListaDeServicosNula() {
			os.iniciarDiagnostico();

			assertThrows(DiagnosticoSemServicoException.class, () -> os.adicionarServicos(null));

			assertTrue(os.getServicos().isEmpty());
			assertEquals(BigDecimal.ZERO, os.getValorOs());
		}

		@Test
		void deveFalharQuandoListaDeServicosContiverElementoNulo() {
			os.iniciarDiagnostico();

			List<OrdemServicoServico> servicos = Arrays.asList(criarServico(BigDecimal.TEN), null);
			assertThrows(DiagnosticoSemServicoException.class, () -> os.adicionarServicos(servicos));

			assertTrue(os.getServicos().isEmpty());
			assertEquals(BigDecimal.ZERO, os.getValorOs());
		}

		@Test
		void deveSomarValoresAoAdicionarMaisDeUmServico() {
			os.iniciarDiagnostico();

			OrdemServicoServico primeiro = criarServico(new BigDecimal("50.25"));

			OrdemServicoServico segundo = criarServico(new BigDecimal("30.75"));

			os.adicionarServicos(List.of(primeiro, segundo));

			assertAll(() -> assertEquals(2, os.getServicos().size()),
					() -> assertEquals(new BigDecimal("81.00"), os.getValorServicos()),
					() -> assertEquals(new BigDecimal("81.00"), os.getValorOs()));
		}

		@Test
		void naoDevePermitirAlterarServicosForaDoDiagnostico() {
			OrdemServico ordem = criarOrdemServico(FINALIZADA);
			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			assertThrows(TransicaoStatusInvalidaException.class, () -> ordem.adicionarServicos(List.of(servico)));

			assertTrue(ordem.getServicos().isEmpty());
		}
	}

	@Nested
	class AdicaoItensEstoque {

		@Test
		void deveAdicionarItensDeEstoqueValidos() {
			os.iniciarDiagnostico();

			OrdemServicoItemEstoque item = criarItemEstoque(new BigDecimal("45.90"));

			os.adicionarItensEstoque(List.of(item));

			assertAll(() -> assertEquals(1, os.getItensEstoque().size()),
					() -> assertEquals(item, os.getItensEstoque().getFirst()),
					() -> assertEquals(new BigDecimal("45.90"), os.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("45.90"), os.getValorOs()));
		}

		@Test
		void deveFalharAoAdicionarListaDeEstoqueNula() {
			os.iniciarDiagnostico();

			assertThrows(ItemEstoqueInvalidoException.class, () -> os.adicionarItensEstoque(null));

			assertTrue(os.getItensEstoque().isEmpty());
		}

		@Test
		void deveFalharAoAdicionarListaDeEstoqueVazia() {
			os.iniciarDiagnostico();

			assertThrows(ItemEstoqueInvalidoException.class, () -> os.adicionarItensEstoque(List.of()));

			assertTrue(os.getItensEstoque().isEmpty());
		}

		@Test
		void deveFalharQuandoListaDeEstoqueContiverElementoNulo() {
			os.iniciarDiagnostico();

			List<OrdemServicoItemEstoque> itens = Arrays.asList(criarItemEstoque(BigDecimal.TEN), null);

			assertThrows(ItemEstoqueInvalidoException.class, () -> os.adicionarItensEstoque(itens));

			assertTrue(os.getItensEstoque().isEmpty());
		}

		@Test
		void naoDevePermitirAlterarEstoqueForaDoDiagnostico() {
			OrdemServico ordem = criarOrdemServico(FINALIZADA);
			OrdemServicoItemEstoque item = criarItemEstoque(BigDecimal.TEN);

			assertThrows(TransicaoStatusInvalidaException.class, () -> ordem.adicionarItensEstoque(List.of(item)));

			assertTrue(ordem.getItensEstoque().isEmpty());
		}

		@Test
		void deveFalharQuandoEstoqueInformadoContiverElementoNulo() {
			os.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			OrdemServicoItemEstoque item = criarItemEstoque(BigDecimal.ONE);

			List<OrdemServicoItemEstoque> itens = Arrays.asList(item, null);

			assertThrows(ItemEstoqueInvalidoException.class, () -> os.adicionarDiagnostico(List.of(servico), itens));

			assertAll(
					() -> assertTrue(os.getServicos().isEmpty()),
					() -> assertTrue(os.getItensEstoque().isEmpty()),
					() -> assertEquals(BigDecimal.ZERO, os.getValorOs()));
		}

		@Test
		void devePreservarDiagnosticoAtualQuandoNovoDiagnosticoForInvalido() {
			os.iniciarDiagnostico();

			OrdemServicoServico servicoAtual = criarServico(new BigDecimal("100.00"));

			OrdemServicoItemEstoque itemAtual = criarItemEstoque(new BigDecimal("50.00"));

			os.adicionarDiagnostico(List.of(servicoAtual),List.of(itemAtual));

			OrdemServicoServico novoServico = criarServico(new BigDecimal("200.00"));

			OrdemServicoItemEstoque novoItem = criarItemEstoque(new BigDecimal("80.00"));

			List<OrdemServicoItemEstoque> novosItensInvalidos = Arrays.asList(novoItem, null);

			assertThrows(
					ItemEstoqueInvalidoException.class,
					() -> os.adicionarDiagnostico(
							List.of(novoServico),
							novosItensInvalidos)
			);

			assertAll(
					() -> assertEquals(List.of(servicoAtual),os.getServicos()),
					() -> assertEquals(List.of(itemAtual),os.getItensEstoque()),
					() -> assertEquals(new BigDecimal("100.00"),os.getValorServicos()),
					() -> assertEquals(new BigDecimal("50.00"),os.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("150.00"),os.getValorOs())
			);
		}
	}

	@Nested
	class AdicaoDiagnostico {

		@Test
		void deveAdicionarDiagnosticoComServicosEEstoque() {
			os.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(new BigDecimal("100.00"));

			OrdemServicoItemEstoque item = criarItemEstoque(new BigDecimal("40.00"));

			os.adicionarDiagnostico(List.of(servico), List.of(item));

			assertAll(() -> assertEquals(1, os.getServicos().size()),
					() -> assertEquals(1, os.getItensEstoque().size()),
					() -> assertEquals(new BigDecimal("100.00"), os.getValorServicos()),
					() -> assertEquals(new BigDecimal("40.00"), os.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("140.00"), os.getValorOs()));
		}

		@Test
		void deveAdicionarDiagnosticoSemEstoque() {
			os.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(new BigDecimal("100.00"));

			assertDoesNotThrow(() -> os.adicionarDiagnostico(List.of(servico), null));

			assertAll(() -> assertEquals(1, os.getServicos().size()), () -> assertTrue(os.getItensEstoque().isEmpty()),
					() -> assertEquals(new BigDecimal("100.00"), os.getValorOs()));
		}

		@Test
		void deveAdicionarDiagnosticoComListaDeEstoqueVazia() {
			os.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(new BigDecimal("100.00"));

			assertDoesNotThrow(() -> os.adicionarDiagnostico(List.of(servico), List.of()));

			assertTrue(os.getItensEstoque().isEmpty());
			assertEquals(new BigDecimal("100.00"), os.getValorOs());
		}

		@Test
		void deveFalharQuandoEstoqueOpcionalForInformadoComElementoInvalido() {
			os.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			List<OrdemServicoItemEstoque> itens = Arrays.asList(criarItemEstoque(BigDecimal.ONE), null);

			assertThrows(ItemEstoqueInvalidoException.class, () -> os.adicionarDiagnostico(List.of(servico), itens));

			assertAll(() -> assertTrue(os.getServicos().isEmpty()), () -> assertTrue(os.getItensEstoque().isEmpty()),
					() -> assertEquals(BigDecimal.ZERO, os.getValorOs()));
		}

		@Test
		void deveFalharAoAdicionarDiagnosticoSemServico() {
			os.iniciarDiagnostico();

			assertThrows(DiagnosticoSemServicoException.class, () -> os.adicionarDiagnostico(List.of(), null));

			assertTrue(os.getServicos().isEmpty());
		}

		@Test
		void deveFalharAoAdicionarDiagnosticoForaDoStatusCorreto() {
			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			assertThrows(TransicaoStatusInvalidaException.class, () -> os.adicionarDiagnostico(List.of(servico), null));

			assertTrue(os.getServicos().isEmpty());
		}

		@Test
		void deveSubstituirDiagnosticoExistente() {
			os.iniciarDiagnostico();

			OrdemServicoServico servicoAnterior = criarServico(new BigDecimal("50.00"));

			OrdemServicoItemEstoque itemAnterior = criarItemEstoque(new BigDecimal("20.00"));

			os.adicionarDiagnostico(List.of(servicoAnterior), List.of(itemAnterior));

			OrdemServicoServico novoServico = criarServico(new BigDecimal("150.00"));

			os.adicionarDiagnostico(List.of(novoServico), null);

			assertAll(() -> assertEquals(List.of(novoServico), os.getServicos()),
					() -> assertTrue(os.getItensEstoque().isEmpty()),
					() -> assertEquals(new BigDecimal("150.00"), os.getValorServicos()),
					() -> assertEquals(BigDecimal.ZERO, os.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("150.00"), os.getValorOs()));
		}

		@Test
		void naoDeveApagarDiagnosticoAtualQuandoNovoDiagnosticoForInvalido() {
			os.iniciarDiagnostico();

			OrdemServicoServico servicoAtual = criarServico(new BigDecimal("100.00"));

			OrdemServicoItemEstoque itemAtual = criarItemEstoque(new BigDecimal("50.00"));

			os.adicionarDiagnostico(List.of(servicoAtual), List.of(itemAtual));

			List<OrdemServicoItemEstoque> itensInvalidos = Arrays.asList(itemAtual, null);

			assertThrows(ItemEstoqueInvalidoException.class,
					() -> os.adicionarDiagnostico(List.of(criarServico(new BigDecimal("999.00"))), itensInvalidos));

			assertAll(() -> assertEquals(List.of(servicoAtual), os.getServicos()),
					() -> assertEquals(List.of(itemAtual), os.getItensEstoque()),
					() -> assertEquals(new BigDecimal("150.00"), os.getValorOs()));
		}
	}

	@Nested
	class FinalizacaoDiagnostico {

		@Test
		void deveFinalizarDiagnosticoComServicoDefinido() {
			os.iniciarDiagnostico();

			os.adicionarDiagnostico(List.of(criarServico(new BigDecimal("120.00"))), null);

			os.finalizarDiagnostico();

			assertEquals(AGUARDANDO_APROVACAO, os.getOrdemDeServicoStatus());

			assertEquals(new BigDecimal("120.00"), os.getValorOs());
		}

		@Test
		void deveFalharAoFinalizarDiagnosticoSemServico() {
			os.iniciarDiagnostico();

			assertThrows(DiagnosticoSemServicoException.class, os::finalizarDiagnostico);

			assertEquals(EM_DIAGNOSTICO, os.getOrdemDeServicoStatus());
		}

		@Test
		void deveFalharAoFinalizarDiagnosticoForaDoStatusCorreto() {
			assertThrows(TransicaoStatusInvalidaException.class, os::finalizarDiagnostico);

			assertEquals(RECEBIDA, os.getOrdemDeServicoStatus());
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
			assertThrows(TransicaoStatusInvalidaException.class, () -> os.registrarAprovacao(true));

			assertEquals(RECEBIDA, os.getOrdemDeServicoStatus());
		}
	}

	@Nested
	class Execucao {

		@Test
		void deveIniciarExecucaoComOrcamentoAprovado() {
			OrdemServico ordem = criarOrdemServico(ORCAMENTO_APROVADO);

			LocalDateTime antes = LocalDateTime.now();

			ordem.iniciarExecucao();

			LocalDateTime depois = LocalDateTime.now();

			assertAll(() -> assertEquals(EM_EXECUCAO, ordem.getOrdemDeServicoStatus()),
					() -> assertNotNull(ordem.getDtHoraInicioExecucao()),
					() -> assertNull(ordem.getDtHoraFimExecucao()),
					() -> assertFalse(ordem.getDtHoraInicioExecucao().isBefore(antes)),
					() -> assertFalse(ordem.getDtHoraInicioExecucao().isAfter(depois)));
		}

		@Test
		void deveFalharAoIniciarExecucaoSemOrcamentoAprovado() {
			OrdemServico ordem = criarOrdemServico(AGUARDANDO_APROVACAO);

			assertThrows(TransicaoStatusInvalidaException.class, ordem::iniciarExecucao);

			assertAll(() -> assertEquals(AGUARDANDO_APROVACAO, ordem.getOrdemDeServicoStatus()),
					() -> assertNull(ordem.getDtHoraInicioExecucao()));
		}

		@Test
		void deveFinalizarExecucaoIniciada() {
			OrdemServico ordem = criarOrdemServico(ORCAMENTO_APROVADO);

			ordem.iniciarExecucao();
			ordem.finalizarExecucao();

			assertAll(() -> assertEquals(FINALIZADA, ordem.getOrdemDeServicoStatus()),
					() -> assertNotNull(ordem.getDtHoraInicioExecucao()),
					() -> assertNotNull(ordem.getDtHoraFimExecucao()),
					() -> assertFalse(ordem.getDtHoraFimExecucao().isBefore(ordem.getDtHoraInicioExecucao())));
		}

		@Test
		void deveFalharAoFinalizarExecucaoForaDoStatusCorreto() {
			OrdemServico ordem = criarOrdemServico(ORCAMENTO_APROVADO);

			assertThrows(TransicaoStatusInvalidaException.class, ordem::finalizarExecucao);

			assertEquals(ORCAMENTO_APROVADO, ordem.getOrdemDeServicoStatus());
		}

		@Test
		void deveFalharQuandoExecucaoNaoPossuirDataDeInicio() {
			OrdemServico ordem = criarOrdemServico(EM_EXECUCAO, null, null);

			assertThrows(InicioExecucaoNaoRegistradoException.class, ordem::finalizarExecucao);

			assertEquals(EM_EXECUCAO, ordem.getOrdemDeServicoStatus());

			assertNull(ordem.getDtHoraFimExecucao());
		}
	}

	@Nested
	class Entrega {

		@Test
		void deveEntregarOrdemFinalizada() {
			OrdemServico ordem = criarOrdemServico(FINALIZADA);

			LocalDateTime antes = LocalDateTime.now();

			ordem.entregar();

			LocalDateTime depois = LocalDateTime.now();

			assertAll(() -> assertEquals(ENTREGUE, ordem.getOrdemDeServicoStatus()),
					() -> assertNotNull(ordem.getDtHoraFechamento()),
					() -> assertFalse(ordem.getDtHoraFechamento().isBefore(antes)),
					() -> assertFalse(ordem.getDtHoraFechamento().isAfter(depois)));
		}

		@Test
		void deveFalharAoEntregarOrdemNaoFinalizada() {
			OrdemServico ordem = criarOrdemServico(EM_EXECUCAO);

			assertThrows(TransicaoStatusInvalidaException.class, ordem::entregar);

			assertAll(() -> assertEquals(EM_EXECUCAO, ordem.getOrdemDeServicoStatus()),
					() -> assertNull(ordem.getDtHoraFechamento()));
		}
	}

	@Nested
	class CalculoValores {

		@Test
		void deveCalcularTotalDeServicosEEstoque() {
			os.iniciarDiagnostico();

			List<OrdemServicoServico> servicos = List.of(criarServico(new BigDecimal("100.00")),
					criarServico(new BigDecimal("75.50")));

			List<OrdemServicoItemEstoque> itens = List.of(criarItemEstoque(new BigDecimal("20.25")),
					criarItemEstoque(new BigDecimal("4.25")));

			os.adicionarDiagnostico(servicos, itens);

			assertAll(() -> assertEquals(new BigDecimal("175.50"), os.getValorServicos()),
					() -> assertEquals(new BigDecimal("24.50"), os.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("200.00"), os.getValorOs()));
		}

		@Test
		void deveCalcularZeroQuandoNaoExistiremItens() {
			assertAll(() -> assertEquals(BigDecimal.ZERO, os.getValorServicos()),
					() -> assertEquals(BigDecimal.ZERO, os.getValorPecasInsumos()),
					() -> assertEquals(BigDecimal.ZERO, os.getValorOs()));
		}

		@Test
		void deveRecalcularValoresAoReconstituirOrdem() {
			OrdemServicoServico servico = criarServico(new BigDecimal("80.00"));

			OrdemServicoItemEstoque item = criarItemEstoque(new BigDecimal("20.00"));

			OrdemServico ordem = criarOrdemServico(EM_DIAGNOSTICO, List.of(servico), List.of(item), null, null);

			assertAll(() -> assertEquals(new BigDecimal("80.00"), ordem.getValorServicos()),
					() -> assertEquals(new BigDecimal("20.00"), ordem.getValorPecasInsumos()),
					() -> assertEquals(new BigDecimal("100.00"), ordem.getValorOs()));
		}
	}

	@Nested
	class Encapsulamento {

		@Test
		void naoDevePermitirAlterarListaDeServicosPeloGetter() {
			os.iniciarDiagnostico();

			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			os.adicionarServicos(List.of(servico));

			List<OrdemServicoServico> servicos = os.getServicos();

			assertThrows(UnsupportedOperationException.class, servicos::clear);

			assertEquals(1, os.getServicos().size());
		}

		@Test
		void naoDevePermitirAlterarListaDeEstoquePeloGetter() {
			os.iniciarDiagnostico();

			OrdemServicoItemEstoque item = criarItemEstoque(BigDecimal.TEN);

			os.adicionarItensEstoque(List.of(item));

			List<OrdemServicoItemEstoque> itens = os.getItensEstoque();

			assertThrows(UnsupportedOperationException.class, itens::clear);

			assertEquals(1, os.getItensEstoque().size());
		}

		@Test
		void construtorDeveFazerCopiaDefensivaDasColecoes() {
			OrdemServicoServico servico = criarServico(BigDecimal.TEN);

			OrdemServicoItemEstoque item = criarItemEstoque(BigDecimal.ONE);

			List<OrdemServicoServico> servicos = new ArrayList<>(List.of(servico));

			List<OrdemServicoItemEstoque> itens = new ArrayList<>(List.of(item));

			OrdemServico ordem = criarOrdemServico(EM_DIAGNOSTICO, servicos, itens, null, null);

			servicos.clear();
			itens.clear();

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

			OrdemServicoItemEstoque item = criarItemEstoque(new BigDecimal("50.00"));

			assertEquals(RECEBIDA, os.getOrdemDeServicoStatus());

			os.iniciarDiagnostico();

			assertEquals(EM_DIAGNOSTICO, os.getOrdemDeServicoStatus());

			os.adicionarDiagnostico(List.of(servico), List.of(item));

			os.finalizarDiagnostico();

			assertEquals(AGUARDANDO_APROVACAO, os.getOrdemDeServicoStatus());

			os.registrarAprovacao(true);

			assertEquals(ORCAMENTO_APROVADO, os.getOrdemDeServicoStatus());

			os.iniciarExecucao();

			assertEquals(EM_EXECUCAO, os.getOrdemDeServicoStatus());

			os.finalizarExecucao();

			assertEquals(FINALIZADA, os.getOrdemDeServicoStatus());

			os.entregar();

			assertAll(() -> assertEquals(ENTREGUE, os.getOrdemDeServicoStatus()),
					() -> assertEquals(new BigDecimal("200.00"), os.getValorOs()),
					() -> assertNotNull(os.getDtHoraInicioExecucao()), () -> assertNotNull(os.getDtHoraFimExecucao()),
					() -> assertNotNull(os.getDtHoraFechamento()));
		}
	}

	private OrdemServico criarOrdemServico(OrdemDeServicoStatus status) {
		return criarOrdemServico(status, List.of(), List.of(), null, null);
	}

	private OrdemServico criarOrdemServico(OrdemDeServicoStatus status, LocalDateTime inicioExecucao,
			LocalDateTime fimExecucao) {
		return criarOrdemServico(status, List.of(), List.of(), inicioExecucao, fimExecucao);
	}

	private OrdemServico criarOrdemServico(OrdemDeServicoStatus status, List<OrdemServicoServico> servicos,
			List<OrdemServicoItemEstoque> itens, LocalDateTime inicioExecucao, LocalDateTime fimExecucao) {
		return new OrdemServico(UUID.randomUUID(), "12345678910", "ABC1D23", DATA_ABERTURA, null, "Barulho ao frear",
				status, null, servicos, itens, inicioExecucao, fimExecucao);
	}

	private OrdemServicoServico criarServico(BigDecimal valorTotal) {
		return new OrdemServicoServico(UUID.randomUUID(), "Serviço de teste", valorTotal);
	}

	private OrdemServicoItemEstoque criarItemEstoque(BigDecimal valorTotal) {
		OrdemServicoItemEstoque item = mock(OrdemServicoItemEstoque.class);

		when(item.getValorTotal()).thenReturn(valorTotal);

		return item;
	}
}