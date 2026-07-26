package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;

import java.util.UUID;

public class ConsultarStatusOsUseCase {
	private final OrdemServicoFinder ordemServicoFinder;

	public ConsultarStatusOsUseCase(OrdemServicoFinder ordemServicoFinder) {
		this.ordemServicoFinder = ordemServicoFinder;
	}

	public OrdemDeServicoStatus executar(UUID id){
		OrdemServico ordemServico = ordemServicoFinder.obterOuFalhar(id);
		return ordemServico.getOrdemDeServicoStatus();
	}
}
