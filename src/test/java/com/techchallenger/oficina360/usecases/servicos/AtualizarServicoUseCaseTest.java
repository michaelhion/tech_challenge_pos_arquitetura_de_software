package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.SERVICO_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarServicoUseCaseTest {

	private static final String CODIGO_ATUAL = "TROCA-DE-OLEO";

	private static final String CODIGO_ATUALIZADO = "TROCA-DE-OLEO-PREMIUM";

	private static final String DESCRICAO_ATUALIZADA = "Troca de óleo com filtro premium";

	private static final BigDecimal VALOR_ATUALIZADO = new BigDecimal("220.00");

	private static final Integer TEMPO_MEDIO_ATUALIZADO = 60;

	@Mock
	private ServicoGateway servicoGateway;

	@Mock
	private Servico servico;

	@InjectMocks
	private AtualizarServicoUseCase useCase;

	private ServicoCommand command;

	@BeforeEach
	void setUp() {
		command = new ServicoCommand(CODIGO_ATUALIZADO, DESCRICAO_ATUALIZADA, VALOR_ATUALIZADO, TEMPO_MEDIO_ATUALIZADO);
	}

	@Test
	void deveAtualizarServicoComSucesso() {
		when(servicoGateway.findByCodigo(CODIGO_ATUAL)).thenReturn(Optional.of(servico));

		when(servicoGateway.save(servico)).thenReturn(servico);

		configurarServicoAtualizado();

		ServicoCommand resultado = useCase.edit(CODIGO_ATUAL, command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CODIGO_ATUALIZADO, resultado.codigo()),
				() -> assertEquals(DESCRICAO_ATUALIZADA, resultado.descricao()),
				() -> assertEquals(0, VALOR_ATUALIZADO.compareTo(resultado.valor())),
				() -> assertEquals(TEMPO_MEDIO_ATUALIZADO, resultado.tempoDeExecucaoMedio()));

		verify(servicoGateway).findByCodigo(CODIGO_ATUAL);

		verify(servico).editar(DESCRICAO_ATUALIZADA, VALOR_ATUALIZADO, CODIGO_ATUALIZADO);

		verify(servicoGateway).save(servico);
	}

	@Test
	void deveBuscarEditarESalvarNestaOrdem() {
		when(servicoGateway.findByCodigo(CODIGO_ATUAL)).thenReturn(Optional.of(servico));

		when(servicoGateway.save(servico)).thenReturn(servico);

		configurarServicoAtualizado();

		useCase.edit(CODIGO_ATUAL, command);

		InOrder ordemDasChamadas = inOrder(servicoGateway, servico);

		ordemDasChamadas.verify(servicoGateway).findByCodigo(CODIGO_ATUAL);

		ordemDasChamadas.verify(servico).editar(DESCRICAO_ATUALIZADA, VALOR_ATUALIZADO, CODIGO_ATUALIZADO);

		ordemDasChamadas.verify(servicoGateway).save(servico);
	}

	@Test
	void deveLancarExcecaoQuandoServicoNaoForEncontrado() {
		when(servicoGateway.findByCodigo(CODIGO_ATUAL)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.edit(CODIGO_ATUAL, command));

		assertEquals(SERVICO_NAO_ENCONTRADO, exception.getMessage());

		verify(servicoGateway).findByCodigo(CODIGO_ATUAL);

		verify(servico, never()).editar(DESCRICAO_ATUALIZADA, VALOR_ATUALIZADO, CODIGO_ATUALIZADO);

		verify(servicoGateway, never()).save(servico);

		verifyNoMoreInteractions(servicoGateway, servico);
	}

	private void configurarServicoAtualizado() {
		when(servico.getCodigo()).thenReturn(CODIGO_ATUALIZADO);

		when(servico.getDescricao()).thenReturn(DESCRICAO_ATUALIZADA);

		when(servico.getValor()).thenReturn(VALOR_ATUALIZADO);

		when(servico.getTempoMedioExecucaoMinutos()).thenReturn(TEMPO_MEDIO_ATUALIZADO);
	}
}