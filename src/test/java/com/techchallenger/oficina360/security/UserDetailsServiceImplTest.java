package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private UsuarioRepository usuarioRepository;

    private UserDetailsServiceImpl userDetailsService;

    private UsuarioEntity usuarioEntity;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(usuarioRepository);

        usuarioEntity = UsuarioEntity.builder()
                .id(UUID.randomUUID())
                .email("admin@oficina360.com")
                .senha("senha-criptografada")
                .role("ADMIN")
                .build();
    }

    @Test
    void deveCarregarUsuarioPorEmailComSucesso() {
        when(usuarioRepository.findByEmail("admin@oficina360.com"))
                .thenReturn(Optional.of(usuarioEntity));

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@oficina360.com");

        assertNotNull(userDetails);
        assertEquals("admin@oficina360.com", userDetails.getUsername());
        assertEquals("senha-criptografada", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")));

        verify(usuarioRepository, times(1))
                .findByEmail("admin@oficina360.com");
    }

    @Test
    void deveLancarExceptionQuandoUsuarioNaoForEncontrado() {
        when(usuarioRepository.findByEmail("inexistente@oficina360.com"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("inexistente@oficina360.com")
        );

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(usuarioRepository, times(1))
                .findByEmail("inexistente@oficina360.com");
    }
}