package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.security.UsuarioSecurityDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioAutenticadoGatewayImplTest {

	@Mock
	private Authentication authentication;

	@Mock
	private UsuarioSecurityDetails usuarioSecurityDetails;

	@Mock
	private Usuario usuario;

	private UsuarioAutenticadoGatewayImpl gateway;

	@BeforeEach
	void setUp() {
		gateway = new UsuarioAutenticadoGatewayImpl();

		SecurityContextHolder.clearContext();
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void deveRetornarUsuarioAutenticadoComSucesso() {
		configurarAuthenticationNoContexto();

		when(authentication.getPrincipal()).thenReturn(usuarioSecurityDetails);

		when(usuarioSecurityDetails.getUsuario()).thenReturn(usuario);

		Usuario resultado = gateway.obterUsuarioAtual();

		assertSame(usuario, resultado);

		verify(authentication).getPrincipal();

		verify(usuarioSecurityDetails).getUsuario();

		verifyNoMoreInteractions(authentication, usuarioSecurityDetails, usuario);
	}

	@Test
	void deveRetornarNullQuandoNaoHouverAuthentication() {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		context.setAuthentication(null);

		SecurityContextHolder.setContext(context);

		Usuario resultado = gateway.obterUsuarioAtual();

		assertNull(resultado);

		verifyNoMoreInteractions(authentication, usuarioSecurityDetails, usuario);
	}

	@Test
	void deveRetornarNullQuandoPrincipalNaoForUsuarioSecurityDetails() {
		configurarAuthenticationNoContexto();

		Object principalIncompativel = new Object();

		when(authentication.getPrincipal()).thenReturn(principalIncompativel);

		Usuario resultado = gateway.obterUsuarioAtual();

		assertNull(resultado);

		verify(authentication).getPrincipal();

		verify(usuarioSecurityDetails, never()).getUsuario();

		verifyNoMoreInteractions(authentication, usuarioSecurityDetails, usuario);
	}

	@Test
	void deveRetornarNullQuandoPrincipalForUmaString() {
		configurarAuthenticationNoContexto();

		when(authentication.getPrincipal()).thenReturn("anonymousUser");

		Usuario resultado = gateway.obterUsuarioAtual();

		assertNull(resultado);

		verify(authentication).getPrincipal();

		verify(usuarioSecurityDetails, never()).getUsuario();

		verifyNoMoreInteractions(authentication, usuarioSecurityDetails, usuario);
	}

	@Test
	void deveRetornarNullQuandoUsuarioDoSecurityDetailsForNulo() {
		configurarAuthenticationNoContexto();

		when(authentication.getPrincipal()).thenReturn(usuarioSecurityDetails);

		when(usuarioSecurityDetails.getUsuario()).thenReturn(null);

		Usuario resultado = gateway.obterUsuarioAtual();

		assertNull(resultado);

		verify(authentication).getPrincipal();

		verify(usuarioSecurityDetails).getUsuario();

		verifyNoMoreInteractions(authentication, usuarioSecurityDetails, usuario);
	}

	private void configurarAuthenticationNoContexto() {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}
}