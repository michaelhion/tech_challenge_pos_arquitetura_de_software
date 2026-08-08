package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;

import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA;
import static com.techchallenger.oficina360.mappers.EstoqueCommandMapper.commandToDomain;
import static com.techchallenger.oficina360.mappers.EstoqueCommandMapper.domaintoCommand;

public class CriarItemEstoqueUseCase {

	private final EstoqueFinder estoqueFinder;
	private final EstoqueGateway estoqueGateway;

	public CriarItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		this.estoqueFinder = estoqueFinder;
		this.estoqueGateway = estoqueGateway;
	}

	public EstoqueCommand save(EstoqueCommand command) {
		Optional<Estoque> obter = estoqueFinder.obter(command.codigo());
		if(obter.isPresent()){
			throw new RegraDeNegocioException(ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA);
		}
		Estoque estoqueSaved = estoqueGateway.save(commandToDomain(command));
		return domaintoCommand(estoqueSaved);
	}


}
