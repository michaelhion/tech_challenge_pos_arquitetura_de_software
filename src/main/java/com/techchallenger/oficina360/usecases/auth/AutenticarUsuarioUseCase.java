package com.techchallenger.oficina360.usecases.auth;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.gateways.AuthenticationGateway;
import com.techchallenger.oficina360.gateways.TokenGateway;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import org.springframework.security.authentication.AuthenticationManager;

public class AutenticarUsuarioUseCase {

	private final AuthenticationManager authenticationManager;
	private final UsuarioGateway usuarioGateway;
	private final TokenGateway tokenGateway;
	private final AuthenticationGateway authenticationGateway;

	public AutenticarUsuarioUseCase(AuthenticationManager authenticationManager, UsuarioGateway usuarioGateway,
			TokenGateway tokenGateway, AuthenticationGateway authenticationGateway) {
		this.authenticationManager = authenticationManager;
		this.usuarioGateway = usuarioGateway;
		this.tokenGateway = tokenGateway;
		this.authenticationGateway = authenticationGateway;
	}

	public String executar(LoginRequestDTO dto) {


		Usuario usuario =
				authenticationGateway.autenticar(
						dto.email(),
						dto.senha()
				);


		return tokenGateway.gerarToken(usuario);
	}
}
