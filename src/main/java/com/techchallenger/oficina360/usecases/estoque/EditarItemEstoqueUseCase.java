package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.mappers.EstoqueMapper;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;

import static com.techchallenger.oficina360.mappers.EstoqueMapper.domaintoDTO;

public class EditarItemEstoqueUseCase {

	private final EstoqueFinder estoqueFinder;
	private final EstoqueGateway estoqueGateway;

	public EditarItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		this.estoqueFinder = estoqueFinder;
		this.estoqueGateway = estoqueGateway;
	}

	public EstoqueDTO edit(String codigo, EstoqueDTO estoqueDTO) {
		Estoque estoque = estoqueFinder.obterOuFalhar(codigo);

		EstoqueMapper.updateDomainFromDto(estoqueDTO, estoque);

		Estoque estoqueAtualizado = estoqueGateway.save(estoque);

		return domaintoDTO(estoqueAtualizado);
	}
}
