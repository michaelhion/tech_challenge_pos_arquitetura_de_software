package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginResponseDTO;
import com.techchallenger.oficina360.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @Test
    void deveAutenticarUsuarioERetornarTokenJwt() {

        LoginRequestDTO request =
                new LoginRequestDTO(
                        "admin@oficina360.com",
                        "123456"
                );

        when(authService.autenticar(request))
                .thenReturn("token-jwt-gerado");

        ResponseEntity<LoginResponseDTO> response =
                authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(
                "token-jwt-gerado",
                response.getBody().token()
        );

        assertEquals(
                "Bearer",
                response.getBody().tipo()
        );

        verify(authService)
                .autenticar(request);
    }


    @Test
    void deveCriarUsuarioComSucesso() {

        CriarUsuarioRequestDTO request =
                new CriarUsuarioRequestDTO(
                        "Novo Usuario",
                        "novo.usuario@oficina360.com",
                        "123456",
                        "ADMIN"
                );

        ResponseEntity<String> response =
                authController.criarUsuario(request);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertEquals(
                "Usuário criado com sucesso!",
                response.getBody()
        );

        verify(authService)
                .criarUsuario(request);
    }

}