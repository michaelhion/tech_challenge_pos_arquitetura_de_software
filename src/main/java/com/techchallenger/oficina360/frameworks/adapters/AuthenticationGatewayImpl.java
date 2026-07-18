package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.security.UsuarioSecurityDetails;
import com.techchallenger.oficina360.gateways.AuthenticationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationGatewayImpl implements AuthenticationGateway {

	private final AuthenticationManager authenticationManager;

	@Override
	public Usuario autenticar(String email, String senha) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(email, senha));

		UsuarioSecurityDetails principal = (UsuarioSecurityDetails) authentication.getPrincipal();

		return principal.getUsuario();
	}
}
