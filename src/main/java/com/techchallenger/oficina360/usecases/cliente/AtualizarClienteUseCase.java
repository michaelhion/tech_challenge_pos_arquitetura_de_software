package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.mappers.ClienteCommandMapper.domainToCommand;

public class AtualizarClienteUseCase {

	private final ClienteGateway clienteGateway;

	public AtualizarClienteUseCase(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}

	public ClienteCommand edit(String documento, ClienteCommand command) {
		Cliente cliente = clienteGateway.findByDocumento(documento)
				.orElseThrow(() -> new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO));

		cliente.editar(
				command.documento(),
				command.nome(),
				command.email(),
				command.telefone()
		);

		return domainToCommand(clienteGateway.save(cliente));
	}
}
