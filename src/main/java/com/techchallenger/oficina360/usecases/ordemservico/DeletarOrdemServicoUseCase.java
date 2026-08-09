package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import java.util.UUID;

public class DeletarOrdemServicoUseCase {

	private final OrdemServicoGateway gateway;

	public DeletarOrdemServicoUseCase(OrdemServicoGateway gateway) {
		this.gateway = gateway;
	}

	public void deleteById(UUID id) {
		gateway.findById(id).orElseThrow(()-> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));

		gateway.deleteById(id);

	}
}
