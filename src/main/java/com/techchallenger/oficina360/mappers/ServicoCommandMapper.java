package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;

public class ServicoCommandMapper {

	private ServicoCommandMapper() {
	}

	public static ServicoCommand domainToCommand(Servico domain) {
		return new ServicoCommand(
				domain.getCodigo(),
				domain.getDescricao(),
				domain.getValor(),
				domain.getTempoMedioExecucaoMinutos()
		);
	}

	public static Servico commandToDomain(ServicoCommand command) {
		return new Servico(
				null,
				command.descricao(),
				command.valor(),
				command.codigo()
		);
	}
}
