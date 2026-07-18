package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.security.UsuarioSecurityDetails;
import com.techchallenger.oficina360.gateways.UsuarioAutenticadoGateway;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAutenticadoGatewayImpl implements UsuarioAutenticadoGateway {

	@Override
	public Usuario obterUsuarioAtual() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null) {
			return null;
		}

		Object principal = authentication.getPrincipal();

		if (!(principal instanceof UsuarioSecurityDetails details)) {
			return null;
		}

		return details.getUsuario();
	}
}
