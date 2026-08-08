package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.mappers.ClienteCommandMapper;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;

public class BuscarClientePorDocumentoUseCase {

	private final ClienteFinder clientefinder;

	public BuscarClientePorDocumentoUseCase(ClienteFinder clientefinder) {
		this.clientefinder = clientefinder;
	}

	public ClienteCommand findByDocumento(String documento) {
		return ClienteCommandMapper.domainToCommand(clientefinder.buscarPorDocumentoOuFalhar(documento));
	}
}
