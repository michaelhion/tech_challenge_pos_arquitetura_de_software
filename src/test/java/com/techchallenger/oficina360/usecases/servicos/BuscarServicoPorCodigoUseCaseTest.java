package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarServicoPorCodigoUseCaseTest {

	private static final String CODIGO = "TROCA-DE-OLEO";

	private static final String DESCRICAO = "Troca de óleo";

	private static final BigDecimal VALOR = new BigDecimal("150.00");

	private static final Integer TEMPO_MEDIO_EXECUCAO = 60;

	private static final String SERVICO_NAO_ENCONTRADO = "Serviço não encontrado";

	@Mock
	private ServicoGateway servicoGateway;

	@Mock
	private Servico servico;

	@InjectMocks
	private BuscarServicoPorCodigoUseCase useCase;

	@Test
	void deveBuscarServicoPorCodigoComSucesso() {
		when(servicoGateway.findByCodigo(CODIGO)).thenReturn(Optional.of(servico));

		configurarServicoEncontrado();

		ServicoCommand resultado = useCase.findByCodigo(CODIGO);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CODIGO, resultado.codigo()), () -> assertEquals(DESCRICAO, resultado.descricao()),
				() -> assertEquals(0, VALOR.compareTo(resultado.valor())),
				() -> assertEquals(TEMPO_MEDIO_EXECUCAO, resultado.tempoDeExecucaoMedio()));

		verify(servicoGateway).findByCodigo(CODIGO);

		verifyNoMoreInteractions(servicoGateway);
	}

	@Test
	void deveLancarExcecaoQuandoServicoNaoForEncontrado() {
		when(servicoGateway.findByCodigo(CODIGO)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.findByCodigo(CODIGO));

		assertEquals(SERVICO_NAO_ENCONTRADO, exception.getMessage());

		verify(servicoGateway).findByCodigo(CODIGO);

		verifyNoMoreInteractions(servicoGateway);

		verify(servico, never()).getCodigo();
	}

	private void configurarServicoEncontrado() {
		when(servico.getCodigo()).thenReturn(CODIGO);

		when(servico.getDescricao()).thenReturn(DESCRICAO);

		when(servico.getValor()).thenReturn(VALOR);

		when(servico.getTempoMedioExecucaoMinutos()).thenReturn(TEMPO_MEDIO_EXECUCAO);
	}
}