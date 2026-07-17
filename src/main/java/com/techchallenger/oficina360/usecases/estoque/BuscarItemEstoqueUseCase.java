package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.mappers.EstoqueMapper;

import java.util.Optional;

public class BuscarItemEstoqueUseCase {

	private final EstoqueGateway estoqueGateway;

	public BuscarItemEstoqueUseCase(EstoqueGateway estoqueGateway) {
		this.estoqueGateway = estoqueGateway;
	}

	public Optional<EstoqueDTO> findByCodigo(String codigo) {
		return estoqueGateway.findByCodigo(codigo).map(EstoqueMapper::domaintoDTO);
	}
}
