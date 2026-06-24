package com.techchallenger.oficina360.security;

import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.repositories.VeiculoRepository;
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

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuario);

        boolean resultado =
                service.podeAcessarClientePorDocumento(
                        "12345678901",
                        authentication
                );

        assertTrue(resultado);
    }

    @Test
    void naoDevePermitirAcessoADocumentoDeOutroCliente() {

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuario);

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

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        Veiculo veiculo = Veiculo.builder()
                .clienteDocumento("12345678901")
                .placa("ABC1D23")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuario);

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculo));

        boolean resultado =
                service.podeAcessarVeiculo(
                        "abc1d23",
                        authentication
                );

        assertTrue(resultado);
    }

    @Test
    void naoDevePermitirAcessoAoVeiculoDeOutroCliente() {

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        Veiculo veiculo = Veiculo.builder()
                .clienteDocumento("99999999999")
                .placa("ABC1D23")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuario);

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculo));

        boolean resultado =
                service.podeAcessarVeiculo(
                        "ABC1D23",
                        authentication
                );

        assertFalse(resultado);
    }

    @Test
    void naoDevePermitirQuandoVeiculoNaoExistir() {

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuario);

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

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setDocumentoCliente("12345678901");

        when(authentication.getPrincipal())
                .thenReturn(usuario);

        when(ordemServicosRepository.findById(id))
                .thenReturn(Optional.of(ordemServico));

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

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        OrdemServico ordemServico = new OrdemServico();
        ordemServico.setDocumentoCliente("99999999999");

        when(authentication.getPrincipal())
                .thenReturn(usuario);

        when(ordemServicosRepository.findById(id))
                .thenReturn(Optional.of(ordemServico));

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

        Usuario usuario = Usuario.builder()
                .documento("12345678901")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(usuario);

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