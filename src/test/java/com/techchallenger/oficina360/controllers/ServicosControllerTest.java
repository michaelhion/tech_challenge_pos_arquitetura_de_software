package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.services.ServicoService;
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
class ServicosControllerTest {

    @Mock
    private ServicoService servicoService;

    private ServicosController servicosController;

    private UUID servicoId;
    private ServicoDTO servicoDTO;

    @BeforeEach
    void setUp() {
        servicosController = new ServicosController(servicoService);

        servicoId = UUID.randomUUID();

        servicoDTO = new ServicoDTO(
                "TROCA-DE-OLEO",
                "Troca de óleo",
                BigDecimal.valueOf(150.00)
        );
    }

    @Test
    void deveBuscarServicoPorIdComSucesso() {
        when(servicoService.findById(servicoDTO.codigo()))
                .thenReturn(Optional.of(servicoDTO));

        ResponseEntity<ServicoDTO> response = servicosController.buscarPorId(servicoDTO.codigo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Troca de óleo", response.getBody().descricao());
        assertEquals(BigDecimal.valueOf(150.00), response.getBody().valor());

        verify(servicoService, times(1)).findById(servicoDTO.codigo());
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarServicoPorIdInexistente() {
        when(servicoService.findById(servicoDTO.codigo()))
                .thenReturn(Optional.empty());

        ResponseEntity<ServicoDTO> response = servicosController.buscarPorId(servicoDTO.codigo());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(servicoService, times(1)).findById(servicoDTO.codigo());
    }

    @Test
    void deveSalvarServicoComSucesso() {
        when(servicoService.save(servicoDTO))
                .thenReturn(servicoDTO);

        ResponseEntity<ServicoDTO> response = servicosController.salvar(servicoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Troca de óleo", response.getBody().descricao());
        assertEquals(BigDecimal.valueOf(150.00), response.getBody().valor());

        verify(servicoService, times(1)).save(servicoDTO);
    }

    @Test
    void deveEditarServicoComSucesso() {
        ServicoDTO servicoAtualizado = new ServicoDTO(
                "ALINHAMENTO-E-BALANCEAMENTO",
                "Alinhamento e balanceamento",
                BigDecimal.valueOf(220.00)
        );

        when(servicoService.edit(servicoId, servicoAtualizado))
                .thenReturn(servicoAtualizado);

        ResponseEntity<ServicoDTO> response = servicosController.editar(servicoId, servicoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alinhamento e balanceamento", response.getBody().descricao());
        assertEquals(BigDecimal.valueOf(220.00), response.getBody().valor());

        verify(servicoService, times(1)).edit(servicoId, servicoAtualizado);
    }

    @Test
    void deveDeletarServicoComSucesso() {
        doNothing().when(servicoService)
                .delete(servicoId);

        ResponseEntity<Void> response = servicosController.deletar(servicoId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(servicoService, times(1)).delete(servicoId);
    }

    @Test
    void deveListarServicosComSucesso() {
        ServicoDTO segundoServico = new ServicoDTO(
                "TROCA-DE-PASTILHA-DE-FREIO",
                "Troca de pastilha de freio",
                BigDecimal.valueOf(300.00)
        );

        when(servicoService.findAll())
                .thenReturn(List.of(servicoDTO, segundoServico));

        ResponseEntity<List<ServicoDTO>> response = servicosController.listarServicos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals("Troca de óleo", response.getBody().get(0).descricao());
        assertEquals(BigDecimal.valueOf(150.00), response.getBody().get(0).valor());

        assertEquals("Troca de pastilha de freio", response.getBody().get(1).descricao());
        assertEquals(BigDecimal.valueOf(300.00), response.getBody().get(1).valor());

        verify(servicoService, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremServicos() {
        when(servicoService.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<ServicoDTO>> response = servicosController.listarServicos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(servicoService, times(1)).findAll();
    }
}