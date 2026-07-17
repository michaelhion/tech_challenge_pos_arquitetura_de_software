package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.mappers.OrdemServicoMapper;

import java.util.List;

public class ListarOrdensServicoUseCase {

	private final OrdemServicoGateway gateway;

	public ListarOrdensServicoUseCase(OrdemServicoGateway ordemServicoGateway){
		gateway = ordemServicoGateway;
	}

	//todo deve retornar classe de dominio, não deve reconhecer dto da camada externa
	public List<OrdemServicoDTO> findAll() {
		return gateway.findAll()
				.stream()
				.map(OrdemServicoMapper::toDTO)
				.toList();
	}
}
