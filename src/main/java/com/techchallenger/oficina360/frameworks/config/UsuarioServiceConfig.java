package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.AuthenticationGateway;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.gateways.PasswordEncoderGateway;
import com.techchallenger.oficina360.gateways.TokenGateway;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import com.techchallenger.oficina360.usecases.auth.AutenticarUsuarioUseCase;
import com.techchallenger.oficina360.usecases.auth.CriarUsuarioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioServiceConfig {

	@Bean
	@Transactional
	public AutenticarUsuarioUseCase autenticarUsuarioUseCase(UsuarioGateway usuarioGateway,
			TokenGateway tokenGateway, AuthenticationGateway authenticationGateway) {
		return new AutenticarUsuarioUseCase(usuarioGateway,tokenGateway,authenticationGateway);

	}

	@Bean
	@Transactional
	public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioGateway usuarioGateway,
			ClienteGateway clienteGateway,
			PasswordEncoderGateway passwordEncoderGateway) {
		return new CriarUsuarioUseCase(usuarioGateway,clienteGateway,passwordEncoderGateway);

	}

}
