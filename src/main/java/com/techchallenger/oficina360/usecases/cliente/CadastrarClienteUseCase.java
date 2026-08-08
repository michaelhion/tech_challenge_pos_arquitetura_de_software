package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import com.techchallenger.oficina360.usecases.shared.exception.ClienteJaCadastradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_JA_CADASTRADO;
import static com.techchallenger.oficina360.mappers.ClienteCommandMapper.domainToCommand;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;

public class CadastrarClienteUseCase {

	private final ClienteGateway clienteGateway;

	public CadastrarClienteUseCase(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}

	public ClienteCommand save(ClienteCommand command) {
		if (clienteGateway.existsByDocumento(normalizarDocumento(command.documento()))) {
			throw new ClienteJaCadastradoException(CLIENTE_JA_CADASTRADO);
		}
		return domainToCommand(clienteGateway.save(domainToCommand(command)));
	}
}
