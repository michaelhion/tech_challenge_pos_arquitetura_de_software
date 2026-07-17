package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;

import static com.techchallenger.oficina360.mappers.EstoqueMapper.domaintoDTO;
import static com.techchallenger.oficina360.mappers.EstoqueMapper.toDomain;

public class CriarItemEstoqueUseCase {

	private final EstoqueFinder estoqueFinder;
	private final EstoqueGateway estoqueGateway;

	public CriarItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		this.estoqueFinder = estoqueFinder;
		this.estoqueGateway = estoqueGateway;
	}

	public EstoqueDTO save(EstoqueDTO estoqueDTO) {
		estoqueFinder.obterOuFalhar(estoqueDTO.codigo());
		Estoque estoqueSaved = estoqueGateway.save(toDomain(estoqueDTO));
		return domaintoDTO(estoqueSaved);
	}


}
