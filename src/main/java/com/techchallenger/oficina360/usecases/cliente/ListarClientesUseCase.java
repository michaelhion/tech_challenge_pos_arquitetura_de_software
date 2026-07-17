package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.mappers.ClienteMapper;

import java.util.List;

public class ListarClientesUseCase {

	private final ClienteGateway clienteGateWay;

	public ListarClientesUseCase(ClienteGateway clienteGateWay) {
		this.clienteGateWay = clienteGateWay;
	}

	public List<ClienteDTO> findAll() {
		return clienteGateWay.findAll()
				.stream()
				.map(ClienteMapper::domainToDTO)
				.toList();
	}
}
