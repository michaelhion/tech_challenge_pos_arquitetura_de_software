package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ServicoRepository;
import com.techchallenger.oficina360.frameworks.persistence.repositories.TempoExecucaoServicoRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicoServiceTest {

    private static final String TROCA_DE_OLEO = "TROCA-DE-OLEO";
    private static final String ALINHAMENTO_E_BALANCEAMENTO = "ALINHAMENTO-E-BALANCEAMENTO";
    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private TempoExecucaoServicoRepository tempoExecucaoServicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private UUID servicoId;
    private ServicoEntity servicoEntity;
    private ServicoDTO servicoDTO;

    @BeforeEach
    void setUp() {
        servicoId = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

        servicoDTO = new ServicoDTO(
                TROCA_DE_OLEO,
                "Troca de óleo",
                BigDecimal.valueOf(150.00),
                1
        );

        servicoEntity = ServicoEntity.builder()
                .id(servicoId)
                .descricao("Troca de óleo")
                .valor(BigDecimal.valueOf(150.00))
                .tempoMedioExecucaoMinutos(2)
                .build();
    }

    @Test
    void deveListarTodosOsServicos() {
        when(servicoRepository.findAll())
                .thenReturn(List.of(servicoEntity));

        List<ServicoDTO> resultado = servicoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Troca de óleo", resultado.get(0).descricao());
        assertEquals(BigDecimal.valueOf(150.00), resultado.get(0).valor());

        verify(servicoRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremServicos() {
        when(servicoRepository.findAll())
                .thenReturn(List.of());

        List<ServicoDTO> resultado = servicoService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(servicoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarServicoPorIdQuandoExistir() {
        when(servicoRepository.findByCodigo(servicoDTO.codigo()))
                .thenReturn(Optional.of(servicoEntity));

        Optional<ServicoDTO> resultado = servicoService.findByCodigo(servicoDTO.codigo());

        assertTrue(resultado.isPresent());
        assertEquals("Troca de óleo", resultado.get().descricao());
        assertEquals(BigDecimal.valueOf(150.00), resultado.get().valor());

        verify(servicoRepository, times(1)).findByCodigo(servicoDTO.codigo());
    }

    @Test
    void deveRetornarOptionalVazioQuandoServicoNaoExistirPorId() {
        when(servicoRepository.findByCodigo(servicoDTO.codigo()))
                .thenReturn(Optional.empty());

        Optional<ServicoDTO> resultado = servicoService.findByCodigo(servicoDTO.codigo());

        assertTrue(resultado.isEmpty());

        verify(servicoRepository, times(1)).findByCodigo(servicoDTO.codigo());
    }

    @Test
    void deveSalvarServicoComSucesso() {
        when(servicoRepository.save(any(ServicoEntity.class)))
                .thenReturn(servicoEntity);

        ServicoDTO resultado = servicoService.save(servicoDTO);

        assertNotNull(resultado);
        assertEquals("Troca de óleo", resultado.descricao());
        assertEquals(BigDecimal.valueOf(150.00), resultado.valor());

        verify(servicoRepository, times(1)).save(any(ServicoEntity.class));
    }

    @Test
    void deveEditarServicoComSucesso() {
        ServicoDTO dtoAtualizado = new ServicoDTO(
                ALINHAMENTO_E_BALANCEAMENTO,
                "Alinhamento e balanceamento",
                BigDecimal.valueOf(220.00),
                1
        );

        ServicoEntity servicoEntityAtualizado = ServicoEntity.builder()
                .id(servicoId)
                .descricao("Alinhamento e balanceamento")
                .valor(BigDecimal.valueOf(220.00))
                .build();

        when(servicoRepository.findByCodigo(ALINHAMENTO_E_BALANCEAMENTO))
                .thenReturn(Optional.of(servicoEntity));

        when(servicoRepository.save(any(ServicoEntity.class)))
                .thenReturn(servicoEntityAtualizado);

        ServicoDTO resultado = servicoService.edit(ALINHAMENTO_E_BALANCEAMENTO, dtoAtualizado);

        assertNotNull(resultado);
        assertEquals("Alinhamento e balanceamento", resultado.descricao());
        assertEquals(BigDecimal.valueOf(220.00), resultado.valor());

        verify(servicoRepository, times(1)).findByCodigo(ALINHAMENTO_E_BALANCEAMENTO);
        verify(servicoRepository, times(1)).save(any(ServicoEntity.class));
    }

    @Test
    void naoDeveEditarServicoQuandoNaoEncontrado() {
        when(servicoRepository.findByCodigo(ALINHAMENTO_E_BALANCEAMENTO))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> servicoService.edit(ALINHAMENTO_E_BALANCEAMENTO, servicoDTO)
        );

        assertEquals("Serviço não encontrado", exception.getMessage());

        verify(servicoRepository, times(1)).findByCodigo(ALINHAMENTO_E_BALANCEAMENTO);
        verify(servicoRepository, never()).save(any(ServicoEntity.class));
    }

    @Test
    void deveDeletarServicoComSucesso() {
        when(servicoRepository.findByCodigo(ALINHAMENTO_E_BALANCEAMENTO))
                .thenReturn(Optional.of(servicoEntity));

        doNothing().when(servicoRepository)
                .deleteByCodigo(ALINHAMENTO_E_BALANCEAMENTO);

        servicoService.delete(ALINHAMENTO_E_BALANCEAMENTO);

        verify(servicoRepository, times(1)).findByCodigo(ALINHAMENTO_E_BALANCEAMENTO);
        verify(servicoRepository, times(1)).deleteByCodigo(ALINHAMENTO_E_BALANCEAMENTO);
    }

    @Test
    void naoDeveDeletarServicoQuandoNaoEncontrado() {
        when(servicoRepository.findByCodigo(ALINHAMENTO_E_BALANCEAMENTO))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> servicoService.delete(ALINHAMENTO_E_BALANCEAMENTO)
        );

        assertEquals("Serviço não encontrado", exception.getMessage());

        verify(servicoRepository, times(1)).findByCodigo(ALINHAMENTO_E_BALANCEAMENTO);
        verify(servicoRepository, never()).delete(any(ServicoEntity.class));
    }

    @Test
    void deveRetornarTempoMedioCalculadoAoListarServicos() {

        when(servicoRepository.findAll())
                .thenReturn(List.of(servicoEntity));

        when(tempoExecucaoServicoRepository
                .calcularTempoMedio(servicoId))
                .thenReturn(35.6);

        List<ServicoDTO> resultado =
                servicoService.findAll();

        assertEquals(1, resultado.size());

        assertEquals(
                36,
                resultado.get(0)
                        .tempoDeExecucaoMedio()
        );

        verify(tempoExecucaoServicoRepository)
                .calcularTempoMedio(servicoId);
    }

    @Test
    void deveRetornarTempoMedioZeroQuandoNaoExistiremExecucoes() {

        when(servicoRepository.findAll())
                .thenReturn(List.of(servicoEntity));

        when(tempoExecucaoServicoRepository
                .calcularTempoMedio(servicoId))
                .thenReturn(null);

        List<ServicoDTO> resultado =
                servicoService.findAll();

        assertEquals(1, resultado.size());

        assertEquals(
                0,
                resultado.get(0)
                        .tempoDeExecucaoMedio()
        );

        verify(tempoExecucaoServicoRepository)
                .calcularTempoMedio(servicoId);
    }

    @Test
    void deveInicializarTempoMedioComZeroAoSalvar() {

        ArgumentCaptor<ServicoEntity> captor =
                ArgumentCaptor.forClass(ServicoEntity.class);

        when(servicoRepository.save(any()))
                .thenReturn(servicoEntity);

        servicoService.save(servicoDTO);

        verify(servicoRepository)
                .save(captor.capture());

        assertEquals(
                0,
                captor.getValue()
                        .getTempoMedioExecucaoMinutos()
        );
    }
}



