package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.services.MovimentacaoEstoqueService;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

public class AprovarOrcamentoUseCase {

	private final OrdemServicoGateway ordemServicoGateway;
	private final OrdemServicoFinder ordemServicoFinder;
	private final MovimentacaoEstoqueService movimentacaoEstoqueService;

	public AprovarOrcamentoUseCase(OrdemServicoGateway ordemServicoGateway, OrdemServicoFinder ordemServicoFinder,
			MovimentacaoEstoqueService movimentacaoEstoqueService) {
		this.ordemServicoGateway = ordemServicoGateway;
		this.ordemServicoFinder = ordemServicoFinder;
		this.movimentacaoEstoqueService = movimentacaoEstoqueService;
	}

	public OrdemServicoDTO aprovar(UUID id, AprovacaoOrdemServicoDTO aprovacaoDTO) {
		OrdemServico ordemServico = ordemServicoFinder.obterOuFalhar(id);

		Boolean aprovado = aprovacaoDTO.aprovado();

		ordemServico.registrarAprovacao(aprovado);

		registrarObservacao(ordemServico, aprovacaoDTO.observacao());

		if (Boolean.FALSE.equals(aprovado)) {
			movimentacaoEstoqueService.liberarReservas(ordemServico.getItensEstoque());
		}

		OrdemServico atualizada = ordemServicoGateway.save(ordemServico);

		return toDTO(atualizada);
	}

	private void registrarObservacao(OrdemServico ordemServico, String observacao) {
		if (observacao != null && !observacao.isBlank()) {
			ordemServico.setObservacaoCliente(observacao.trim());
		}
	}
}