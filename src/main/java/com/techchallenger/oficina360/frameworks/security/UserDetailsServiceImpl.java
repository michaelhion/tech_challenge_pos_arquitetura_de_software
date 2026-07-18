package com.techchallenger.oficina360.frameworks.security;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final String USUARIO_NAO_ENCONTRADO =
			"Usuário não encontrado";

	private final UsuarioGateway usuarioGateway;

	@Override
	public UserDetails loadUserByUsername(String email)
			throws UsernameNotFoundException {

		Usuario usuario = usuarioGateway.findByEmail(email)
						.orElseThrow(() -> new UsernameNotFoundException(USUARIO_NAO_ENCONTRADO));
		return new UsuarioSecurityDetails(usuario);
	}
}