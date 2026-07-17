package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.mappers.EstoqueMapper;

import java.util.List;

public class ListarItensEstoqueUseCase {

	private final EstoqueGateway estoqueGateway;

	public ListarItensEstoqueUseCase(EstoqueGateway estoqueGateway) {
		this.estoqueGateway = estoqueGateway;
	}

	public List<EstoqueDTO> findAll() {
		return estoqueGateway.findAll()
				.stream()
				.map(EstoqueMapper::domaintoDTO)
				.toList();
	}
}
