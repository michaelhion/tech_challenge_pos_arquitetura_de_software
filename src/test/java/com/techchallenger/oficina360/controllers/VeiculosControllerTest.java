package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.VeiculosController;
import com.techchallenger.oficina360.services.VeiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculosControllerTest {

    @Mock
    private VeiculoService veiculoService;

    private VeiculosController veiculosController;

    private VeiculoDTO veiculoDTO;

    @BeforeEach
    void setUp() {
        veiculosController = new VeiculosController(veiculoService);

        veiculoDTO = new VeiculoDTO(
                "ABC1D23",
                "Volkswagen",
                "Gol",
                2020,
                "12345678901"
        );
    }

    @Test
    void deveBuscarVeiculoPorPlacaComSucesso() {
        when(veiculoService.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculoDTO));

        ResponseEntity<VeiculoDTO> response =
                veiculosController.buscarPorPlaca("ABC1D23");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ABC1D23", response.getBody().placa());
        assertEquals("Volkswagen", response.getBody().marca());
        assertEquals("Gol", response.getBody().modelo());
        assertEquals(2020, response.getBody().ano());
        assertEquals("12345678901", response.getBody().clienteDocumento());

        verify(veiculoService, times(1)).findByPlaca("ABC1D23");
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarVeiculoPorPlacaInexistente() {
        when(veiculoService.findByPlaca("ZZZ9Z99"))
                .thenReturn(Optional.empty());

        ResponseEntity<VeiculoDTO> response =
                veiculosController.buscarPorPlaca("ZZZ9Z99");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(veiculoService, times(1)).findByPlaca("ZZZ9Z99");
    }

    @Test
    void deveSalvarVeiculoComSucesso() {
        when(veiculoService.save(veiculoDTO))
                .thenReturn(veiculoDTO);

        ResponseEntity<VeiculoDTO> response =
                veiculosController.salvar(veiculoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ABC1D23", response.getBody().placa());
        assertEquals("Volkswagen", response.getBody().marca());
        assertEquals("Gol", response.getBody().modelo());
        assertEquals(2020, response.getBody().ano());
        assertEquals("12345678901", response.getBody().clienteDocumento());

        verify(veiculoService, times(1)).save(veiculoDTO);
    }

    @Test
    void deveEditarVeiculoComSucesso() {
        String placaAtual = "ABC1D23";

        VeiculoDTO veiculoAtualizado = new VeiculoDTO(
                "DEF2G34",
                "Toyota",
                "Corolla",
                2022,
                "12345678901"
        );

        when(veiculoService.edit(placaAtual, veiculoAtualizado))
                .thenReturn(veiculoAtualizado);

        ResponseEntity<VeiculoDTO> response =
                veiculosController.editar(placaAtual, veiculoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("DEF2G34", response.getBody().placa());
        assertEquals("Toyota", response.getBody().marca());
        assertEquals("Corolla", response.getBody().modelo());
        assertEquals(2022, response.getBody().ano());
        assertEquals("12345678901", response.getBody().clienteDocumento());

        verify(veiculoService, times(1)).edit(placaAtual, veiculoAtualizado);
    }

    @Test
    void deveDeletarVeiculoPorPlacaComSucesso() {
        String placa = "ABC1D23";

        doNothing().when(veiculoService).delete(placa);

        ResponseEntity<Void> response =
                veiculosController.deletar(placa);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(veiculoService, times(1)).delete(placa);
    }

    @Test
    void deveListarVeiculosComSucesso() {
        VeiculoDTO veiculoDTO2 = new VeiculoDTO(
                "DEF2G34",
                "Toyota",
                "Corolla",
                2022,
                "98765432100"
        );

        when(veiculoService.findAll())
                .thenReturn(List.of(veiculoDTO, veiculoDTO2));

        ResponseEntity<List<VeiculoDTO>> response =
                veiculosController.listarVeiculos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals("ABC1D23", response.getBody().get(0).placa());
        assertEquals("Volkswagen", response.getBody().get(0).marca());
        assertEquals("Gol", response.getBody().get(0).modelo());
        assertEquals(2020, response.getBody().get(0).ano());
        assertEquals("12345678901", response.getBody().get(0).clienteDocumento());

        assertEquals("DEF2G34", response.getBody().get(1).placa());
        assertEquals("Toyota", response.getBody().get(1).marca());
        assertEquals("Corolla", response.getBody().get(1).modelo());
        assertEquals(2022, response.getBody().get(1).ano());
        assertEquals("98765432100", response.getBody().get(1).clienteDocumento());

        verify(veiculoService, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremVeiculos() {
        when(veiculoService.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<VeiculoDTO>> response =
                veiculosController.listarVeiculos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(veiculoService, times(1)).findAll();
    }
}

