package com.techchallenger.oficina360.usecases.auth;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.gateways.AuthenticationGateway;
import com.techchallenger.oficina360.gateways.TokenGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.UsuarioCommand;

public class AutenticarUsuarioUseCase {

	private final TokenGateway tokenGateway;
	private final AuthenticationGateway authenticationGateway;

	public AutenticarUsuarioUseCase(TokenGateway tokenGateway, AuthenticationGateway authenticationGateway) {
		this.tokenGateway = tokenGateway;
		this.authenticationGateway = authenticationGateway;
	}

	public String executar(UsuarioCommand dto) {

		Usuario usuario = authenticationGateway.autenticar(dto.email(), dto.senha());

		return tokenGateway.gerarToken(usuario);
	}
}
