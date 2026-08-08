package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ReservaEstoqueCommand;

import static com.techchallenger.oficina360.mappers.EstoqueCommandMapper.domaintoCommand;

public class ReservarEstoqueUseCase {

	private final EstoqueFinder estoqueFinder;
	private final EstoqueGateway estoqueGateway;

	public ReservarEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		this.estoqueFinder = estoqueFinder;
		this.estoqueGateway = estoqueGateway;
	}

	public EstoqueCommand reservar(String codigo, ReservaEstoqueCommand command) {
		Estoque estoque = estoqueFinder.obterOuFalhar(codigo);

		estoque.reservar(command.quantidade());

		Estoque estoqueAtualizado = estoqueGateway.save(estoque);

		return domaintoCommand(estoqueAtualizado);
	}
}
