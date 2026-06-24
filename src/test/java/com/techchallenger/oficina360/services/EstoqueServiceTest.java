package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.exceptions.ConflitoException;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.repositories.EstoqueRepository;
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
class EstoqueServiceTest {

    @Mock
    private EstoqueRepository estoqueRepository;

    @InjectMocks
    private EstoqueService estoqueService;

    private UUID estoqueId;
    private Estoque estoque;
    private EstoqueDTO estoqueDTO;

    @BeforeEach
    void setUp() {
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

        estoque = Estoque.builder()
                .id(estoqueId)
                .nome("Filtro de óleo")
                .valor(BigDecimal.valueOf(45.90))
                .quantidade(20)
                .reservados(5)
                .build();
    }

    @Test
    void deveListarTodosOsItensDeEstoque() {
        when(estoqueRepository.findAll())
                .thenReturn(List.of(estoque));

        List<EstoqueDTO> resultado = estoqueService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Filtro de óleo", resultado.get(0).nome());
        assertEquals(BigDecimal.valueOf(45.90), resultado.get(0).valor());
        assertEquals(20, resultado.get(0).quantidade());
        assertEquals(5, resultado.get(0).reservados());
        assertEquals(15, resultado.get(0).disponiveis());

        verify(estoqueRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremItensDeEstoque() {
        when(estoqueRepository.findAll())
                .thenReturn(List.of());

        List<EstoqueDTO> resultado = estoqueService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(estoqueRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarItemDeEstoquePorIdQuandoExistir() {
        when(estoqueRepository.findByCodigo(estoqueDTO.codigo()))
                .thenReturn(Optional.of(estoque));

        Optional<EstoqueDTO> resultado = estoqueService.findByCodigo(estoqueDTO.codigo());

        assertTrue(resultado.isPresent());
        assertEquals("Filtro de óleo", resultado.get().nome());
        assertEquals(BigDecimal.valueOf(45.90), resultado.get().valor());
        assertEquals(20, resultado.get().quantidade());
        assertEquals(5, resultado.get().reservados());
        assertEquals(15, resultado.get().disponiveis());

        verify(estoqueRepository, times(1)).findByCodigo(estoqueDTO.codigo());
    }

    @Test
    void deveRetornarOptionalVazioQuandoItemDeEstoqueNaoExistirPorId() {
        when(estoqueRepository.findByCodigo(estoqueDTO.codigo()))
                .thenReturn(Optional.empty());

        Optional<EstoqueDTO> resultado = estoqueService.findByCodigo(estoqueDTO.codigo());

        assertTrue(resultado.isEmpty());

        verify(estoqueRepository, times(1)).findByCodigo(estoqueDTO.codigo());
    }

    @Test
    void deveSalvarItemDeEstoqueComSucesso() {
        when(estoqueRepository.save(any(Estoque.class)))
                .thenReturn(estoque);

        EstoqueDTO resultado = estoqueService.save(estoqueDTO);

        assertNotNull(resultado);
        assertEquals("Filtro de óleo", resultado.nome());
        assertEquals(BigDecimal.valueOf(45.90), resultado.valor());
        assertEquals(20, resultado.quantidade());
        assertEquals(5, resultado.reservados());
        assertEquals(15, resultado.disponiveis());

        verify(estoqueRepository, times(1)).save(any(Estoque.class));
    }

    @Test
    void deveEditarItemDeEstoqueComSucesso() {
        EstoqueDTO dtoAtualizado = new EstoqueDTO(
                estoqueId,
                "FILTRO-DE-OLEO-PREMIUM",
                "Filtro de óleo premium",
                BigDecimal.valueOf(60.00),
                30,
                10,
                20
        );

        Estoque estoqueAtualizado = Estoque.builder()
                .id(estoqueId)
                .nome("Filtro de óleo premium")
                .valor(BigDecimal.valueOf(60.00))
                .quantidade(30)
                .reservados(10)
                .build();

        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.of(estoque));

        when(estoqueRepository.save(any(Estoque.class)))
                .thenReturn(estoqueAtualizado);

        EstoqueDTO resultado = estoqueService.edit(estoqueId, dtoAtualizado);

        assertNotNull(resultado);
        assertEquals("Filtro de óleo premium", resultado.nome());
        assertEquals(BigDecimal.valueOf(60.00), resultado.valor());
        assertEquals(30, resultado.quantidade());
        assertEquals(10, resultado.reservados());
        assertEquals(20, resultado.disponiveis());

        verify(estoqueRepository, times(1)).findById(estoqueId);
        verify(estoqueRepository, times(1)).save(any(Estoque.class));
    }

    @Test
    void naoDeveEditarItemDeEstoqueQuandoNaoEncontrado() {
        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> estoqueService.edit(estoqueId, estoqueDTO)
        );

        assertEquals("Item de estoque não disponível", exception.getMessage());

        verify(estoqueRepository, times(1)).findById(estoqueId);
        verify(estoqueRepository, never()).save(any(Estoque.class));
    }

    @Test
    void deveDeletarItemDeEstoqueComSucesso() {
        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.of(estoque));

        estoqueService.delete(estoqueId);

        verify(estoqueRepository, times(1)).deleteById(estoqueId);
    }

    @Test
    void naoDeveDeletarItemDeEstoqueQuandoNaoEncontrado() {
        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> estoqueService.delete(estoqueId)
        );

        assertEquals("Item de estoque não disponível", exception.getMessage());

        verify(estoqueRepository, times(1)).findById(estoqueId);
        verify(estoqueRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void deveReservarItemDeEstoqueComSucesso() {
        ReservaEstoqueDTO reservaDTO = new ReservaEstoqueDTO(3);

        Estoque estoqueReservado = Estoque.builder()
                .id(estoqueId)
                .nome("Filtro de óleo")
                .valor(BigDecimal.valueOf(45.90))
                .quantidade(20)
                .reservados(8)
                .build();

        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.of(estoque));

        when(estoqueRepository.save(any(Estoque.class)))
                .thenReturn(estoqueReservado);

        EstoqueDTO resultado = estoqueService.reservar(estoqueId, reservaDTO);

        assertNotNull(resultado);
        assertEquals("Filtro de óleo", resultado.nome());
        assertEquals(BigDecimal.valueOf(45.90), resultado.valor());
        assertEquals(20, resultado.quantidade());
        assertEquals(8, resultado.reservados());
        assertEquals(12, resultado.disponiveis());

        verify(estoqueRepository, times(1)).findById(estoqueId);
        verify(estoqueRepository, times(1)).save(any(Estoque.class));
    }

    @Test
    void naoDeveReservarItemDeEstoqueQuandoNaoEncontrado() {
        ReservaEstoqueDTO reservaDTO = new ReservaEstoqueDTO(3);

        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> estoqueService.reservar(estoqueId, reservaDTO)
        );

        assertEquals("Item de estoque não encontrado", exception.getMessage());

        verify(estoqueRepository, times(1)).findById(estoqueId);
        verify(estoqueRepository, never()).save(any(Estoque.class));
    }

    @Test
    void naoDeveReservarQuandoQuantidadeForMaiorQueDisponivel() {
        ReservaEstoqueDTO reservaDTO = new ReservaEstoqueDTO(30);

        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.of(estoque));

        ConflitoException exception = assertThrows(
                ConflitoException.class,
                () -> estoqueService.reservar(estoqueId, reservaDTO)
        );

        assertEquals("Quantidade indisponível em estoque", exception.getMessage());

        verify(estoqueRepository, times(1)).findById(estoqueId);
        verify(estoqueRepository, never()).save(any(Estoque.class));
    }

    @Test
    void naoDeveReservarQuandoQuantidadeForZero() {
        ReservaEstoqueDTO reservaDTO = new ReservaEstoqueDTO(0);

        when(estoqueRepository.findById(estoqueId))
                .thenReturn(Optional.of(estoque));

        ConflitoException exception = assertThrows(
                ConflitoException.class,
                () -> estoqueService.reservar(estoqueId, reservaDTO)
        );

        assertEquals("Quantidade a reservar deve ser maior que zero", exception.getMessage());

        verify(estoqueRepository, times(1)).findById(estoqueId);
        verify(estoqueRepository, never()).save(any(Estoque.class));
    }
}

