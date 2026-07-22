package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.services.MovimentacaoEstoqueService;
import com.techchallenger.oficina360.usecases.services.TempoExecucaoService;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

public class FinalizarExecucaoUseCase {

	private final OrdemServicoGateway ordemServicoGateway;
	private final TempoExecucaoService tempoExecucaoService;
	private final OrdemServicoFinder ordemServicoFinder;
	private final MovimentacaoEstoqueService movimentacaoEstoqueService;

	public FinalizarExecucaoUseCase(OrdemServicoGateway ordemServicoGateway, TempoExecucaoService tempoExecucaoService,
			OrdemServicoFinder ordemServicoFinder, MovimentacaoEstoqueService movimentacaoEstoqueService) {
		this.ordemServicoGateway = ordemServicoGateway;
		this.tempoExecucaoService = tempoExecucaoService;
		this.ordemServicoFinder = ordemServicoFinder;
		this.movimentacaoEstoqueService = movimentacaoEstoqueService;
	}

	public OrdemServicoDTO finalizarExecucao(UUID id) {
		OrdemServico ordemServico = ordemServicoFinder.obterOuFalhar(id);

		ordemServico.finalizarExecucao();

		movimentacaoEstoqueService.consumirReservas(ordemServico.getItensEstoque());

		tempoExecucaoService.registrar(ordemServico);

		OrdemServico atualizada = ordemServicoGateway.save(ordemServico);

		return toDTO(atualizada);
	}
}