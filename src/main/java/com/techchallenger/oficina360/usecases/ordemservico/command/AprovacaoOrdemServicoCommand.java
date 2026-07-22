package com.techchallenger.oficina360.usecases.ordemservico.command;

public record AprovacaoOrdemServicoCommand(
		Boolean aprovado,
		String observacao
) {}
