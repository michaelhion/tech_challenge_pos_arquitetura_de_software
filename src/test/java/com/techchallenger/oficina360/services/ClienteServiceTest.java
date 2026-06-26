package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.entities.Cliente;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    private  static final String CPF = "***8901";
    public static final String DOCUMENTO = "12345678901";

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private UUID clienteId;
    private Cliente cliente;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();

        clienteDTO = new ClienteDTO(
                DOCUMENTO,
                "João da Silva",
                "joao.silva@email.com",
                "11999999999"
        );

        cliente = Cliente.builder()
                .id(clienteId)
                .documento(DOCUMENTO)
                .nome("João da Silva")
                .email("joao.silva@email.com")
                .telefone("11999999999")
                .build();
    }

    @Test
    void deveListarTodosOsClientes() {
        when(clienteRepository.findAll())
                .thenReturn(List.of(cliente));

        List<ClienteDTO> resultado = clienteService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        assertEquals(CPF, resultado.get(0).documento());
        assertEquals("João da Silva", resultado.get(0).nome());
        assertEquals("joao.silva@email.com", resultado.get(0).email());
        assertEquals("11999999999", resultado.get(0).telefone());

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
        when(clienteRepository.findAll())
                .thenReturn(List.of());

        List<ClienteDTO> resultado = clienteService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarClientePorDocumentoQuandoExistir() {
        when(clienteRepository.findByDocumento(DOCUMENTO))
                .thenReturn(Optional.of(cliente));

        Optional<ClienteDTO> resultado = clienteService.findByDocumento(DOCUMENTO);

        assertTrue(resultado.isPresent());
        assertEquals(CPF, resultado.get().documento());
        assertEquals("João da Silva", resultado.get().nome());
        assertEquals("joao.silva@email.com", resultado.get().email());
        assertEquals("11999999999", resultado.get().telefone());

        verify(clienteRepository, times(1)).findByDocumento(DOCUMENTO);
    }

    @Test
    void deveRetornarOptionalVazioQuandoClienteNaoExistirPorDocumento() {
        when(clienteRepository.findByDocumento("00000000000"))
                .thenReturn(Optional.empty());

        Optional<ClienteDTO> resultado = clienteService.findByDocumento("00000000000");

        assertTrue(resultado.isEmpty());

        verify(clienteRepository, times(1)).findByDocumento("00000000000");
    }

    @Test
    void deveSalvarClienteComSucesso() {
        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(cliente);

        ClienteDTO resultado = clienteService.save(clienteDTO);

        assertNotNull(resultado);
        assertEquals(CPF, resultado.documento());
        assertEquals("João da Silva", resultado.nome());
        assertEquals("joao.silva@email.com", resultado.email());
        assertEquals("11999999999", resultado.telefone());

        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void deveEditarClienteComSucesso() {
        ClienteDTO dtoAtualizado = new ClienteDTO(
                DOCUMENTO,
                "João da Silva Atualizado",
                "joao.atualizado@email.com",
                "11888888888"
        );

        Cliente clienteAtualizado = Cliente.builder()
                .id(clienteId)
                .documento(DOCUMENTO)
                .nome("João da Silva Atualizado")
                .email("joao.atualizado@email.com")
                .telefone("11888888888")
                .build();

        when(clienteRepository.findByDocumento(DOCUMENTO))
                .thenReturn(Optional.of(cliente));

        when(clienteRepository.save(any(Cliente.class)))
                .thenReturn(clienteAtualizado);

        ClienteDTO resultado = clienteService.edit(DOCUMENTO, dtoAtualizado);

        assertNotNull(resultado);
        assertEquals(CPF, resultado.documento());
        assertEquals("João da Silva Atualizado", resultado.nome());
        assertEquals("joao.atualizado@email.com", resultado.email());
        assertEquals("11888888888", resultado.telefone());

        verify(clienteRepository, times(1)).findByDocumento(DOCUMENTO);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void naoDeveEditarClienteQuandoNaoEncontrado() {
        when(clienteRepository.findByDocumento(DOCUMENTO))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> clienteService.edit(DOCUMENTO, clienteDTO)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());

        verify(clienteRepository, times(1)).findByDocumento(DOCUMENTO);
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deveDeletarClientePorDocumento() {
        doNothing().when(clienteRepository)
                .deleteByDocumento(DOCUMENTO);

        clienteService.delete(DOCUMENTO);

        verify(clienteRepository, times(1))
                .deleteByDocumento(DOCUMENTO);
    }
}
