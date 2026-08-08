package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.AprovacaoOrdemServicoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.servicos.MovimentacaoEstoqueService;

import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.domainToCommand;

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

	public OrdemServicoRespCommand aprovar(UUID id, AprovacaoOrdemServicoCommand command) {
		OrdemServico ordemServico = ordemServicoFinder.obterOuFalhar(id);

		Boolean aprovado = command.aprovado();

		ordemServico.registrarAprovacao(aprovado);

		registrarObservacao(ordemServico, command.observacao());

		if (Boolean.FALSE.equals(aprovado)) {
			movimentacaoEstoqueService.liberarReservas(ordemServico.getItensEstoque());
		}

		OrdemServico atualizada = ordemServicoGateway.save(ordemServico);

		return domainToCommand(atualizada);
	}

	private void registrarObservacao(OrdemServico ordemServico, String observacao) {
		if (observacao != null && !observacao.isBlank()) {
			ordemServico.setObservacaoCliente(observacao.trim());
		}
	}
}