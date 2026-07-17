package com.techchallenger.oficina360.dtos.servicos;

import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ClienteRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.UsuarioRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.security.JwtService;
import com.techchallenger.oficina360.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void deveAutenticarUsuarioComSucesso() {

        LoginRequestDTO request =
                new LoginRequestDTO(
                        "admin@oficina360.com",
                        "123456"
                );

        UsuarioEntity usuarioEntity =
                UsuarioEntity.builder()
                        .email("admin@oficina360.com")
                        .role("ADMIN")
                        .build();

        Authentication authentication =
                mock(Authentication.class);

        when(usuarioRepository.existsByEmail(
                request.email()))
                .thenReturn(true);

        when(authenticationManager.authenticate(any()))
                .thenReturn(authentication);

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        when(jwtService.gerarToken(usuarioEntity))
                .thenReturn("jwt-token");

        String token =
                authService.autenticar(request);

        assertEquals(
                "jwt-token",
                token
        );

        verify(jwtService)
                .gerarToken(usuarioEntity);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {

        LoginRequestDTO request =
                new LoginRequestDTO(
                        "naoexiste@email.com",
                        "123456"
                );

        when(usuarioRepository.existsByEmail(
                request.email()))
                .thenReturn(false);

        AccessDeniedException exception =
                assertThrows(
                        AccessDeniedException.class,
                        () -> authService.autenticar(request)
                );

        assertEquals(
                "Dados de login inválidos",
                exception.getMessage()
        );

        verify(authenticationManager, never())
                .authenticate(any());
    }

    @Test
    void deveCriarUsuarioClienteComSucesso() {

        CriarUsuarioRequestDTO request =
                new CriarUsuarioRequestDTO(
                        "cliente@email.com",
                        "123456",
                        "12345678901",
                        "CLIENTE"
                );

        when(usuarioRepository.existsByEmail(
                request.email()))
                .thenReturn(false);

        when(clienteRepository.findByDocumento(
                request.documento()))
                .thenReturn(Optional.of(mock(ClienteEntity.class)));

        authService.criarUsuario(request);

        verify(usuarioRepository)
                .save(any(UsuarioEntity.class));
    }

    @Test
    void deveLancarConflitoQuandoEmailJaExiste() {

        CriarUsuarioRequestDTO request =
                new CriarUsuarioRequestDTO(
                        "cliente@email.com",
                        "123456",
                        "12345678901",
                        "CLIENTE"
                );

        when(usuarioRepository.existsByEmail(
                request.email()))
                .thenReturn(true);

        assertThrows(
                ConflitoException.class,
                () -> authService.criarUsuario(request)
        );

        verify(usuarioRepository, never())
                .save(any());
    }

    @Test
    void deveLancarRecursoNaoEncontradoQuandoClienteNaoExiste() {

        CriarUsuarioRequestDTO request =
                new CriarUsuarioRequestDTO(
                        "cliente@email.com",
                        "123456",
                        "12345678901",
                        "CLIENTE"
                );

        when(usuarioRepository.existsByEmail(
                request.email()))
                .thenReturn(false);

        when(clienteRepository.findByDocumento(
                request.documento()))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> authService.criarUsuario(request)
        );
    }

    @Test
    void deveLancarExcecaoQuandoRoleForInvalida() {

        CriarUsuarioRequestDTO request =
                new CriarUsuarioRequestDTO(
                        "cliente@email.com",
                        "123456",
                        "12345678901",
                        "GERENTE"
                );

        when(usuarioRepository.existsByEmail(
                request.email()))
                .thenReturn(false);

        assertThrows(
                RegraDeNegocioException.class,
                () -> authService.criarUsuario(request)
        );
    }

}