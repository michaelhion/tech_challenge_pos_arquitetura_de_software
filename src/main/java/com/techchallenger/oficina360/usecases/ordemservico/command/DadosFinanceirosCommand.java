package com.techchallenger.oficina360.usecases.ordemservico.command;

import java.math.BigDecimal;
import java.util.List;

public record DadosFinanceirosCommand(
		List<ServicosAdicionadosCommand> servicos,
		List<PecasInsumosAdicionadosCommand> pecasInsumos,
		BigDecimal valorServicos,
		BigDecimal valorPecasInsumos,
		BigDecimal valorTotal
) {
}
