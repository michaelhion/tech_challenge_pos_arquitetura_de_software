package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.entities.Cliente;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.exceptions.ConflitoException;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    private static final String PLACA_GOL = "ABC***23";
    private static final String VOLKSWAGEN = "Volkswagen";
    private static final String GOL = "Gol";
    private static final String DOCUMENTO_JOAO = "12345678901";
    private static final String CLIENTE_NAO_ENCONTRADO_PARA_O_DOCUMENTO_INFORMADO =
            "Cliente não encontrado para o documento informado";
    private static final String JA_EXISTE_VEICULO_CADASTRADO_COM_ESSA_PLACA =
            "Já existe veículo cadastrado com essa placa";
    public static final String PLACA = "ABC***23";

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VeiculoService veiculoService;

    private VeiculoDTO veiculoDTO;
    private Veiculo veiculo;
    private UUID veiculoId;

    @BeforeEach
    void setUp() {
        veiculoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        veiculoDTO = new VeiculoDTO(
                PLACA_GOL,
                VOLKSWAGEN,
                GOL,
                2020,
                DOCUMENTO_JOAO
        );

        veiculo = Veiculo.builder()
                .id(UUID.randomUUID())
                .placa(PLACA_GOL)
                .marca(VOLKSWAGEN)
                .modelo(GOL)
                .ano("2020")
                .clienteDocumento(DOCUMENTO_JOAO)
                .build();
    }

    @Test
    void deveListarTodosOsVeiculos() {
        when(veiculoRepository.findAll()).thenReturn(List.of(veiculo));

        List<VeiculoDTO> resultado = veiculoService.findAll();
        assertAll(
                ()->assertNotNull(resultado),
                ()->assertEquals(1, resultado.size()),
                ()->assertEquals(PLACA_GOL, resultado.get(0).placa()),
                ()->assertEquals(VOLKSWAGEN, resultado.get(0).marca()),
                ()->assertEquals(GOL, resultado.get(0).modelo()),
                ()->assertEquals(2020, resultado.get(0).ano()),
                ()->assertEquals(DOCUMENTO_JOAO, resultado.get(0).clienteDocumento())
        );


        verify(veiculoRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarVeiculoPorPlacaQuandoExistir() {
        when(veiculoRepository.findByPlaca(PLACA_GOL)).thenReturn(Optional.of(veiculo));

        Optional<VeiculoDTO> resultado = veiculoService.findByPlaca(PLACA_GOL);

        assertTrue(resultado.isPresent());
        assertEquals(PLACA_GOL, resultado.get().placa());
        assertEquals(VOLKSWAGEN, resultado.get().marca());

        verify(veiculoRepository, times(1)).findByPlaca(PLACA_GOL);
    }

    @Test
    void deveRetornarOptionalVazioQuandoVeiculoNaoExistirPorPlaca() {
        when(veiculoRepository.findByPlaca("ZZZ9Z99")).thenReturn(Optional.empty());

        Optional<VeiculoDTO> resultado = veiculoService.findByPlaca("ZZZ9Z99");

        assertTrue(resultado.isEmpty());

        verify(veiculoRepository, times(1)).findByPlaca("ZZZ9Z99");
    }

    @Test
    void deveSalvarVeiculoComSucesso() {
        when(clienteRepository.existsByDocumento(DOCUMENTO_JOAO)).thenReturn(Boolean.TRUE);
        when(veiculoRepository.existsByPlaca(PLACA_GOL)).thenReturn(Boolean.FALSE);
        when(veiculoRepository.save(any(Veiculo.class))).thenReturn(veiculo);

        VeiculoDTO resultado = veiculoService.save(veiculoDTO);

        assertAll(
                ()-> assertNotNull(resultado),
                ()-> assertEquals(PLACA_GOL, resultado.placa()),
                ()-> assertEquals(VOLKSWAGEN, resultado.marca()),
                ()-> assertEquals("Gol", resultado.modelo()),
                ()-> assertEquals(2020, resultado.ano()),
                ()-> assertEquals(DOCUMENTO_JOAO, resultado.clienteDocumento())
        );


        verify(clienteRepository, times(1)).existsByDocumento(DOCUMENTO_JOAO);
        verify(veiculoRepository, times(1)).existsByPlaca(PLACA_GOL);
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    void naoDeveSalvarVeiculoQuandoClienteNaoExistir() {
        when(clienteRepository.existsByDocumento(DOCUMENTO_JOAO)).thenReturn(Boolean.FALSE);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> veiculoService.save(veiculoDTO)
        );

        assertEquals(CLIENTE_NAO_ENCONTRADO_PARA_O_DOCUMENTO_INFORMADO, exception.getMessage());

        verify(clienteRepository, times(1)).existsByDocumento(DOCUMENTO_JOAO);
        verify(veiculoRepository, never()).findByPlaca(anyString());
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void naoDeveSalvarVeiculoQuandoPlacaJaExistir() {
        when(clienteRepository.existsByDocumento(DOCUMENTO_JOAO)).thenReturn(Boolean.TRUE);
        when(veiculoRepository.existsByPlaca(PLACA_GOL)).thenReturn(Boolean.TRUE);

        ConflitoException exception = assertThrows(
                ConflitoException.class,
                () -> veiculoService.save(veiculoDTO)
        );

        assertEquals(JA_EXISTE_VEICULO_CADASTRADO_COM_ESSA_PLACA, exception.getMessage());

        verify(clienteRepository, times(1)).existsByDocumento(DOCUMENTO_JOAO);
        verify(veiculoRepository, times(1)).existsByPlaca(PLACA_GOL);
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void deveEditarVeiculoComSucesso() {
        VeiculoDTO dtoAtualizado = new VeiculoDTO(
                PLACA,
                "Volkswagen",
                "Polo",
                2022,
                "12345678901"
        );

        Veiculo veiculoExistente = Veiculo.builder()
                .id(veiculoId)
                .placa(PLACA)
                .marca("Volkswagen")
                .modelo("Gol")
                .ano("2020")
                .clienteDocumento("12345678901")
                .build();

        when(veiculoRepository.findByPlaca(PLACA))
                .thenReturn(Optional.of(veiculoExistente));

        when(clienteRepository.existsByDocumento("12345678901"))
                .thenReturn(true);

        when(veiculoRepository.existsByPlacaAndIdNot(PLACA, veiculoId))
                .thenReturn(false);

        when(veiculoRepository.save(any(Veiculo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        VeiculoDTO resultado = veiculoService.edit(PLACA, dtoAtualizado );

        assertNotNull(resultado);
        assertEquals(PLACA, resultado.placa());
        assertEquals("Polo", resultado.modelo());
        assertEquals(2022, resultado.ano());

        verify(veiculoRepository, times(1)).findByPlaca(PLACA);
        verify(clienteRepository, times(1)).existsByDocumento("12345678901");
        verify(veiculoRepository, times(1)).existsByPlacaAndIdNot(PLACA, veiculoId);
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

    @Test
    void naoDeveEditarQuandoVeiculoNaoExistir() {
        when(veiculoRepository.findByPlaca(PLACA_GOL)).thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> veiculoService.edit(PLACA_GOL, veiculoDTO)
        );

        assertEquals("Veículo não encontrado", exception.getMessage());

        verify(veiculoRepository, times(1)).findByPlaca(PLACA_GOL);
        verify(clienteRepository, never()).findByDocumento(anyString());
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void naoDeveEditarQuandoClienteNaoExistir() {
        when(veiculoRepository.findByPlaca(PLACA_GOL)).thenReturn(Optional.of(veiculo));
        when(clienteRepository.existsByDocumento(DOCUMENTO_JOAO)).thenReturn(Boolean.FALSE);

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> veiculoService.edit(PLACA_GOL, veiculoDTO)
        );

        assertEquals(CLIENTE_NAO_ENCONTRADO_PARA_O_DOCUMENTO_INFORMADO, exception.getMessage());

        verify(veiculoRepository, times(1)).findByPlaca(PLACA_GOL);
        verify(clienteRepository, times(1)).existsByDocumento(DOCUMENTO_JOAO);
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void naoDeveEditarQuandoNovaPlacaJaExistir() {
        VeiculoDTO dtoAtualizado = new VeiculoDTO(
                "DEF2G34",
                "Toyota",
                "Corolla",
                2022,
                DOCUMENTO_JOAO
        );

        Veiculo veiculoAtual = Veiculo.builder()
                .id(UUID.randomUUID())
                .placa(PLACA_GOL)
                .marca("Volkswagen")
                .modelo("Gol")
                .ano("2020")
                .clienteDocumento(DOCUMENTO_JOAO)
                .build();

        when(veiculoRepository.findByPlaca(PLACA_GOL))
                .thenReturn(Optional.of(veiculoAtual));

        when(clienteRepository.existsByDocumento(DOCUMENTO_JOAO))
                .thenReturn(true);

        when(veiculoRepository.existsByPlacaAndIdNot("DEF2G34", veiculoAtual.getId()))
                .thenReturn(true);

        ConflitoException exception = assertThrows(
                ConflitoException.class,
                () -> veiculoService.edit(PLACA_GOL, dtoAtualizado)
        );

        assertEquals(JA_EXISTE_VEICULO_CADASTRADO_COM_ESSA_PLACA, exception.getMessage());

        verify(veiculoRepository, times(1)).findByPlaca(PLACA_GOL);
        verify(clienteRepository, times(1)).existsByDocumento(DOCUMENTO_JOAO);
        verify(veiculoRepository, times(1))
                .existsByPlacaAndIdNot("DEF2G34", veiculoAtual.getId());
        verify(veiculoRepository, never()).save(any(Veiculo.class));
    }

    @Test
    void deveDeletarVeiculoPorPlaca() {
        when(veiculoRepository.existsByPlaca(PLACA_GOL)).thenReturn(Boolean.TRUE);
        doNothing().when(veiculoRepository).deleteByPlaca(PLACA_GOL);

        veiculoService.delete(PLACA_GOL);

        verify(veiculoRepository, times(1)).deleteByPlaca(PLACA_GOL);
    }

    @Test
    void deveEditarVeiculoMantendoMesmaPlaca() {
        VeiculoDTO dtoAtualizado = new VeiculoDTO(
                PLACA_GOL,
                VOLKSWAGEN,
                "Gol",
                2021,
                DOCUMENTO_JOAO
        );

        Veiculo veiculoAtualizado = Veiculo.builder()
                .id(veiculo.getId())
                .placa(PLACA_GOL)
                .marca(VOLKSWAGEN)
                .modelo("Gol")
                .ano("2021")
                .clienteDocumento(DOCUMENTO_JOAO)
                .build();

        when(veiculoRepository.findByPlaca(PLACA_GOL))
                .thenReturn(Optional.of(veiculo))
                .thenReturn(Optional.of(veiculo));

        when(clienteRepository.existsByDocumento(DOCUMENTO_JOAO))
                .thenReturn(Boolean.TRUE);

        when(veiculoRepository.save(any(Veiculo.class)))
                .thenReturn(veiculoAtualizado);

        VeiculoDTO resultado = veiculoService.edit(PLACA_GOL, dtoAtualizado);
        assertAll(
                ()->assertNotNull(resultado),
                ()->assertEquals(PLACA_GOL, resultado.placa()),
                ()->assertEquals(VOLKSWAGEN, resultado.marca()),
                ()->assertEquals("Gol", resultado.modelo()),
                ()->assertEquals(2021, resultado.ano()),
                ()->assertEquals(DOCUMENTO_JOAO, resultado.clienteDocumento())
        );


        verify(veiculoRepository, times(1)).findByPlaca(PLACA_GOL);
        verify(clienteRepository, times(1)).existsByDocumento(DOCUMENTO_JOAO);
        verify(veiculoRepository, times(1)).save(any(Veiculo.class));
    }

}