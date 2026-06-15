package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.repositories.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private ServicoRepository servicoRepository;

    @InjectMocks
    private ServicoService servicoService;

    private UUID servicoId;
    private Servico servico;
    private ServicoDTO servicoDTO;

    @BeforeEach
    void setUp() {
        servicoId = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

        servicoDTO = new ServicoDTO(
                "TROCA-DE-OLEO",
                "Troca de óleo",
                BigDecimal.valueOf(150.00)
        );

        servico = Servico.builder()
                .id(servicoId)
                .descricao("Troca de óleo")
                .valor(BigDecimal.valueOf(150.00))
                .build();
    }

    @Test
    void deveListarTodosOsServicos() {
        when(servicoRepository.findAll())
                .thenReturn(List.of(servico));

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
                .thenReturn(Optional.of(servico));

        Optional<ServicoDTO> resultado = servicoService.findById(servicoDTO.codigo());

        assertTrue(resultado.isPresent());
        assertEquals("Troca de óleo", resultado.get().descricao());
        assertEquals(BigDecimal.valueOf(150.00), resultado.get().valor());

        verify(servicoRepository, times(1)).findByCodigo(servicoDTO.codigo());
    }

    @Test
    void deveRetornarOptionalVazioQuandoServicoNaoExistirPorId() {
        when(servicoRepository.findByCodigo(servicoDTO.codigo()))
                .thenReturn(Optional.empty());

        Optional<ServicoDTO> resultado = servicoService.findById(servicoDTO.codigo());

        assertTrue(resultado.isEmpty());

        verify(servicoRepository, times(1)).findByCodigo(servicoDTO.codigo());
    }

    @Test
    void deveSalvarServicoComSucesso() {
        when(servicoRepository.save(any(Servico.class)))
                .thenReturn(servico);

        ServicoDTO resultado = servicoService.save(servicoDTO);

        assertNotNull(resultado);
        assertEquals("Troca de óleo", resultado.descricao());
        assertEquals(BigDecimal.valueOf(150.00), resultado.valor());

        verify(servicoRepository, times(1)).save(any(Servico.class));
    }

    @Test
    void deveEditarServicoComSucesso() {
        ServicoDTO dtoAtualizado = new ServicoDTO(
                "ALINHAMENTO-E-BALANCEAMENTO",
                "Alinhamento e balanceamento",
                BigDecimal.valueOf(220.00)
        );

        Servico servicoAtualizado = Servico.builder()
                .id(servicoId)
                .descricao("Alinhamento e balanceamento")
                .valor(BigDecimal.valueOf(220.00))
                .build();

        when(servicoRepository.findById(servicoId))
                .thenReturn(Optional.of(servico));

        when(servicoRepository.save(any(Servico.class)))
                .thenReturn(servicoAtualizado);

        ServicoDTO resultado = servicoService.edit(servicoId, dtoAtualizado);

        assertNotNull(resultado);
        assertEquals("Alinhamento e balanceamento", resultado.descricao());
        assertEquals(BigDecimal.valueOf(220.00), resultado.valor());

        verify(servicoRepository, times(1)).findById(servicoId);
        verify(servicoRepository, times(1)).save(any(Servico.class));
    }

    @Test
    void naoDeveEditarServicoQuandoNaoEncontrado() {
        when(servicoRepository.findById(servicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> servicoService.edit(servicoId, servicoDTO)
        );

        assertEquals("Serviço não encontrado", exception.getMessage());

        verify(servicoRepository, times(1)).findById(servicoId);
        verify(servicoRepository, never()).save(any(Servico.class));
    }

    @Test
    void deveDeletarServicoComSucesso() {
        when(servicoRepository.findById(servicoId))
                .thenReturn(Optional.of(servico));

        doNothing().when(servicoRepository)
                .deleteById(servicoId);

        servicoService.delete(servicoId);

        verify(servicoRepository, times(1)).findById(servicoId);
        verify(servicoRepository, times(1)).deleteById(servicoId);
    }

    @Test
    void naoDeveDeletarServicoQuandoNaoEncontrado() {
        when(servicoRepository.findById(servicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> servicoService.delete(servicoId)
        );

        assertEquals("Serviço não encontrado", exception.getMessage());

        verify(servicoRepository, times(1)).findById(servicoId);
        verify(servicoRepository, never()).delete(any(Servico.class));
    }
}



