package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

public class IniciarExecucaoUseCase {

	private final OrdemServicoGateway gateway;
	private final OrdemServicoFinder loader;

	public IniciarExecucaoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		this.gateway = gateway;
		this.loader = loader;
	}

	public OrdemServicoDTO iniciarExecucao(UUID id) {
		OrdemServico ordemServico = loader.obterOuFalhar(id);

		ordemServico.iniciarExecucao();

		OrdemServico ordemServicoEntityAtualizada = gateway.save(ordemServico);

		return toDTO(ordemServicoEntityAtualizada);
	}
}
