package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.factories.DiagnosticoFactory;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.loaders.DiagnosticoDados;
import com.techchallenger.oficina360.usecases.loaders.DiagnosticoLoader;
import com.techchallenger.oficina360.usecases.ordemservico.command.DiagnosticoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoDiagnosticoRespCommand;
import com.techchallenger.oficina360.usecases.services.NotificarStatusOrdemServicoService;
import com.techchallenger.oficina360.usecases.services.ReservaEstoqueService;
import com.techchallenger.oficina360.usecases.validators.DiagnosticoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.domainToDiagnosticoCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosticarOrdemServicoUseCaseTest {

	@Mock
	private OrdemServicoGateway ordemServicoGateway;

	@Mock
	private DiagnosticoFactory diagnosticoFactory;

	@Mock
	private DiagnosticoValidator diagnosticoValidator;

	@Mock
	private OrdemServicoFinder ordemServicoFinder;

	@Mock
	private DiagnosticoLoader diagnosticoLoader;

	@Mock
	private ReservaEstoqueService reservaEstoqueService;

	@Mock
	private NotificarStatusOrdemServicoService notificarStatusOrdemServicoService;

	@InjectMocks
	private DiagnosticarOrdemServicoUseCase useCase;

	@Test
	void deveDiagnosticarOrdemServicoComSucesso() {

		UUID id = UUID.randomUUID();

		OrdemServico os = OrdemServico.criar("12345678901", "ABC1234", "Problema", LocalDateTime.now());

		DiagnosticoCommand command = mock(DiagnosticoCommand.class);

		DiagnosticoDados dados = mock(DiagnosticoDados.class);

		Servico servico = mock(Servico.class);

		when(servico.getId()).thenReturn(UUID.randomUUID());
		when(servico.getDescricao()).thenReturn("Troca de óleo");
		when(servico.getValor()).thenReturn(BigDecimal.valueOf(100));

		OrdemServicoServico servicoOs = new OrdemServicoServico(UUID.randomUUID(), servico.getId(),
				servico.getDescricao(), servico.getValor());

		OrdemServicoDiagnosticoRespCommand response = mock(OrdemServicoDiagnosticoRespCommand.class);

		when(ordemServicoFinder.obterOuFalhar(id)).thenReturn(os);

		when(diagnosticoLoader.carregar(command)).thenReturn(dados);

		when(dados.codigosServicos()).thenReturn(List.of("SERV01"));

		when(dados.servicosPorCodigoBanco()).thenReturn(Map.of("SERV01", servico));

		when(dados.estoquesPorCodigoSolicitado()).thenReturn(Map.of());


		when(diagnosticoFactory.criarServicoDaOs(any())).thenReturn(servicoOs);

		when(ordemServicoGateway.save(any(OrdemServico.class))).thenAnswer(invocation -> invocation.getArgument(0));

		try (MockedStatic<com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper> mapper = mockStatic(
				com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.class)) {

			mapper.when(() -> domainToDiagnosticoCommand(any(OrdemServico.class))).thenReturn(response);

			OrdemServicoDiagnosticoRespCommand resultado = useCase.diagnosticar(id, command);

			assertEquals(response, resultado);

			verify(ordemServicoFinder).obterOuFalhar(id);
			verify(diagnosticoLoader).carregar(command);
			verify(diagnosticoValidator).validar(dados);
			verify(reservaEstoqueService).reservar(dados);
			verify(notificarStatusOrdemServicoService).notificar(os);
			verify(ordemServicoGateway).save(os);

			assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, os.getOrdemDeServicoStatus());

			assertEquals(BigDecimal.valueOf(100), os.getValorServicos());
		}
	}

	@Test
	void devePararFluxoQuandoValidacaoFalhar() {

		UUID id = UUID.randomUUID();

		OrdemServico os = OrdemServico.criar("12345678901", "ABC1234", "Problema", LocalDateTime.now());

		DiagnosticoCommand command = mock(DiagnosticoCommand.class);
		DiagnosticoDados dados = mock(DiagnosticoDados.class);

		when(ordemServicoFinder.obterOuFalhar(id)).thenReturn(os);

		when(diagnosticoLoader.carregar(command)).thenReturn(dados);

		doThrow(new IllegalArgumentException("Erro validação")).when(diagnosticoValidator).validar(dados);

		assertThrows(IllegalArgumentException.class, () -> useCase.diagnosticar(id, command));

		verify(ordemServicoFinder).obterOuFalhar(id);
		verify(diagnosticoLoader).carregar(command);
		verify(diagnosticoValidator).validar(dados);

		verifyNoInteractions(reservaEstoqueService);
		verifyNoInteractions(notificarStatusOrdemServicoService);
		verify(ordemServicoGateway, never()).save(any());
	}
}