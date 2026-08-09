package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.gateways.TempoExecucaoServicoGateway;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarServicosUseCaseTest {

	private static final UUID PRIMEIRO_SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID SEGUNDO_SERVICO_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final String CODIGO_TROCA_OLEO = "TROCA-DE-OLEO";

	private static final String DESCRICAO_TROCA_OLEO = "Troca de óleo";

	private static final BigDecimal VALOR_TROCA_OLEO = new BigDecimal("150.00");

	private static final String CODIGO_ALINHAMENTO = "ALINHAMENTO";

	private static final String DESCRICAO_ALINHAMENTO = "Alinhamento do veículo";

	private static final BigDecimal VALOR_ALINHAMENTO = new BigDecimal("100.00");

	@Mock
	private ServicoGateway servicoGateway;

	@Mock
	private TempoExecucaoServicoGateway tempoExecucaoServicoGateway;

	@Mock
	private Servico primeiroServico;

	@Mock
	private Servico segundoServico;

	@Test
	void deveListarServicosComTempoMedioCalculado() {
		ListarServicosUseCase useCase = criarUseCase();

		configurarPrimeiroServico();

		when(servicoGateway.findAll()).thenReturn(List.of(primeiroServico));

		when(tempoExecucaoServicoGateway.calcularTempoMedio(PRIMEIRO_SERVICO_ID)).thenReturn(45.0);

		List<ServicoCommand> resultado = useCase.findAll();

		assertNotNull(resultado);
		assertEquals(1, resultado.size());

		assertServico(resultado.get(0), CODIGO_TROCA_OLEO, DESCRICAO_TROCA_OLEO, VALOR_TROCA_OLEO, 45);

		verify(servicoGateway, times(1)).findAll();

		verify(tempoExecucaoServicoGateway, times(1)).calcularTempoMedio(PRIMEIRO_SERVICO_ID);

		verifyNoMoreInteractions(servicoGateway, tempoExecucaoServicoGateway);
	}

	@Test
	void deveArredondarTempoMedioParaInteiroMaisProximo() {
		ListarServicosUseCase useCase = criarUseCase();

		configurarPrimeiroServico();

		when(servicoGateway.findAll()).thenReturn(List.of(primeiroServico));

		when(tempoExecucaoServicoGateway.calcularTempoMedio(PRIMEIRO_SERVICO_ID)).thenReturn(45.7);

		List<ServicoCommand> resultado = useCase.findAll();

		assertEquals(46, resultado.get(0).tempoDeExecucaoMedio());

		verify(servicoGateway).findAll();

		verify(tempoExecucaoServicoGateway).calcularTempoMedio(PRIMEIRO_SERVICO_ID);

		verifyNoMoreInteractions(servicoGateway, tempoExecucaoServicoGateway);
	}

	@Test
	void deveArredondarTempoMedioParaBaixoQuandoFracaoForMenorQueMeio() {
		ListarServicosUseCase useCase = criarUseCase();

		configurarPrimeiroServico();

		when(servicoGateway.findAll()).thenReturn(List.of(primeiroServico));

		when(tempoExecucaoServicoGateway.calcularTempoMedio(PRIMEIRO_SERVICO_ID)).thenReturn(45.4);

		List<ServicoCommand> resultado = useCase.findAll();

		assertEquals(45, resultado.get(0).tempoDeExecucaoMedio());

		verify(servicoGateway).findAll();

		verify(tempoExecucaoServicoGateway).calcularTempoMedio(PRIMEIRO_SERVICO_ID);

		verifyNoMoreInteractions(servicoGateway, tempoExecucaoServicoGateway);
	}

	@Test
	void deveRetornarTempoMedioZeroQuandoNaoExistirMedia() {
		ListarServicosUseCase useCase = criarUseCase();

		configurarPrimeiroServico();

		when(servicoGateway.findAll()).thenReturn(List.of(primeiroServico));

		when(tempoExecucaoServicoGateway.calcularTempoMedio(PRIMEIRO_SERVICO_ID)).thenReturn(null);

		List<ServicoCommand> resultado = useCase.findAll();

		assertNotNull(resultado);
		assertEquals(1, resultado.size());

		assertServico(resultado.get(0), CODIGO_TROCA_OLEO, DESCRICAO_TROCA_OLEO, VALOR_TROCA_OLEO, 0);

		verify(servicoGateway).findAll();

		verify(tempoExecucaoServicoGateway).calcularTempoMedio(PRIMEIRO_SERVICO_ID);

		verifyNoMoreInteractions(servicoGateway, tempoExecucaoServicoGateway);
	}

	@Test
	void deveCalcularTempoMedioParaCadaServico() {
		ListarServicosUseCase useCase = criarUseCase();

		configurarPrimeiroServico();
		configurarSegundoServico();

		when(servicoGateway.findAll()).thenReturn(List.of(primeiroServico, segundoServico));

		when(tempoExecucaoServicoGateway.calcularTempoMedio(PRIMEIRO_SERVICO_ID)).thenReturn(45.2);

		when(tempoExecucaoServicoGateway.calcularTempoMedio(SEGUNDO_SERVICO_ID)).thenReturn(70.8);

		List<ServicoCommand> resultado = useCase.findAll();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		assertServico(resultado.get(0), CODIGO_TROCA_OLEO, DESCRICAO_TROCA_OLEO, VALOR_TROCA_OLEO, 45);

		assertServico(resultado.get(1), CODIGO_ALINHAMENTO, DESCRICAO_ALINHAMENTO, VALOR_ALINHAMENTO, 71);

		verify(servicoGateway).findAll();

		verify(tempoExecucaoServicoGateway).calcularTempoMedio(PRIMEIRO_SERVICO_ID);

		verify(tempoExecucaoServicoGateway).calcularTempoMedio(SEGUNDO_SERVICO_ID);

		verifyNoMoreInteractions(servicoGateway, tempoExecucaoServicoGateway);
	}

	@Test
	void devePreservarOrdemDosServicosRetornadosPeloGateway() {
		ListarServicosUseCase useCase = criarUseCase();

		configurarPrimeiroServico();
		configurarSegundoServico();

		when(servicoGateway.findAll()).thenReturn(List.of(segundoServico, primeiroServico));

		when(tempoExecucaoServicoGateway.calcularTempoMedio(SEGUNDO_SERVICO_ID)).thenReturn(70.0);

		when(tempoExecucaoServicoGateway.calcularTempoMedio(PRIMEIRO_SERVICO_ID)).thenReturn(45.0);

		List<ServicoCommand> resultado = useCase.findAll();

		assertAll(() -> assertEquals(2, resultado.size()),
				() -> assertEquals(CODIGO_ALINHAMENTO, resultado.get(0).codigo()),
				() -> assertEquals(CODIGO_TROCA_OLEO, resultado.get(1).codigo()));

		verify(servicoGateway).findAll();

		verify(tempoExecucaoServicoGateway).calcularTempoMedio(SEGUNDO_SERVICO_ID);

		verify(tempoExecucaoServicoGateway).calcularTempoMedio(PRIMEIRO_SERVICO_ID);

		verifyNoMoreInteractions(servicoGateway, tempoExecucaoServicoGateway);
	}

	@Test
	void deveBuscarServicosAntesDeCalcularTempoMedio() {
		ListarServicosUseCase useCase = criarUseCase();

		configurarPrimeiroServico();

		when(servicoGateway.findAll()).thenReturn(List.of(primeiroServico));

		when(tempoExecucaoServicoGateway.calcularTempoMedio(PRIMEIRO_SERVICO_ID)).thenReturn(45.0);

		useCase.findAll();

		InOrder ordemDasChamadas = inOrder(servicoGateway, tempoExecucaoServicoGateway);

		ordemDasChamadas.verify(servicoGateway).findAll();

		ordemDasChamadas.verify(tempoExecucaoServicoGateway).calcularTempoMedio(PRIMEIRO_SERVICO_ID);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremServicos() {
		ListarServicosUseCase useCase = criarUseCase();

		when(servicoGateway.findAll()).thenReturn(List.of());

		List<ServicoCommand> resultado = useCase.findAll();

		assertAll(() -> assertNotNull(resultado), () -> assertTrue(resultado.isEmpty()));

		verify(servicoGateway, times(1)).findAll();

		verify(tempoExecucaoServicoGateway, never()).calcularTempoMedio(org.mockito.ArgumentMatchers.any(UUID.class));

		verifyNoMoreInteractions(servicoGateway, tempoExecucaoServicoGateway);
	}

	private ListarServicosUseCase criarUseCase() {
		return new ListarServicosUseCase(servicoGateway, tempoExecucaoServicoGateway);
	}

	private void configurarPrimeiroServico() {
		when(primeiroServico.getId()).thenReturn(PRIMEIRO_SERVICO_ID);

		when(primeiroServico.getCodigo()).thenReturn(CODIGO_TROCA_OLEO);

		when(primeiroServico.getDescricao()).thenReturn(DESCRICAO_TROCA_OLEO);

		when(primeiroServico.getValor()).thenReturn(VALOR_TROCA_OLEO);
	}

	private void configurarSegundoServico() {
		when(segundoServico.getId()).thenReturn(SEGUNDO_SERVICO_ID);

		when(segundoServico.getCodigo()).thenReturn(CODIGO_ALINHAMENTO);

		when(segundoServico.getDescricao()).thenReturn(DESCRICAO_ALINHAMENTO);

		when(segundoServico.getValor()).thenReturn(VALOR_ALINHAMENTO);
	}

	private void assertServico(ServicoCommand resultado, String codigoEsperado, String descricaoEsperada,
			BigDecimal valorEsperado, Integer tempoMedioEsperado) {
		assertAll(() -> assertEquals(codigoEsperado, resultado.codigo()),
				() -> assertEquals(descricaoEsperada, resultado.descricao()),
				() -> assertEquals(0, valorEsperado.compareTo(resultado.valor())),
				() -> assertEquals(tempoMedioEsperado, resultado.tempoDeExecucaoMedio()));
	}
}