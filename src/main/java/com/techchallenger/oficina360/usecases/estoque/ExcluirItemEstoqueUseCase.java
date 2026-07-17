package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;

public class ExcluirItemEstoqueUseCase {

	private final EstoqueFinder estoqueFinder;
	private final EstoqueGateway estoqueGateway;

	public ExcluirItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		this.estoqueFinder = estoqueFinder;
		this.estoqueGateway = estoqueGateway;
	}

	public void delete(String codigo) {
		estoqueFinder.obterOuFalhar(codigo);
		estoqueGateway.deleteByCodigo(codigo);
	}
}
