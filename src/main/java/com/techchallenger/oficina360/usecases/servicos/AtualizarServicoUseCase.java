package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.SERVICO_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.mappers.ServicoCommandMapper.domainToCommand;

public class AtualizarServicoUseCase {

	private final ServicoGateway servicoGateway;

	public AtualizarServicoUseCase(ServicoGateway servicoGateway) {
		this.servicoGateway = servicoGateway;
	}

	public ServicoCommand edit(String codigo, ServicoCommand command) {
		Servico servico = servicoGateway.findByCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));
		servico.editar(command.descricao(),command.valor(),command.codigo());

		Servico servicoAtualizado = servicoGateway.save(servico);

		return domainToCommand(servicoAtualizado);
	}
}
