package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;

import static com.techchallenger.oficina360.mappers.ServicoCommandMapper.commandToDomain;
import static com.techchallenger.oficina360.mappers.ServicoCommandMapper.domainToCommand;

public class CadastrarServicoUseCase {

	private final ServicoGateway servicoGateway;

	public CadastrarServicoUseCase(ServicoGateway servicoGateway) {
		this.servicoGateway = servicoGateway;
	}

	public ServicoCommand save(ServicoCommand command) {
		Servico servico = new Servico();
		servico.inicializaTempoDeExecucao(0);
		Servico servicoSalvo = servicoGateway.save(commandToDomain(command));

		return domainToCommand(servicoSalvo);
	}
}
