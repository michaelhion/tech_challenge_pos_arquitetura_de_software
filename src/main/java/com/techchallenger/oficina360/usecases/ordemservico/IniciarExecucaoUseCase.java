package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;

import java.util.UUID;

public class IniciarExecucaoUseCase {

	private final OrdemServicoGateway gateway;
	private final OrdemServicoFinder loader;

	public IniciarExecucaoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		this.gateway = gateway;
		this.loader = loader;
	}

	public void iniciarExecucao(UUID id) {
		OrdemServico ordemServico = loader.obterOuFalhar(id);

		ordemServico.iniciarExecucao();

		gateway.save(ordemServico);
	}
}
