package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.gateways.Relogio;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;

import java.util.UUID;

public class IniciarExecucaoUseCase {

	private final OrdemServicoGateway gateway;
	private final OrdemServicoFinder loader;
	private final Relogio relogio;

	public IniciarExecucaoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader, Relogio relogio) {
		this.gateway = gateway;
		this.loader = loader;
		this.relogio = relogio;
	}

	public void iniciarExecucao(UUID id) {
		OrdemServico ordemServico = loader.obterOuFalhar(id);

		ordemServico.iniciarExecucao(relogio.agora());

		gateway.save(ordemServico);
	}
}
