package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

public class AprovarOrcamentoUseCase {

	private final OrdemServicoGateway gateway;
	private final OrdemServicoFinder loader;

	public AprovarOrcamentoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		this.gateway = gateway;
		this.loader = loader;
	}

	public OrdemServicoDTO aprovar(UUID id, AprovacaoOrdemServicoDTO aprovacaoDTO) {
		OrdemServico ordemServico =loader.obterOuFalhar(id);

		ordemServico.registrarAprovacao(aprovacaoDTO.aprovado());
		if (aprovacaoDTO.observacao() != null && !aprovacaoDTO.observacao().isBlank()) {
			ordemServico.setObservacaoCliente(aprovacaoDTO.observacao());
		}
		OrdemServico ordemServicoAtualizada = gateway.save(ordemServico);

		return toDTO(ordemServicoAtualizada);
	}
}
