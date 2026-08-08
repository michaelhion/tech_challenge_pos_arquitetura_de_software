package com.techchallenger.oficina360.usecases.ordemservico.command;

public record OrdemServicoReqCommand(
		String documentoCliente,
		String placaVeiculo,
		String descricaoProblema

) {
}