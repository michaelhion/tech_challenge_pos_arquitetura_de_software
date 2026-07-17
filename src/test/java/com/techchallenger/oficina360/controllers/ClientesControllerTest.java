package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.ClientesController;
import com.techchallenger.oficina360.usecases.cliente.AtualizarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.BuscarClientePorDocumentoUseCase;
import com.techchallenger.oficina360.usecases.cliente.CadastrarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ExcluirClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ListarClientesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClientesControllerTest {

    public static final String DOCUMENTO = "12345678901";
    @Mock
    private BuscarClientePorDocumentoUseCase buscarClientePorDocumentoUseCase;

    @Mock
    private CadastrarClienteUseCase cadastrarClienteUseCase;

    @Mock
    private ExcluirClienteUseCase excluirClienteUseCase;

    @Mock
    private ListarClientesUseCase listarClientesUseCase;

    @Mock
    private AtualizarClienteUseCase atualizarClienteUseCase;

    @InjectMocks
    private ClientesController clientesController;

    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        clienteDTO = new ClienteDTO(
                DOCUMENTO,
                "João da Silva",
                "joao.silva@email.com",
                "11999999999"
        );
    }

    @Test
    void deveBuscarClientePorDocumentoComSucesso() {
        String documento = DOCUMENTO;

        when(buscarClientePorDocumentoUseCase.findByDocumento(documento))
                .thenReturn(Optional.of(clienteDTO));

        ResponseEntity<ClienteDTO> response = clientesController.buscarPorDocumento(documento);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(DOCUMENTO, response.getBody().documento());
        assertEquals("João da Silva", response.getBody().nome());
        assertEquals("joao.silva@email.com", response.getBody().email());
        assertEquals("11999999999", response.getBody().telefone());

        verify(buscarClientePorDocumentoUseCase, times(1)).findByDocumento(documento);
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarClientePorDocumentoInexistente() {
        String documento = "00000000000";

        when(buscarClientePorDocumentoUseCase.findByDocumento(documento))
                .thenReturn(Optional.empty());

        ResponseEntity<ClienteDTO> response = clientesController.buscarPorDocumento(documento);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(buscarClientePorDocumentoUseCase, times(1)).findByDocumento(documento);
    }

    @Test
    void deveSalvarClienteComSucesso() {
        when(cadastrarClienteUseCase.save(clienteDTO))
                .thenReturn(clienteDTO);

        ResponseEntity<ClienteDTO> response = clientesController.salvar(clienteDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(DOCUMENTO, response.getBody().documento());
        assertEquals("João da Silva", response.getBody().nome());
        assertEquals("joao.silva@email.com", response.getBody().email());
        assertEquals("11999999999", response.getBody().telefone());

        verify(cadastrarClienteUseCase, times(1)).save(clienteDTO);
    }

    @Test
    void deveEditarClienteComSucesso() {

        ClienteDTO clienteAtualizado = new ClienteDTO(
                DOCUMENTO,
                "João da Silva Atualizado",
                "joao.atualizado@email.com",
                "11888888888"
        );

        when(atualizarClienteUseCase.edit(DOCUMENTO, clienteAtualizado))
                .thenReturn(clienteAtualizado);

        ResponseEntity<ClienteDTO> response = clientesController.editar(DOCUMENTO, clienteAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(DOCUMENTO, response.getBody().documento());
        assertEquals("João da Silva Atualizado", response.getBody().nome());
        assertEquals("joao.atualizado@email.com", response.getBody().email());
        assertEquals("11888888888", response.getBody().telefone());

        verify(atualizarClienteUseCase, times(1)).edit(DOCUMENTO, clienteAtualizado);
    }

    @Test
    void deveDeletarClientePorDocumentoComSucesso() {
        String documento = DOCUMENTO;

        doNothing().when(excluirClienteUseCase).delete(documento);

        ResponseEntity<Void> response = clientesController.deletar(documento);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(excluirClienteUseCase, times(1)).delete(documento);
    }

    @Test
    void deveListarClientesComSucesso() {
        ClienteDTO segundoCliente = new ClienteDTO(
                "98765432100",
                "Maria Oliveira",
                "maria.oliveira@email.com",
                "11988887777"
        );

        when(listarClientesUseCase.findAll())
                .thenReturn(List.of(clienteDTO, segundoCliente));

        ResponseEntity<List<ClienteDTO>> response = clientesController.listarClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals(DOCUMENTO, response.getBody().get(0).documento());
        assertEquals("João da Silva", response.getBody().get(0).nome());
        assertEquals("joao.silva@email.com", response.getBody().get(0).email());
        assertEquals("11999999999", response.getBody().get(0).telefone());

        assertEquals("98765432100", response.getBody().get(1).documento());
        assertEquals("Maria Oliveira", response.getBody().get(1).nome());
        assertEquals("maria.oliveira@email.com", response.getBody().get(1).email());
        assertEquals("11988887777", response.getBody().get(1).telefone());

        verify(listarClientesUseCase, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
        when(listarClientesUseCase.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<ClienteDTO>> response = clientesController.listarClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(listarClientesUseCase, times(1)).findAll();
    }
}

