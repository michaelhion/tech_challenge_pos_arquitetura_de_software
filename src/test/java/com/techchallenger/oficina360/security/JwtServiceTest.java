package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.entities.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secret", "segredo-teste-jwt");
        ReflectionTestUtils.setField(jwtService, "issuer", "oficina360-api-test");
        ReflectionTestUtils.setField(jwtService, "expirationHours", 2L);

        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .email("admin@oficina360.com")
                .senha("senha-criptografada")
                .role("ADMIN")
                .build();
    }

    @Test
    void deveGerarTokenJwtComSucesso() {
        String token = jwtService.gerarToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deveValidarTokenEObterSubject() {
        String token = jwtService.gerarToken(usuario);

        String subject = jwtService.validarTokenEObterSubject(token);

        assertEquals("admin@oficina360.com", subject);
    }

    @Test
    void deveLancarExceptionQuandoTokenForInvalido() {
        assertThrows(
                RuntimeException.class,
                () -> jwtService.validarTokenEObterSubject("token-invalido")
        );
    }
}