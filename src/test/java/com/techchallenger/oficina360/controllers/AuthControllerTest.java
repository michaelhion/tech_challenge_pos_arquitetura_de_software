package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginResponseDTO;
import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private AuthController authController;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, jwtService);

        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .email("admin@oficina360.com")
                .senha("senha-criptografada")
                .role("ADMIN")
                .build();
    }

    @Test
    void deveAutenticarUsuarioERetornarTokenJwt() {
        LoginRequestDTO request = new LoginRequestDTO(
                "admin@oficina360.com",
                "123456"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(usuario);

        when(jwtService.gerarToken(usuario))
                .thenReturn("token-jwt-gerado");

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("token-jwt-gerado", response.getBody().token());
        assertEquals("Bearer", response.getBody().tipo());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(authentication, times(1))
                .getPrincipal();

        verify(jwtService, times(1))
                .gerarToken(usuario);
    }
}