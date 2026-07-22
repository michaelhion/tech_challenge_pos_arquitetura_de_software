package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;

public class ExcluirClienteUseCase {

	private final ClienteGateway clienteGateway;

	public ExcluirClienteUseCase(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}



	public void delete(String documento) {
		if(clienteGateway.existsByDocumento(documento)) {
			throw new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO);
		}
		clienteGateway.deleteByDocumento(documento);
	}
}
