package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;

public class ExcluirClienteUseCase {

	private final ClienteGateway clienteGateway;

	public ExcluirClienteUseCase(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}

	public void delete(String documento) {
		String documentoNormalizado = normalizarDocumento(documento);

		if (!clienteGateway.existsByDocumento(documentoNormalizado)) {
			throw new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO);
		}

		clienteGateway.deleteByDocumento(documentoNormalizado);
	}
}