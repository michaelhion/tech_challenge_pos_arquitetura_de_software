package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.web.controllers.OrdemServicoOficinaController;
import com.techchallenger.oficina360.usecases.ordemservico.AbrirOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DeletarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DiagnosticarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.EditarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.FinalizarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.IniciarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.ListarOrdensServicoUseCase;
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
class OrdemServicoOficinaControllerTest {

    private static final String CPF = "12345678901";
    private static final String PLACA = "ABC1D23";
    private static final String RECLAMACAO_CLIENTE = "Veículo apresenta ruído ao frear e vibração no volante.";

    @Mock
    private AbrirOrdemServicoUseCase abrirOrdemServicoUseCase;

    @Mock
    private DiagnosticarOrdemServicoUseCase diagnosticarOrdemServicoUseCase;

    @Mock
    private IniciarExecucaoUseCase iniciarExecucaoUseCase;

    @Mock
    private FinalizarExecucaoUseCase finalizarExecucaoUseCase;

    @Mock
    private ListarOrdensServicoUseCase listarOrdensServicoUseCase;

    @Mock
    private BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;

    @Mock
    private EditarOrdemServicoUseCase editarOrdemServicoUseCase;

    @Mock
    private DeletarOrdemServicoUseCase deletarOrdemServicoUseCase;

    private OrdemServicoOficinaController ordemServicoOficinaController;

    private UUID ordemServicoId;

    private OrdemServicoDTO ordemServicoDTO;
    private CriarOrdemServicoDTO criarOrdemServicoDTO;

    @BeforeEach
    void setUp() {

        ordemServicoOficinaController =
                new OrdemServicoOficinaController(
                        abrirOrdemServicoUseCase,
                        diagnosticarOrdemServicoUseCase,
                        iniciarExecucaoUseCase,
                        finalizarExecucaoUseCase,
                        listarOrdensServicoUseCase,
                        buscarOrdemServicoPorIdUseCase,
                        editarOrdemServicoUseCase,
                        deletarOrdemServicoUseCase
                );

        ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

        ordemServicoDTO = new OrdemServicoDTO(
                ordemServicoId,
                CPF,
                PLACA,
                RECLAMACAO_CLIENTE,
                OrdemDeServicoStatus.RECEBIDA,
                null
        );
        criarOrdemServicoDTO = new CriarOrdemServicoDTO(
                ordemServicoId,
                CPF,
                PLACA,
                RECLAMACAO_CLIENTE,
                OrdemDeServicoStatus.RECEBIDA
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

        when(listarOrdensServicoUseCase.findAll())
                .thenReturn(List.of(ordemServicoDTO, segundaOrdem));

        ResponseEntity<List<OrdemServicoDTO>> response =
                ordemServicoOficinaController.listarOrdensServico();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals(ordemServicoId, response.getBody().get(0).id());
        assertEquals(CPF, response.getBody().get(0).documentoCliente());
        assertEquals(PLACA, response.getBody().get(0).placaVeiculo());
        assertEquals(RECLAMACAO_CLIENTE, response.getBody().get(0).descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().get(0).ordemDeServicoStatus());

        assertEquals("98765432100", response.getBody().get(1).documentoCliente());
        assertEquals("DEF2G34", response.getBody().get(1).placaVeiculo());
        assertEquals("Veículo apresenta falha na partida.", response.getBody().get(1).descricaoProblema());
        assertEquals(OrdemDeServicoStatus.EM_DIAGNOSTICO, response.getBody().get(1).ordemDeServicoStatus());

        verify(listarOrdensServicoUseCase, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremOrdensServico() {
        when(listarOrdensServicoUseCase.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<OrdemServicoDTO>> response =
                ordemServicoOficinaController.listarOrdensServico();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(listarOrdensServicoUseCase, times(1)).findAll();
    }

    @Test
    void deveBuscarOrdemServicoPorIdComSucesso() {
        when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServicoDTO));

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoOficinaController.buscarPorId(ordemServicoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals(CPF, response.getBody().documentoCliente());
        assertEquals(PLACA, response.getBody().placaVeiculo());
        assertEquals(RECLAMACAO_CLIENTE, response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

        verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveRetornarNotFoundQuandoOrdemServicoNaoForEncontradaPorId() {
        when(buscarOrdemServicoPorIdUseCase.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoOficinaController.buscarPorId(ordemServicoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(buscarOrdemServicoPorIdUseCase, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveSalvarOrdemServicoComSucesso() {
        CriarOrdemServicoDTO request = new CriarOrdemServicoDTO(
                null,
                CPF,
                PLACA,
                RECLAMACAO_CLIENTE,
                null
        );

        when(abrirOrdemServicoUseCase.abrirOrdemServico(request))
                .thenReturn(criarOrdemServicoDTO);

        ResponseEntity<CriarOrdemServicoDTO> response =
                ordemServicoOficinaController.salvar(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals(CPF, response.getBody().documentoCliente());
        assertEquals(PLACA, response.getBody().placaVeiculo());
        assertEquals(RECLAMACAO_CLIENTE, response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

        verify(abrirOrdemServicoUseCase, times(1)).abrirOrdemServico(request);
    }

    @Test
    void deveEditarOrdemServicoComSucesso() {
        OrdemServicoDTO ordemServicoAtualizada = new OrdemServicoDTO(
                ordemServicoId,
                CPF,
                PLACA,
                "Veículo apresenta ruído ao frear após troca recente de pastilhas.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );

        when(editarOrdemServicoUseCase.edit(ordemServicoId, ordemServicoAtualizada))
                .thenReturn(ordemServicoAtualizada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoOficinaController.editar(ordemServicoId, ordemServicoAtualizada);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals(CPF, response.getBody().documentoCliente());
        assertEquals(PLACA, response.getBody().placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear após troca recente de pastilhas.", response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, response.getBody().ordemDeServicoStatus());

        verify(editarOrdemServicoUseCase, times(1))
                .edit(ordemServicoId, ordemServicoAtualizada);
    }

    @Test
    void deveDeletarOrdemServicoComSucesso() {
        doNothing().when(deletarOrdemServicoUseCase)
                .deleteById(ordemServicoId);

        ResponseEntity<Void> response =
                ordemServicoOficinaController.deletar(ordemServicoId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(deletarOrdemServicoUseCase, times(1)).deleteById(ordemServicoId);
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
                CPF,
                PLACA,
                RECLAMACAO_CLIENTE,
                OrdemDeServicoStatus.AGUARDANDO_APROVACAO,
                null
        );

        when(diagnosticarOrdemServicoUseCase.diagnosticar(ordemServicoId, diagnosticoDTO))
                .thenReturn(ordemServicoDiagnosticada);

        ResponseEntity<OrdemServicoDTO> response =
                ordemServicoOficinaController.diagnosticar(ordemServicoId, diagnosticoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ordemServicoId, response.getBody().id());
        assertEquals(CPF, response.getBody().documentoCliente());
        assertEquals(PLACA, response.getBody().placaVeiculo());
        assertEquals(RECLAMACAO_CLIENTE, response.getBody().descricaoProblema());
        assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, response.getBody().ordemDeServicoStatus());

        verify(diagnosticarOrdemServicoUseCase, times(1))
                .diagnosticar(ordemServicoId, diagnosticoDTO);
    }

    @Test
    void deveIniciarExecucaoComSucesso() {


        when(iniciarExecucaoUseCase.iniciarExecucao(ordemServicoId))
                .thenReturn(ordemServicoDTO);

        ResponseEntity<OrdemServicoDTO> response = ordemServicoOficinaController
                        .iniciarExecucao(ordemServicoId);

        assertNotNull(response);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        assertFalse(response.hasBody());

        assertNull(response.getBody());

        verify(iniciarExecucaoUseCase, times(1)).iniciarExecucao(ordemServicoId);

        verifyNoMoreInteractions(iniciarExecucaoUseCase);
    }

    @Test
    void deveFinalizarExecucaoComSucesso() {



        ResponseEntity<OrdemServicoDTO> response = ordemServicoOficinaController
                        .finalizarExecucao(ordemServicoId);

        assertNotNull(response);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

        assertFalse(response.hasBody());

        assertNull(response.getBody());

        verify(finalizarExecucaoUseCase, times(1)).finalizarExecucao(ordemServicoId);

        verifyNoMoreInteractions(finalizarExecucaoUseCase);
    }
}