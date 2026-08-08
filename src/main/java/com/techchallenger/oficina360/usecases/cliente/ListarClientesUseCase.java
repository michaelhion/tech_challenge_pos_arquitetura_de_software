package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.mappers.ClienteCommandMapper;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;

import java.util.List;

public class ListarClientesUseCase {

	private final ClienteGateway clienteGateWay;

	public ListarClientesUseCase(ClienteGateway clienteGateWay) {
		this.clienteGateWay = clienteGateWay;
	}

	public List<ClienteCommand> findAll() {
		return clienteGateWay.findAll()
				.stream()
				.map(ClienteCommandMapper::domainToCommand)
				.toList();
	}
}
