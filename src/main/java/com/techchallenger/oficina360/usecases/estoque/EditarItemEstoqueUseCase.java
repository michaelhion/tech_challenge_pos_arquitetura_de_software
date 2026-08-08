package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;

import static com.techchallenger.oficina360.mappers.EstoqueCommandMapper.domaintoCommand;

public class EditarItemEstoqueUseCase {

	private final EstoqueFinder estoqueFinder;
	private final EstoqueGateway estoqueGateway;

	public EditarItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		this.estoqueFinder = estoqueFinder;
		this.estoqueGateway = estoqueGateway;
	}

	public EstoqueCommand edit(String codigo, EstoqueCommand command) {
		Estoque estoque = estoqueFinder.obterOuFalhar(codigo);

		estoque.editar(command.codigo(),command.nome(),command.valor(),command.quantidade(),command.reservados());
		Estoque estoqueAtualizado = estoqueGateway.save(estoque);

		return domaintoCommand(estoqueAtualizado);
	}
}
