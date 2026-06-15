package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.services.OrdemServicoService;
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
class OrdemServicoControllerTest {

    @Mock
    private OrdemServicoService ordemServicoService;

    private OrdemServicoController ordemServicoController;

    private UUID ordemServicoId;

    private OrdemServicoDTO ordemServicoDTO;

    @BeforeEach
    void setUp() {
        ordemServicoController = new OrdemServicoController(ordemServicoService);

        ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

        ordemServicoDTO = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear e vibração no volante.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );
    }

    @Test
    void deveListarOrdensServicoComSucesso() {
        OrdemServicoDTO segundaOrdem = new OrdemServicoDTO(
                UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe"),
                "98765432100",
                "DEF2G34",
                "Veículo apresenta falha na partida.",
                OrdemDeServicoStatus.EM_DIAGNOSTICO,
                null
        );

        when(ordemServicoService.findAll())
                .thenReturn(List.of(ordemServicoDTO, segundaOrdem));

        ResponseEntity<List<OrdemServicoDTO>> response =
                ordemServicoController.listarOrdensServico();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals(ordemServicoId, response.getBody().get(0).id());
        assertEquals("12345678901", response.getBody().get(0).documentoCliente());
        assertEquals("ABC1D23", response.getBody().get(0).placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear e vibração no volante.", response.getBody().get(0).descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().get(0).ordemDeServicoStatus());

        assertEquals("98765432100", response.getBody().get(1).documentoCliente());
        assertEquals("DEF2G34", response.getBody().get(1).placaVeiculo());
        assertEquals("Veículo apresenta falha na partida.", response.getBody().get(1).descricaoProblema());
        assertEquals(OrdemDeServicoStatus.EM_DIAGNOSTICO, response.getBody().get(1).ordemDeServicoStatus());

        verify(ordemServicoService, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremOrdensServico() {
        when(ordemServicoService.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<OrdemServicoDTO>> response =
                ordemServicoController.listarOrdensServico();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(ordemServicoService, times(1)).findAll();
    }

    @Test
    void deveBuscarOrdemServicoPorIdComSucesso() {
        when(ordemServicoService.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServicoDTO));

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoController.buscarPorId(ordemServicoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear e vibração no volante.", response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoService, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveRetornarNotFoundQuandoOrdemServicoNaoForEncontradaPorId() {
        when(ordemServicoService.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoController.buscarPorId(ordemServicoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(ordemServicoService, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveSalvarOrdemServicoComSucesso() {
        OrdemServicoDTO request = new OrdemServicoDTO(
                null,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear e vibração no volante.",
                null,
                null
        );

        when(ordemServicoService.save(request))
                .thenReturn(ordemServicoDTO);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoController.salvar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear e vibração no volante.", response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoService, times(1)).save(request);
    }

    @Test
    void deveEditarOrdemServicoComSucesso() {
        OrdemServicoDTO ordemServicoAtualizada = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear após troca recente de pastilhas.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );

        when(ordemServicoService.edit(ordemServicoId, ordemServicoAtualizada))
                .thenReturn(ordemServicoAtualizada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoController.editar(ordemServicoId, ordemServicoAtualizada);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear após troca recente de pastilhas.", response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoService, times(1))
                .edit(ordemServicoId, ordemServicoAtualizada);
    }

    @Test
    void deveDeletarOrdemServicoComSucesso() {
        doNothing().when(ordemServicoService)
                .delete(ordemServicoId);

        ResponseEntity<Void> response =
                ordemServicoController.deletar(ordemServicoId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(ordemServicoService, times(1)).delete(ordemServicoId);
    }

    @Test
    void deveAprovarOrdemServicoComSucesso() {
        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(true);

        OrdemServicoDTO ordemServicoAprovada = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear e vibração no volante.",
                OrdemDeServicoStatus.ORCAMENTO_APROVADO,
                null
        );

        when(ordemServicoService.aprovar(ordemServicoId, aprovacaoDTO))
                .thenReturn(ordemServicoAprovada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoController.aprovar(ordemServicoId, aprovacaoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_APROVADO, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoService, times(1))
                .aprovar(ordemServicoId, aprovacaoDTO);
    }

    @Test
    void deveReprovarOrdemServicoComSucesso() {
        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(false);

        OrdemServicoDTO ordemServicoReprovada = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear e vibração no volante.",
                OrdemDeServicoStatus.ORCAMENTO_REPROVADO,
                null
        );

        when(ordemServicoService.aprovar(ordemServicoId, aprovacaoDTO))
                .thenReturn(ordemServicoReprovada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoController.aprovar(ordemServicoId, aprovacaoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_REPROVADO, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoService, times(1))
                .aprovar(ordemServicoId, aprovacaoDTO);
    }

    @Test
    void deveDiagnosticarOrdemServicoComSucesso() {
        DiagnosticoDTO diagnosticoDTO = new DiagnosticoDTO(
                List.of("SRV-TROCA-OLEO", "SRV-ALINHAMENTO"),
                List.of(
                        new DiagnosticoEstoqueDTO("EST-FILTRO-OLEO", 1),
                        new DiagnosticoEstoqueDTO("EST-OLEO-5W30", 4)
                )
        );

        OrdemServicoDTO ordemServicoDiagnosticada = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear e vibração no volante.",
                OrdemDeServicoStatus.AGUARDANDO_APROVACAO,
                null
        );

        when(ordemServicoService.diagnosticar(ordemServicoId, diagnosticoDTO))
                .thenReturn(ordemServicoDiagnosticada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoController.diagnosticar(ordemServicoId, diagnosticoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals("12345678901", response.getBody().documentoCliente());
        assertEquals("ABC1D23", response.getBody().placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear e vibração no volante.", response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, response.getBody().ordemDeServicoStatus());

        verify(ordemServicoService, times(1))
                .diagnosticar(ordemServicoId, diagnosticoDTO);
    }
}