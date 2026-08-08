package com.techchallenger.oficina360.usecases.ordemservico.command;

public record ClienteCommand(
        String documento,
        String nome,
        String email,
        String telefone
) {
}