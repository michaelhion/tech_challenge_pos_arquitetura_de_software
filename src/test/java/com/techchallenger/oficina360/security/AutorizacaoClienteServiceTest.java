package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ClienteRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AutorizacaoClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private OrdemServicosRepository ordemServicosRepository;

    @Mock
    private Authentication authentication;

    private AutorizacaoClienteService service;

    @BeforeEach
    void setup() {
        service = new AutorizacaoClienteService(
                veiculoRepository,
                ordemServicosRepository
        );
    }

    @Test
    void devePermitirAcessoAoProprioDocumento() {

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        boolean resultado =
                service.podeAcessarClientePorDocumento(
                        "12345678901",
                        authentication
                );

        assertTrue(resultado);
    }

    @Test
    void naoDevePermitirAcessoADocumentoDeOutroCliente() {

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        boolean resultado =
                service.podeAcessarClientePorDocumento(
                        "99999999999",
                        authentication
                );

        assertFalse(resultado);
    }

    @Test
    void naoDevePermitirQuandoAuthenticationForNull() {

        boolean resultado =
                service.podeAcessarClientePorDocumento(
                        "12345678901",
                        null
                );

        assertFalse(resultado);
    }

    @Test
    void naoDevePermitirQuandoPrincipalNaoForUsuario() {

        when(authentication.getPrincipal())
                .thenReturn("qualquer-coisa");

        boolean resultado =
                service.podeAcessarClientePorDocumento(
                        "12345678901",
                        authentication
                );

        assertFalse(resultado);
    }

    @Test
    void devePermitirAcessoAoVeiculoDoCliente() {

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        VeiculoEntity veiculoEntity = VeiculoEntity.builder()
                .clienteDocumento("12345678901")
                .placa("ABC1D23")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculoEntity));

        boolean resultado =
                service.podeAcessarVeiculo(
                        "abc1d23",
                        authentication
                );

        assertTrue(resultado);
    }

    @Test
    void naoDevePermitirAcessoAoVeiculoDeOutroCliente() {

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        VeiculoEntity veiculoEntity = VeiculoEntity.builder()
                .clienteDocumento("99999999999")
                .placa("ABC1D23")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculoEntity));

        boolean resultado =
                service.podeAcessarVeiculo(
                        "ABC1D23",
                        authentication
                );

        assertFalse(resultado);
    }

    @Test
    void naoDevePermitirQuandoVeiculoNaoExistir() {

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        when(veiculoRepository.findByPlaca(anyString()))
                .thenReturn(Optional.empty());

        boolean resultado =
                service.podeAcessarVeiculo(
                        "ABC1D23",
                        authentication
                );

        assertFalse(resultado);
    }

    @Test
    void devePermitirAcessoAOrdemServicoDoCliente() {

        UUID id = UUID.randomUUID();

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        OrdemServicoEntity ordemServicoEntity = new OrdemServicoEntity();
        ordemServicoEntity.setDocumentoCliente("12345678901");

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        when(ordemServicosRepository.findById(id))
                .thenReturn(Optional.of(ordemServicoEntity));

        boolean resultado =
                service.podeAcessarOrdemServico(
                        id,
                        authentication
                );

        assertTrue(resultado);
    }

    @Test
    void naoDevePermitirAcessoAOrdemServicoDeOutroCliente() {

        UUID id = UUID.randomUUID();

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        OrdemServicoEntity ordemServicoEntity = new OrdemServicoEntity();
        ordemServicoEntity.setDocumentoCliente("99999999999");

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        when(ordemServicosRepository.findById(id))
                .thenReturn(Optional.of(ordemServicoEntity));

        boolean resultado =
                service.podeAcessarOrdemServico(
                        id,
                        authentication
                );

        assertFalse(resultado);
    }

    @Test
    void naoDevePermitirQuandoOrdemServicoNaoExistir() {

        UUID id = UUID.randomUUID();

        UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuarioEntity);

        when(ordemServicosRepository.findById(id))
                .thenReturn(Optional.empty());

        boolean resultado =
                service.podeAcessarOrdemServico(
                        id,
                        authentication
                );

        assertFalse(resultado);
    }
}