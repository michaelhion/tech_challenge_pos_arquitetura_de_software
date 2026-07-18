package com.techchallenger.oficina360.frameworks.security;

import com.techchallenger.oficina360.dominio.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UsuarioSecurityDetails implements UserDetails {

	private final Usuario usuario;

	public UsuarioSecurityDetails(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRole()));
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getEmail();
	}

	public Usuario getUsuario() {
		return usuario;
	}
}