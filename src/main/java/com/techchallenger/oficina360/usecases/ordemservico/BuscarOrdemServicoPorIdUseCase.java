package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.mappers.OrdemServicoMapper;

import java.util.Optional;
import java.util.UUID;

public class BuscarOrdemServicoPorIdUseCase {

	private final OrdemServicoGateway gateway;

	public BuscarOrdemServicoPorIdUseCase(OrdemServicoGateway ordemServicoGateway){
		gateway = ordemServicoGateway;
	}

	public Optional<OrdemServicoDTO> findById(UUID id) {
		return gateway.findById(id)
				.map(OrdemServicoMapper::toDTO);
	}
}
