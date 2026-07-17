package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.mappers.ServicoMapper;

import java.util.Optional;

public class BuscarServicoPorCodigoUseCase {

	private final ServicoGateway servicoGateway;

	public BuscarServicoPorCodigoUseCase(ServicoGateway servicoGateway) {
		this.servicoGateway = servicoGateway;
	}

	public Optional<ServicoDTO> findByCodigo(String codigo) {
		return servicoGateway.findByCodigo(codigo)
				.map(ServicoMapper::domainToDTO);
	}
}
