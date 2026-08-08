package com.techchallenger.oficina360.usecases.servicos.commands;

import java.math.BigDecimal;

public record ServicoCommand(
        String codigo,
        String descricao,
        BigDecimal valor,
        Integer tempoDeExecucaoMedio
) {
}