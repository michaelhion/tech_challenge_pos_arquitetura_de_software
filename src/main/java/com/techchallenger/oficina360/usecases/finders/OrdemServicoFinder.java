package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA;

public class OrdemServicoFinder {

	private final OrdemServicoGateway gateway;

	public OrdemServicoFinder(OrdemServicoGateway gateway) {
		this.gateway = gateway;
	}

	public OrdemServico obterOuFalhar(UUID id){
		return gateway.findById(id).orElseThrow(()-> new RecursoNaoEncontradoException(OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA));
	}
}
