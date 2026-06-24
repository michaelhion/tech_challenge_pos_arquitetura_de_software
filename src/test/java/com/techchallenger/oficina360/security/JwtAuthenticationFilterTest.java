package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private AutoCloseable closeable;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, usuarioRepository);

        usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .email("admin@oficina360.com")
                .senha("senha-criptografada")
                .role("ADMIN")
                .build();

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() throws Exception {
        SecurityContextHolder.clearContext();
        closeable.close();
    }

    @Test
    void deveContinuarFiltroQuandoNaoExistirHeaderAuthorization() throws Exception {
        when(request.getHeader("Authorization"))
                .thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService, never()).validarTokenEObterSubject(anyString());
        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveContinuarFiltroQuandoHeaderAuthorizationNaoForBearer() throws Exception {
        when(request.getHeader("Authorization"))
                .thenReturn("Basic abc123");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService, never()).validarTokenEObterSubject(anyString());
        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void deveAutenticarUsuarioQuandoTokenForValido() throws Exception {
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token-valido");

        when(jwtService.validarTokenEObterSubject("token-valido"))
                .thenReturn("admin@oficina360.com");

        when(usuarioRepository.findByEmail("admin@oficina360.com"))
                .thenReturn(Optional.of(usuario));

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(
                "admin@oficina360.com",
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        verify(jwtService, times(1))
                .validarTokenEObterSubject("token-valido");

        verify(usuarioRepository, times(1))
                .findByEmail("admin@oficina360.com");

        verify(filterChain, times(1))
                .doFilter(request, response);
    }

    @Test
    void deveContinuarFiltroQuandoTokenValidoMasUsuarioNaoEncontrado() throws Exception {
        when(request.getHeader("Authorization"))
                .thenReturn("Bearer token-valido");

        when(jwtService.validarTokenEObterSubject("token-valido"))
                .thenReturn("admin@oficina360.com");

        when(usuarioRepository.findByEmail("admin@oficina360.com"))
                .thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService, times(1))
                .validarTokenEObterSubject("token-valido");

        verify(usuarioRepository, times(1))
                .findByEmail("admin@oficina360.com");

        verify(filterChain, times(1))
                .doFilter(request, response);
    }
}