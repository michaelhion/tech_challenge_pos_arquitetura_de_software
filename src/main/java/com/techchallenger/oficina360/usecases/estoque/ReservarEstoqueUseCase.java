package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.mappers.EstoqueMapper;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;

public class ReservarEstoqueUseCase {

	private final EstoqueFinder estoqueFinder;
	private final EstoqueGateway estoqueGateway;

	public ReservarEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		this.estoqueFinder = estoqueFinder;
		this.estoqueGateway = estoqueGateway;
	}

	public EstoqueDTO reservar(String codigo, ReservaEstoqueDTO reservaDTO) {
		Estoque estoque = estoqueFinder.obterOuFalhar(codigo);

		estoque.reservar(reservaDTO.quantidade());

		Estoque estoqueAtualizado = estoqueGateway.save(estoque);

		return EstoqueMapper.domaintoDTO(estoqueAtualizado);
	}
}
