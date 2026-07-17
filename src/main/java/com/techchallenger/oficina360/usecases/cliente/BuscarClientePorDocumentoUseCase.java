package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.mappers.ClienteMapper;

import java.util.Optional;

public class BuscarClientePorDocumentoUseCase {

	private final ClienteGateway clienteGateway;

	public BuscarClientePorDocumentoUseCase(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}

	public Optional<ClienteDTO> findByDocumento(String documento) {
		return clienteGateway.findByDocumento(documento).map(ClienteMapper::domainToDTO);
	}
}
