package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.services.TempoExecucaoService;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

public class FinalizarExecucaoUseCase {

	private final OrdemServicoGateway ordemServicoGateway;
	private final TempoExecucaoService tempoExecucaoService;
	private final OrdemServicoFinder ordemServicoFinder;

	public FinalizarExecucaoUseCase(OrdemServicoGateway gateway,
			TempoExecucaoService tempoExecucaoService, OrdemServicoFinder loader) {
		this.ordemServicoGateway = gateway;
		this.tempoExecucaoService = tempoExecucaoService;
		this.ordemServicoFinder = loader;
	}

	public OrdemServicoDTO finalizarExecucao(UUID id) {
		OrdemServico ordemServico = ordemServicoFinder.obterOuFalhar(id);

		ordemServico.finalizarExecucao();

		OrdemServico ordemServicoEntityAtualizada = ordemServicoGateway.save(ordemServico);
		tempoExecucaoService.registrar(ordemServico);
		return toDTO(ordemServicoEntityAtualizada);
	}


}
