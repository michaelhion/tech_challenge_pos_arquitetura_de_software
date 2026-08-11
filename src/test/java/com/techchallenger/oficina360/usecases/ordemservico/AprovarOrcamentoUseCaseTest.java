package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.AprovacaoOrdemServicoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.servicos.MovimentacaoEstoqueService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.domainToCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AprovarOrcamentoUseCaseTest {

	@Mock
	private OrdemServicoGateway ordemServicoGateway;

	@Mock
	private OrdemServicoFinder ordemServicoFinder;

	@Mock
	private MovimentacaoEstoqueService movimentacaoEstoqueService;

	@InjectMocks
	private AprovarOrcamentoUseCase useCase;

	@Test
	void deveAprovarOrcamento() {

		UUID id = UUID.randomUUID();

		OrdemServico ordemServico = criarOsAguardandoAprovacao();

		AprovacaoOrdemServicoCommand command = new AprovacaoOrdemServicoCommand(true, null);

		OrdemServicoRespCommand response = mock(OrdemServicoRespCommand.class);

		when(ordemServicoFinder.obterOuFalhar(id)).thenReturn(ordemServico);

		when(ordemServicoGateway.save(ordemServico)).thenReturn(ordemServico);

		try (MockedStatic<com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper> mapper = mockStatic(
				com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.class)) {

			mapper.when(() -> domainToCommand(ordemServico)).thenReturn(response);

			OrdemServicoRespCommand resultado = useCase.aprovar(id, command);

			assertEquals(response, resultado);

			assertEquals(OrdemDeServicoStatus.ORCAMENTO_APROVADO, ordemServico.getOrdemDeServicoStatus());

			verify(movimentacaoEstoqueService, never()).liberarReservas(anyList());

			verify(ordemServicoGateway).save(ordemServico);
		}
	}

	@Test
	void deveReprovarOrcamentoELiberarReservas() {

		UUID id = UUID.randomUUID();

		OrdemServico ordemServico = criarOsAguardandoAprovacao();

		AprovacaoOrdemServicoCommand command = new AprovacaoOrdemServicoCommand(false, null);

		OrdemServicoRespCommand response = mock(OrdemServicoRespCommand.class);

		when(ordemServicoFinder.obterOuFalhar(id)).thenReturn(ordemServico);

		when(ordemServicoGateway.save(ordemServico)).thenReturn(ordemServico);

		try (MockedStatic<com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper> mapper = mockStatic(
				com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.class)) {

			mapper.when(() -> domainToCommand(ordemServico)).thenReturn(response);

			useCase.aprovar(id, command);

			assertEquals(OrdemDeServicoStatus.ORCAMENTO_REPROVADO, ordemServico.getOrdemDeServicoStatus());

			verify(movimentacaoEstoqueService).liberarReservas(ordemServico.getItensEstoque());

			verify(ordemServicoGateway).save(ordemServico);
		}
	}

	@Test
	void deveRegistrarObservacaoDoCliente() {

		UUID id = UUID.randomUUID();

		OrdemServico ordemServico = criarOsAguardandoAprovacao();

		AprovacaoOrdemServicoCommand command = new AprovacaoOrdemServicoCommand(true, "   orçamento aprovado   ");

		OrdemServicoRespCommand response = mock(OrdemServicoRespCommand.class);

		when(ordemServicoFinder.obterOuFalhar(id)).thenReturn(ordemServico);

		when(ordemServicoGateway.save(ordemServico)).thenReturn(ordemServico);

		try (MockedStatic<com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper> mapper = mockStatic(
				com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.class)) {

			mapper.when(() -> domainToCommand(ordemServico)).thenReturn(response);

			useCase.aprovar(id, command);

			assertEquals("orçamento aprovado", ordemServico.getObservacaoCliente());
		}
	}

	private OrdemServico criarOsAguardandoAprovacao() {

		return new OrdemServico(UUID.randomUUID(), "12345678901", "ABC1234", LocalDateTime.now(), null,
				"Problema veículo", OrdemDeServicoStatus.AGUARDANDO_APROVACAO, null, List.of(), List.of(), null, null);
	}
}