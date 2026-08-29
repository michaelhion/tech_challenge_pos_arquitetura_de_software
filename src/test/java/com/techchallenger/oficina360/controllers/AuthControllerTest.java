package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.frameworks.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.frameworks.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.frameworks.dtos.autenticacao.LoginResponseDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.AuthController;
import com.techchallenger.oficina360.usecases.auth.AutenticarUsuarioUseCase;
import com.techchallenger.oficina360.usecases.auth.CriarUsuarioUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.command.CriarUsuarioCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.UsuarioCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AutenticarUsuarioUseCase autenticarUsuarioUseCase;

	@Mock
	private CriarUsuarioUseCase criarUsuarioUseCase;

	@InjectMocks
	private AuthController authController;

	@Test
	void deveAutenticarUsuarioERetornarTokenJwt() {

		LoginRequestDTO request = new LoginRequestDTO("admin@oficina360.com", "123456");

		UsuarioCommand command = new UsuarioCommand("admin@oficina360.com", "123456");

		when(autenticarUsuarioUseCase.executar(command)).thenReturn("token-jwt-gerado");

		ResponseEntity<LoginResponseDTO> response = authController.login(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEquals("token-jwt-gerado", response.getBody().token());

		assertEquals("Bearer", response.getBody().tipo());

		verify(autenticarUsuarioUseCase).executar(command);
	}

	@Test
	void deveCriarUsuarioComSucesso() {

		CriarUsuarioCommand command = new CriarUsuarioCommand("Novo Usuario", "novo.usuario@oficina360.com",
				"123456", "ADMIN");
		CriarUsuarioRequestDTO dto = new CriarUsuarioRequestDTO("Novo Usuario", "novo.usuario@oficina360.com",
				"123456", "ADMIN");

		ResponseEntity<String> response = authController.criarUsuario(dto);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertEquals("Usuário criado com sucesso!", response.getBody());

		verify(criarUsuarioUseCase).executar(command);
	}

}