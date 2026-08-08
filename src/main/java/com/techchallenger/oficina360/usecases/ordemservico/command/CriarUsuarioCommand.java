package com.techchallenger.oficina360.usecases.ordemservico.command;

public record CriarUsuarioCommand(
		String email,
		String senha,
		String documento,
		String role
) {
}
