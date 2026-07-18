package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.mappers.VeiculoMapper;

import java.util.List;

public class ListarVeiculosUseCase {

	private final VeiculoGateway veiculoGateway;

	public ListarVeiculosUseCase(VeiculoGateway veiculoGateway) {
		this.veiculoGateway = veiculoGateway;
	}

	public List<VeiculoDTO> findAll() {
		return veiculoGateway.findAll()
				.stream()
				.map(VeiculoMapper::domainToDTO)
				.toList();
	}
}
