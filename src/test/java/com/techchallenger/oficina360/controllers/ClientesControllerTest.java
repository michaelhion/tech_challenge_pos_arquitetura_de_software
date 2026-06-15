package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClientesControllerTest {

    @Mock
    private ClienteService clienteService;

    private ClientesController clientesController;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clientesController = new ClientesController(clienteService);

        clienteDTO = new ClienteDTO(
                "12345678901",
                "João da Silva",
                "joao.silva@email.com",
                "11999999999"
        );
    }

    @Test
    void deveBuscarClientePorDocumentoComSucesso() {
        String documento = "12345678901";

        when(clienteService.findByDocumento(documento))
                .thenReturn(Optional.of(clienteDTO));

        ResponseEntity<ClienteDTO> response = clientesController.buscarPorDocumento(documento);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345678901", response.getBody().documento());
        assertEquals("João da Silva", response.getBody().nome());
        assertEquals("joao.silva@email.com", response.getBody().email());
        assertEquals("11999999999", response.getBody().telefone());

        verify(clienteService, times(1)).findByDocumento(documento);
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarClientePorDocumentoInexistente() {
        String documento = "00000000000";

        when(clienteService.findByDocumento(documento))
                .thenReturn(Optional.empty());

        ResponseEntity<ClienteDTO> response = clientesController.buscarPorDocumento(documento);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(clienteService, times(1)).findByDocumento(documento);
    }

    @Test
    void deveSalvarClienteComSucesso() {
        when(clienteService.save(clienteDTO))
                .thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = clientesController.salvar(clienteDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345678901", response.getBody().documento());
        assertEquals("João da Silva", response.getBody().nome());
        assertEquals("joao.silva@email.com", response.getBody().email());
        assertEquals("11999999999", response.getBody().telefone());

        verify(clienteService, times(1)).save(clienteDTO);
    }

    @Test
    void deveEditarClienteComSucesso() {
        UUID id = UUID.randomUUID();

        ClienteDTO clienteAtualizado = new ClienteDTO(
                "12345678901",
                "João da Silva Atualizado",
                "joao.atualizado@email.com",
                "11888888888"
        );

        when(clienteService.edit(id, clienteAtualizado))
                .thenReturn(clienteAtualizado);

        ResponseEntity<ClienteDTO> response = clientesController.editar(id, clienteAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345678901", response.getBody().documento());
        assertEquals("João da Silva Atualizado", response.getBody().nome());
        assertEquals("joao.atualizado@email.com", response.getBody().email());
        assertEquals("11888888888", response.getBody().telefone());

        verify(clienteService, times(1)).edit(id, clienteAtualizado);
    }

    @Test
    void deveDeletarClientePorDocumentoComSucesso() {
        String documento = "12345678901";

        doNothing().when(clienteService).delete(documento);

        ResponseEntity<Void> response = clientesController.deletar(documento);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(clienteService, times(1)).delete(documento);
    }

    @Test
    void deveListarClientesComSucesso() {
        ClienteDTO segundoCliente = new ClienteDTO(
                "98765432100",
                "Maria Oliveira",
                "maria.oliveira@email.com",
                "11988887777"
        );

        when(clienteService.findAll())
                .thenReturn(List.of(clienteDTO, segundoCliente));

        ResponseEntity<List<ClienteDTO>> response = clientesController.listarClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals("12345678901", response.getBody().get(0).documento());
        assertEquals("João da Silva", response.getBody().get(0).nome());
        assertEquals("joao.silva@email.com", response.getBody().get(0).email());
        assertEquals("11999999999", response.getBody().get(0).telefone());

        assertEquals("98765432100", response.getBody().get(1).documento());
        assertEquals("Maria Oliveira", response.getBody().get(1).nome());
        assertEquals("maria.oliveira@email.com", response.getBody().get(1).email());
        assertEquals("11988887777", response.getBody().get(1).telefone());

        verify(clienteService, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
        when(clienteService.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<ClienteDTO>> response = clientesController.listarClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(clienteService, times(1)).findAll();
    }
}

