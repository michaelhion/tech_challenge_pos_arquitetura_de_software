package com.techchallenger.oficina360.usecases.ordemservico.command;

import java.math.BigDecimal;

public record PecasInsumosAdicionadosCommand(
		String nome,
		BigDecimal valorUnitario,
		Integer quantidade,
		BigDecimal valorTotal
) {
}
