package com.techchallenger.oficina360.usecases.veiculo.commands;

public record VeiculoCommand(
        String placa,
        String marca,
        String modelo,
        String ano,
        String clienteDocumento
) {
}