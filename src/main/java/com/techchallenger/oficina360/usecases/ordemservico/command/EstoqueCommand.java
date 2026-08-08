package com.techchallenger.oficina360.usecases.ordemservico.command;

import java.math.BigDecimal;

public record EstoqueCommand(
        String codigo,
        String nome,
        BigDecimal valor,
        Integer quantidade,
        Integer reservados,
		Integer disponiveis
) {
}