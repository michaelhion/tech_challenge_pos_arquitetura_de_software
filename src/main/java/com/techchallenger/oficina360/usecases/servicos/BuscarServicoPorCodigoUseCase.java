package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import static com.techchallenger.oficina360.mappers.ServicoCommandMapper.domainToCommand;

public class BuscarServicoPorCodigoUseCase {

	private final ServicoGateway servicoGateway;

	public BuscarServicoPorCodigoUseCase(ServicoGateway servicoGateway) {
		this.servicoGateway = servicoGateway;
	}

	public ServicoCommand findByCodigo(String codigo) {
		return domainToCommand(servicoGateway.findByCodigo(codigo).orElseThrow(()-> new RecursoNaoEncontradoException("Serviço não encontrado")));
	}
}
