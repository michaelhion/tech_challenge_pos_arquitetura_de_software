package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.services.OrdemServicoClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoClienteControllerTest {

    @Mock
    private OrdemServicoClienteService ordemServicoClienteService;

    @Mock
    private OrdemServicoClienteController ordemServicoClienteController;

    private UUID ordemServicoId;


    @BeforeEach
    void setUp() {
        ordemServicoClienteController = new OrdemServicoClienteController(ordemServicoClienteService);

        ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");
    }


    @Test
    void deveAprovarOrdemServicoComSucesso() {
        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(true, null);

        OrdemServicoDTO ordemServicoAprovada = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear e vibração no volante.",
                OrdemDeServicoStatus.ORCAMENTO_APROVADO,
                null
        );

        when(ordemServicoClienteService.aprovar(ordemServicoId, aprovacaoDTO))
                .thenReturn(ordemServicoAprovada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoClienteController.aprovar(ordemServicoId, aprovacaoDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_APROVADO, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoClienteService, times(1))
                .aprovar(ordemServicoId, aprovacaoDTO);
    }

    @Test
    void deveReprovarOrdemServicoComSucesso() {
        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(false, "muito caro");

        OrdemServicoDTO ordemServicoReprovada = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear e vibração no volante.",
                OrdemDeServicoStatus.ORCAMENTO_REPROVADO,
                null
        );

        when(ordemServicoClienteService.aprovar(ordemServicoId, aprovacaoDTO))
                .thenReturn(ordemServicoReprovada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoClienteController.aprovar(ordemServicoId, aprovacaoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_REPROVADO, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoClienteService, times(1))
                .aprovar(ordemServicoId, aprovacaoDTO);
    }

    @Test
    void deveBuscarOrdemServicoPorIdComSucesso() {

        OrdemServicoDTO ordemServicoDTO =
                new OrdemServicoDTO(
                        ordemServicoId,
                        "12345678901",
                        "ABC1D23",
                        "Problema no freio",
                        OrdemDeServicoStatus.RECEBIDA,
                        null
                );

        when(ordemServicoClienteService.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServicoDTO));

        ResponseEntity<OrdemServicoDTO> response = ordemServicoClienteController
                        .buscarPorId(ordemServicoId);

        assertEquals(HttpStatus.OK,response.getStatusCode());

        assertNotNull(response.getBody());

        assertEquals(ordemServicoId,response.getBody().id());

        verify(ordemServicoClienteService)
                .findById(ordemServicoId);
    }

    @Test
    void deveRetornar404QuandoOrdemServicoNaoExistir() {

        when(ordemServicoClienteService.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        ResponseEntity<OrdemServicoDTO> response = ordemServicoClienteController
                        .buscarPorId(ordemServicoId);

        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

        verify(ordemServicoClienteService).findById(ordemServicoId);
    }
}