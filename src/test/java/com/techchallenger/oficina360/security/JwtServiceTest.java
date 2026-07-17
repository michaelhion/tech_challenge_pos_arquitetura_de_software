package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secret", "segredo-teste-jwt");
        ReflectionTestUtils.setField(jwtService, "issuer", "oficina360-api-test");
        ReflectionTestUtils.setField(jwtService, "expirationHours", 2L);

        usuarioEntity = UsuarioEntity.builder()
                .id(UUID.randomUUID())
                .email("admin@oficina360.com")
                .senha("senha-criptografada")
                .role("ADMIN")
                .build();
    }

    @Test
    void deveGerarTokenJwtComSucesso() {
        String token = jwtService.gerarToken(usuarioEntity);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deveValidarTokenEObterSubject() {
        String token = jwtService.gerarToken(usuarioEntity);

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