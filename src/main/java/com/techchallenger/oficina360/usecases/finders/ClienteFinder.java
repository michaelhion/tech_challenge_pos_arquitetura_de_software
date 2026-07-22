package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;

public class ClienteFinder {

	private final ClienteGateway gateway;

	public ClienteFinder(ClienteGateway gateway) {
		this.gateway = gateway;
	}

	public Cliente buscarPorDocumentoOuFalhar(String documento) {
		return gateway.findByDocumento(normalizarDocumento(documento))
				.orElseThrow(() ->
						new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO));
	}

}
