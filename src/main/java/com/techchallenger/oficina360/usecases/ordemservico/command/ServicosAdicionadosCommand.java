package com.techchallenger.oficina360.usecases.ordemservico.command;

import java.math.BigDecimal;

public record ServicosAdicionadosCommand(
		String nome,
		BigDecimal valor
) {
}
