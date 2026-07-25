package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dominio.Cliente;

import java.util.UUID;

public record ClienteOutput(UUID id, String documento, String nome, String email, String telefone) {

	public static ClienteOutput from(Cliente cliente) {
		return new ClienteOutput(cliente.getId(), cliente.getDocumento(), cliente.getNome(), cliente.getEmail(),
				cliente.getTelefone());
	}
}