package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.mappers.ClienteMapper;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.mappers.ClienteMapper.domainToDTO;

public class AtualizarClienteUseCase {

	private final ClienteGateway clienteGateway;

	public AtualizarClienteUseCase(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}
	public ClienteDTO edit(String documento, ClienteDTO dto) {
		Cliente cliente = clienteGateway.findByDocumento(documento)
				.orElseThrow(() -> new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO));

		ClienteMapper.updateDomainFromDto(dto, cliente);

		Cliente clienteAtualizado = clienteGateway.save(cliente);

		return domainToDTO(clienteAtualizado);
	}
}
