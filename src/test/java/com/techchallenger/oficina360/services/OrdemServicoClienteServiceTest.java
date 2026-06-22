package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.entities.Cliente;
import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.EstoqueRepository;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.repositories.ServicoRepository;
import com.techchallenger.oficina360.repositories.VeiculoRepository;
import com.techchallenger.oficina360.services.factories.DiagnosticoFactory;
import com.techchallenger.oficina360.services.factories.OrdemServicoFactory;
import com.techchallenger.oficina360.services.validators.DiagnosticoValidator;
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

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private ServicoRepository servicoRepository;

    @Mock
    private EstoqueRepository estoqueRepository;

    @Mock
    private OrdemServicoFactory ordemServicoFactory;

    @Mock
    private DiagnosticoFactory diagnosticoFactory;

    @Mock
    private DiagnosticoValidator diagnosticoValidator;

    @InjectMocks
    private OrdemServicoClienteService ordemServicoClienteService;

    private UUID ordemServicoId;
    private UUID clienteId;
    private UUID veiculoId;
    private UUID servicoId;
    private UUID estoqueId;

    private Cliente cliente;
    private Veiculo veiculo;
    private OrdemServico ordemServico;
    private OrdemServicoDTO ordemServicoDTO;
    private Servico servico;
    private Estoque estoque;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        ordemServicoId = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");
        clienteId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        veiculoId = UUID.fromString("22222222-2222-2222-2222-222222222222");
        servicoId = UUID.fromString("33333333-3333-3333-3333-333333333333");
        estoqueId = UUID.fromString("44444444-4444-4444-4444-444444444444");

        cliente = Cliente.builder()
                .id(clienteId)
                .documento("12345678901")
                .nome("João da Silva")
                .email("joao.silva@email.com")
                .telefone("11999999999")
                .build();

        veiculo = Veiculo.builder()
                .id(veiculoId)
                .placa("ABC1D23")
                .marca("Volkswagen")
                .modelo("Gol")
                .ano("2020")
                .clienteDocumento("12345678901")
                .build();

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

        ordemServicoDTO = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );

        servico = Servico.builder()
                .id(servicoId)
                .codigo("SRV-TROCA-OLEO")
                .descricao("Troca de óleo")
                .valor(BigDecimal.valueOf(150.00))
                .build();

        estoque = Estoque.builder()
                .id(estoqueId)
                .codigo("EST-FILTRO-OLEO")
                .nome("Filtro de óleo")
                .valor(BigDecimal.valueOf(45.90))
                .quantidade(10)
                .reservados(0)
                .build();

        usuario = Usuario
                .builder()
                .senha("senha123")
                .email("email@teste.com")
                .documento("12312312312")
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