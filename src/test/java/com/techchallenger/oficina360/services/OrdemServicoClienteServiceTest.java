package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoClienteServiceTest {
    @Mock
    private OrdemServicosRepository ordemServicosRepository;

    @InjectMocks
    private OrdemServicoClienteService ordemServicoClienteService;

    private UUID ordemServicoId;
    private OrdemServico ordemServico;


    @BeforeEach
    void setUp() {
        ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");


        ordemServico = OrdemServico.builder()
                .id(ordemServicoId)
                .documentoCliente("12345678901")
                .placaVeiculo("ABC1D23")
                .descricaoProblema("Veículo apresenta ruído ao frear.")
                .dtHoraAbertura(LocalDateTime.now())
                .ordemDeServicoStatus(OrdemDeServicoStatus.RECEBIDA)
                .valorServicos(BigDecimal.ZERO)
                .valorPecasInsumos(BigDecimal.ZERO)
                .valorOs(BigDecimal.ZERO)
                .build();

    }




    @Test
    void deveAprovarOrdemServicoComSucesso() {
        ordemServico.setOrdemDeServicoStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(true,null);

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado = ordemServicoClienteService.aprovar(ordemServicoId, aprovacaoDTO);

        assertNotNull(resultado);
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_APROVADO, resultado.ordemDeServicoStatus());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, times(1)).save(any(OrdemServico.class));
    }

    @Test
    void deveReprovarOrdemServicoComSucesso() {
        ordemServico.setOrdemDeServicoStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(false,"muito caro");

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado = ordemServicoClienteService.aprovar(ordemServicoId, aprovacaoDTO);

        assertNotNull(resultado);
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_REPROVADO, resultado.ordemDeServicoStatus());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, times(1)).save(any(OrdemServico.class));
    }

    @Test
    void naoDeveAprovarQuandoOrdemServicoNaoForEncontrada() {
        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(true,null);

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoClienteService.aprovar(ordemServicoId, aprovacaoDTO)
        );

        assertEquals("Ordem de serviço não encontrada", exception.getMessage());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void deveBuscarOrdemServicoPorId() {

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        Optional<OrdemServicoDTO> resultado =
                ordemServicoClienteService.findById(ordemServicoId);

        assertTrue(resultado.isPresent());

        assertEquals(
                ordemServicoId,
                resultado.get().id()
        );

        verify(ordemServicosRepository)
                .findById(ordemServicoId);
    }

    @Test
    void deveRetornarOptionalVazioQuandoNaoEncontrarOrdemServico() {

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        Optional<OrdemServicoDTO> resultado =
                ordemServicoClienteService.findById(ordemServicoId);

        assertTrue(resultado.isEmpty());

        verify(ordemServicosRepository)
                .findById(ordemServicoId);
    }

    @Test
    void deveSalvarObservacaoQuandoInformada() {

        ordemServico.setOrdemDeServicoStatus(
                OrdemDeServicoStatus.AGUARDANDO_APROVACAO
        );

        AprovacaoOrdemServicoDTO dto =
                new AprovacaoOrdemServicoDTO(
                        true,
                        "Pode executar o serviço"
                );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ordemServicoClienteService.aprovar(
                ordemServicoId,
                dto
        );

        assertEquals(
                "Pode executar o serviço",
                ordemServico.getObservacaoCliente()
        );
    }

    @Test
    void naoDeveSalvarObservacaoQuandoForNull() {

        ordemServico.setOrdemDeServicoStatus(
                OrdemDeServicoStatus.AGUARDANDO_APROVACAO
        );

        AprovacaoOrdemServicoDTO dto =
                new AprovacaoOrdemServicoDTO(
                        true,
                        null
                );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ordemServicoClienteService.aprovar(
                ordemServicoId,
                dto
        );

        assertNull(
                ordemServico.getObservacaoCliente()
        );
    }

    @Test
    void naoDeveSalvarObservacaoQuandoEstiverEmBranco() {

        ordemServico.setOrdemDeServicoStatus(
                OrdemDeServicoStatus.AGUARDANDO_APROVACAO
        );

        AprovacaoOrdemServicoDTO dto =
                new AprovacaoOrdemServicoDTO(
                        true,
                        "   "
                );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ordemServicoClienteService.aprovar(
                ordemServicoId,
                dto
        );

        assertNull(
                ordemServico.getObservacaoCliente()
        );
    }
}