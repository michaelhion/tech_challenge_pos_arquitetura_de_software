package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.mappers.EstoqueCommandMapper;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;

import java.util.List;

public class ListarItensEstoqueUseCase {

	private final EstoqueGateway estoqueGateway;

	public ListarItensEstoqueUseCase(EstoqueGateway estoqueGateway) {
		this.estoqueGateway = estoqueGateway;
	}

	public List<EstoqueCommand> findAll() {
		return estoqueGateway.findAll()
				.stream()
				.map(EstoqueCommandMapper::domaintoCommand)
				.toList();
	}
}
