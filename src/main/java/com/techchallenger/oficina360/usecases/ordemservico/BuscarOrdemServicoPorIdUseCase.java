package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.domainToCommand;

public class BuscarOrdemServicoPorIdUseCase {

	private final OrdemServicoFinder ordemServicoFinder;

	public BuscarOrdemServicoPorIdUseCase(OrdemServicoFinder ordemServicoFinder) {
		this.ordemServicoFinder = ordemServicoFinder;
	}

	public OrdemServicoRespCommand findById(UUID id) {
		return domainToCommand(ordemServicoFinder.obterOuFalhar(id));
	}
}
