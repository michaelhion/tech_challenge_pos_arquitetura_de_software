package com.techchallenger.oficina360.usecases.ordemservico.command;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;

import java.util.UUID;

public record OrdemServicoDiagnosticoRespCommand(
		UUID id,
		String documentoCliente,
		String placaVeiculo,
		String descricaoProblema,
		OrdemDeServicoStatus status,
		DadosFinanceirosCommand dadosFinanceirosCommand
) {
}