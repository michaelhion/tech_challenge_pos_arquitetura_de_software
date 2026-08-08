package com.techchallenger.oficina360.usecases.ordemservico.command;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;

public record CriarOrdemServicoCommand(
		String documentoCliente,
		String placaVeiculo,
		String descricaoProblema,
		OrdemDeServicoStatus ordemDeServicoStatus
) {
}