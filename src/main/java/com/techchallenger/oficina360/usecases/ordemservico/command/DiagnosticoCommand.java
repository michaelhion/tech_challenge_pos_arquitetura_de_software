package com.techchallenger.oficina360.usecases.ordemservico.command;

import java.util.List;

public record DiagnosticoCommand(
        List<String> codigosServicos,
        List<DiagnosticoEstoqueCommand> itensEstoque
) {
}