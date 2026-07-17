package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;

import java.util.UUID;

public class DeletarOrdemServicoUseCase {

	private final OrdemServicoGateway gateway;
	private final OrdemServicoFinder loader;

	public DeletarOrdemServicoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		this.gateway = gateway;
		this.loader = loader;
	}

	public void deleteById(UUID id) {
		OrdemServico ordemServico = loader.obterOuFalhar(id);

		gateway.deleteById(id);

	}
}
