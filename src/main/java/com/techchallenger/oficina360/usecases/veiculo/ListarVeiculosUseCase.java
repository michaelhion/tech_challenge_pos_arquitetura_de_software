package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.mappers.VeiculoCommandMapper;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;

import java.util.List;

public class ListarVeiculosUseCase {

	private final VeiculoGateway veiculoGateway;

	public ListarVeiculosUseCase(VeiculoGateway veiculoGateway) {
		this.veiculoGateway = veiculoGateway;
	}

	public List<VeiculoCommand> findAll() {
		return veiculoGateway.findAll()
				.stream()
				.map(VeiculoCommandMapper::domainToCommand)
				.toList();
	}
}
