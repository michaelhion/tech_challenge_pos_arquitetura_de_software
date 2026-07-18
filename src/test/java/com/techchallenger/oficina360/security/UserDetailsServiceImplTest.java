package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.security.UserDetailsServiceImpl;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    private static final String DOCUMENTO = "27631184020";
    private static final String EMAIL = "admin@oficina360.com";
    private static final String SENHA_CRIPTOGRAFADA = "senha-criptografada";
    private static final String ROLE = "ADMIN";

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario(
                UUID.randomUUID(),
                EMAIL,
                SENHA_CRIPTOGRAFADA,
                ROLE,
                DOCUMENTO
        );
    }

    @Test
    void deveCarregarUsuarioPorEmailComSucesso() {
        when(usuarioGateway.findByEmail("admin@oficina360.com"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@oficina360.com");

        assertNotNull(userDetails);
        assertEquals("admin@oficina360.com", userDetails.getUsername());
        assertEquals("senha-criptografada", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));

        verify(usuarioGateway, times(1))
                .findByEmail("admin@oficina360.com");
    }

    @Test
    void deveLancarExceptionQuandoUsuarioNaoForEncontrado() {
        when(usuarioGateway.findByEmail("inexistente@oficina360.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("inexistente@oficina360.com")
        );

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(usuarioGateway, times(1))
                .findByEmail("inexistente@oficina360.com");
    }
}