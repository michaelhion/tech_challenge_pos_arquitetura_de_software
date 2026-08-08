package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.mappers.EstoqueCommandMapper;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

public class BuscarItemEstoqueUseCase {

	private final EstoqueGateway estoqueGateway;

	public BuscarItemEstoqueUseCase(EstoqueGateway estoqueGateway) {
		this.estoqueGateway = estoqueGateway;
	}

	public EstoqueCommand findByCodigo(String codigo) {
		 Estoque estoque = estoqueGateway.findByCodigo(codigo).orElseThrow(()-> new RecursoNaoEncontradoException("Item de estoque não encontrado"));
		 return EstoqueCommandMapper.domaintoCommand(estoque);
	}
}
