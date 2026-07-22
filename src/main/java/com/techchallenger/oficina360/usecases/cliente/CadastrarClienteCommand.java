package com.techchallenger.oficina360.usecases.cliente;

public record CadastrarClienteCommand(
		String documento,
		String nome,
		String email,
		String telefone
) {
}