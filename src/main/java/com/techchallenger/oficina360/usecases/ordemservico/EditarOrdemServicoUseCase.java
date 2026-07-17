package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

public class EditarOrdemServicoUseCase {

	private final OrdemServicoGateway gateway;
	private final OrdemServicoFinder loader;

	public EditarOrdemServicoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		this.gateway = gateway;
		this.loader = loader;
	}

	public OrdemServicoDTO edit(UUID id, OrdemServicoDTO dto) {
		OrdemServico ordemServico = loader.obterOuFalhar(id);

		ordemServico.setDescricaoProblema(dto.descricaoProblema());

		OrdemServico ordemServicoAtualizada = gateway.save(ordemServico);

		return toDTO(ordemServicoAtualizada);
	}
}
