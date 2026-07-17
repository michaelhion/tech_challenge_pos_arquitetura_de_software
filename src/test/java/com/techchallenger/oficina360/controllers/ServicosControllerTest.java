package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.ServicosController;
import com.techchallenger.oficina360.usecases.servicos.AtualizarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.BuscarServicoPorCodigoUseCase;
import com.techchallenger.oficina360.usecases.servicos.CadastrarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ExcluirServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ListarServicosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicosControllerTest {

    private static final String ALINHAMENTO_E_BALANCEAMENTO = "ALINHAMENTO-E-BALANCEAMENTO";
    @Mock
    private CadastrarServicoUseCase cadastrarServicoUseCase;

    @Mock
    private BuscarServicoPorCodigoUseCase buscarServicoPorCodigoUseCase;

    @Mock
    private ListarServicosUseCase listarServicosUseCase;

    @Mock
    private AtualizarServicoUseCase atualizarServicoUseCase;

    @Mock
    private ExcluirServicoUseCase excluirServicoUseCase;

    @InjectMocks
    private ServicosController servicosController;

    private ServicoDTO servicoDTO;

    @BeforeEach
    void setUp() {
        servicoDTO = new ServicoDTO(
                "TROCA-DE-OLEO",
                "Troca de óleo",
                BigDecimal.valueOf(150.00),
                1
        );
    }

    @Test
    void deveBuscarServicoPorIdComSucesso() {
        when(buscarServicoPorCodigoUseCase.findByCodigo(servicoDTO.codigo()))
                .thenReturn(Optional.of(servicoDTO));

        ResponseEntity<ServicoDTO> response = servicosController.buscarPorId(servicoDTO.codigo());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Troca de óleo", response.getBody().descricao());
        assertEquals(BigDecimal.valueOf(150.00), response.getBody().valor());

        verify(buscarServicoPorCodigoUseCase, times(1)).findByCodigo(servicoDTO.codigo());
    }

    @Test
    void deveRetornarNotFoundQuandoBuscarServicoPorIdInexistente() {
        when(buscarServicoPorCodigoUseCase.findByCodigo(servicoDTO.codigo()))
                .thenReturn(Optional.empty());

        ResponseEntity<ServicoDTO> response = servicosController.buscarPorId(servicoDTO.codigo());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(buscarServicoPorCodigoUseCase, times(1)).findByCodigo(servicoDTO.codigo());
    }

    @Test
    void deveSalvarServicoComSucesso() {
        when(cadastrarServicoUseCase.save(servicoDTO))
                .thenReturn(servicoDTO);

        ResponseEntity<ServicoDTO> response = servicosController.salvar(servicoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Troca de óleo", response.getBody().descricao());
        assertEquals(BigDecimal.valueOf(150.00), response.getBody().valor());

        verify(cadastrarServicoUseCase, times(1)).save(servicoDTO);
    }

    @Test
    void deveEditarServicoComSucesso() {
        ServicoDTO servicoAtualizado = new ServicoDTO(
                ALINHAMENTO_E_BALANCEAMENTO,
                "Alinhamento e balanceamento",
                BigDecimal.valueOf(220.00),
                1
        );

        when(atualizarServicoUseCase.edit(ALINHAMENTO_E_BALANCEAMENTO, servicoAtualizado))
                .thenReturn(servicoAtualizado);

        ResponseEntity<ServicoDTO> response = servicosController.editar(ALINHAMENTO_E_BALANCEAMENTO, servicoAtualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Alinhamento e balanceamento", response.getBody().descricao());
        assertEquals(BigDecimal.valueOf(220.00), response.getBody().valor());

        verify(atualizarServicoUseCase, times(1)).edit(ALINHAMENTO_E_BALANCEAMENTO, servicoAtualizado);
    }

    @Test
    void deveDeletarServicoComSucesso() {
        doNothing().when(excluirServicoUseCase)
                .delete(ALINHAMENTO_E_BALANCEAMENTO);

        ResponseEntity<Void> response = servicosController.deletar(ALINHAMENTO_E_BALANCEAMENTO);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());

        verify(excluirServicoUseCase, times(1)).delete(ALINHAMENTO_E_BALANCEAMENTO);
    }

    @Test
    void deveListarServicosComSucesso() {
        ServicoDTO segundoServico = new ServicoDTO(
                "TROCA-DE-PASTILHA-DE-FREIO",
                "Troca de pastilha de freio",
                BigDecimal.valueOf(300.00),
                3
        );

        when(listarServicosUseCase.findAll())
                .thenReturn(List.of(servicoDTO, segundoServico));

        ResponseEntity<List<ServicoDTO>> response = servicosController.listarServicos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals("Troca de óleo", response.getBody().get(0).descricao());
        assertEquals(BigDecimal.valueOf(150.00), response.getBody().get(0).valor());

        assertEquals("Troca de pastilha de freio", response.getBody().get(1).descricao());
        assertEquals(BigDecimal.valueOf(300.00), response.getBody().get(1).valor());

        verify(listarServicosUseCase, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremServicos() {
        when(listarServicosUseCase.findAll())
                .thenReturn(List.of());

        ResponseEntity<List<ServicoDTO>> response = servicosController.listarServicos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(listarServicosUseCase, times(1)).findAll();
    }
}