package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.services.EstoqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueControllerTest {

    @Mock
    private EstoqueService estoqueService;

    private EstoqueController estoqueController;

    private UUID estoqueId;

    private EstoqueDTO estoqueDTO;

    @BeforeEach
    void setUp() {
        estoqueController = new EstoqueController(estoqueService);

        estoqueId = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

        estoqueDTO = new EstoqueDTO(
                estoqueId,
                "FILTRO-DE-OLEO",
                "Filtro de óleo",
                BigDecimal.valueOf(45.90),
                20,
                5,
                15
        );
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(estoqueService.findByCodigo(estoqueDTO.codigo()))
                .thenReturn(Optional.of(estoqueDTO));

        ResponseEntity<EstoqueDTO> response = estoqueController.buscarPorId(estoqueDTO.codigo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Filtro de óleo", response.getBody().nome());
        assertEquals(BigDecimal.valueOf(45.90), response.getBody().valor());
        assertEquals(20, response.getBody().quantidade());
        assertEquals(5, response.getBody().reservados());
        assertEquals(15, response.getBody().disponiveis());

        verify(estoqueService, times(1)).findByCodigo(estoqueDTO.codigo());
    }

    @Test
    void deveRetornarNotFoundQuandoNaoEncontrarPorId() {
        when(estoqueService.findByCodigo(estoqueDTO.codigo()))
                .thenReturn(Optional.empty());

        ResponseEntity<EstoqueDTO> response = estoqueController.buscarPorId(estoqueDTO.codigo());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(estoqueService, times(1)).findByCodigo(estoqueDTO.codigo());
    }

    @Test
    void deveSalvarComSucesso() {
        when(estoqueService.save(estoqueDTO))
                .thenReturn(estoqueDTO);

        ResponseEntity<EstoqueDTO> response = estoqueController.salvar(estoqueDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Filtro de óleo", response.getBody().nome());
        assertEquals(BigDecimal.valueOf(45.90), response.getBody().valor());
        assertEquals(20, response.getBody().quantidade());
        assertEquals(5, response.getBody().reservados());
        assertEquals(15, response.getBody().disponiveis());

        verify(estoqueService, times(1)).save(estoqueDTO);
    }

    @Test
    void deveEditarComSucesso() {
        EstoqueDTO estoqueAtualizado = new EstoqueDTO(
                estoqueId,
                "FILTRO-DE-OLEO-PREMIUM",
                "Filtro de óleo premium",
                BigDecimal.valueOf(60.00),
                30,
                10,
                20
        );

        when(estoqueService.edit(estoqueId, estoqueAtualizado))
                .thenReturn(estoqueAtualizado);

        ResponseEntity<EstoqueDTO> response = estoqueController.editar(estoqueId, estoqueAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Filtro de óleo premium", response.getBody().nome());
        assertEquals(BigDecimal.valueOf(60.00), response.getBody().valor());
        assertEquals(30, response.getBody().quantidade());
        assertEquals(10, response.getBody().reservados());
        assertEquals(20, response.getBody().disponiveis());

        verify(estoqueService, times(1)).edit(estoqueId, estoqueAtualizado);
    }

    @Test
    void deveDeletarComSucesso() {
        doNothing().when(estoqueService).delete(estoqueId);

        ResponseEntity<Void> response = estoqueController.deletar(estoqueId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(estoqueService, times(1)).delete(estoqueId);
    }

    @Test
    void deveListarEstoquesComSucesso() {
        EstoqueDTO segundoItem = new EstoqueDTO(
                estoqueId,
                "PASTILHA-DE-FREIO",
                "Pastilha de freio",
                BigDecimal.valueOf(120.00),
                10,
                2,
                8
        );

        when(estoqueService.findAll())
                .thenReturn(List.of(estoqueDTO, segundoItem));

        ResponseEntity<List<EstoqueDTO>> response = estoqueController.listarEstoques();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals("Filtro de óleo", response.getBody().get(0).nome());
        assertEquals(BigDecimal.valueOf(45.90), response.getBody().get(0).valor());
        assertEquals(20, response.getBody().get(0).quantidade());
        assertEquals(5, response.getBody().get(0).reservados());
        assertEquals(15, response.getBody().get(0).disponiveis());

        assertEquals("Pastilha de freio", response.getBody().get(1).nome());
        assertEquals(BigDecimal.valueOf(120.00), response.getBody().get(1).valor());
        assertEquals(10, response.getBody().get(1).quantidade());
        assertEquals(2, response.getBody().get(1).reservados());
        assertEquals(8, response.getBody().get(1).disponiveis());

        verify(estoqueService, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremEstoques() {
        when(estoqueService.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<EstoqueDTO>> response = estoqueController.listarEstoques();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(estoqueService, times(1)).findAll();
    }

    @Test
    void deveReservarComSucesso() {
        ReservaEstoqueDTO reservaDTO = new ReservaEstoqueDTO(3);

        EstoqueDTO estoqueReservado = new EstoqueDTO(
                estoqueId,
                "FILTRO-DE-OLEO",
                "Filtro de óleo",
                BigDecimal.valueOf(45.90),
                20,
                8,
                12
        );

        when(estoqueService.reservar(estoqueId, reservaDTO))
                .thenReturn(estoqueReservado);

        ResponseEntity<EstoqueDTO> response = estoqueController.reservar(estoqueId, reservaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Filtro de óleo", response.getBody().nome());
        assertEquals(BigDecimal.valueOf(45.90), response.getBody().valor());
        assertEquals(20, response.getBody().quantidade());
        assertEquals(8, response.getBody().reservados());
        assertEquals(12, response.getBody().disponiveis());

        verify(estoqueService, times(1)).reservar(estoqueId, reservaDTO);
    }
}