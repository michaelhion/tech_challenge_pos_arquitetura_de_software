package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginResponseDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.AuthController;
import com.techchallenger.oficina360.usecases.auth.AutenticarUsuarioUseCase;
import com.techchallenger.oficina360.usecases.auth.CriarUsuarioUseCase;
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

        LoginRequestDTO request =
                new LoginRequestDTO(
                        "admin@oficina360.com",
                        "123456"
                );

        when(autenticarUsuarioUseCase.executar(request))
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

        verify(autenticarUsuarioUseCase)
                .executar(request);
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

        verify(criarUsuarioUseCase)
                .executar(request);
    }

}