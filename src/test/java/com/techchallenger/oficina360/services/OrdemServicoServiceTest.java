package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.entities.Cliente;
import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.entities.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.entities.OrdemServicoServico;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.exceptions.RegraDeNegocioException;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

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
    private OrdemServicoService ordemServicoService;

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
    }

    @Test
    void deveListarTodasAsOrdensServico() {
        when(ordemServicosRepository.findAll())
                .thenReturn(List.of(ordemServico));

        List<OrdemServicoDTO> resultado = ordemServicoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(ordemServicoId, resultado.get(0).id());
        assertEquals("12345678901", resultado.get(0).documentoCliente());
        assertEquals("ABC1D23", resultado.get(0).placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear.", resultado.get(0).descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.get(0).ordemDeServicoStatus());

        verify(ordemServicosRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarOrdemServicoPorIdQuandoExistir() {
        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        Optional<OrdemServicoDTO> resultado = ordemServicoService.findById(ordemServicoId);

        assertTrue(resultado.isPresent());
        assertEquals(ordemServicoId, resultado.get().id());
        assertEquals("12345678901", resultado.get().documentoCliente());
        assertEquals("ABC1D23", resultado.get().placaVeiculo());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveRetornarOptionalVazioQuandoOrdemServicoNaoExistirPorId() {
        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        Optional<OrdemServicoDTO> resultado = ordemServicoService.findById(ordemServicoId);

        assertTrue(resultado.isEmpty());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveSalvarOrdemServicoComSucesso() {
        OrdemServicoDTO request = new OrdemServicoDTO(
                null,
                "12345678901",
                "abc1d23",
                "Veículo apresenta ruído ao frear.",
                null,
                null
        );

        when(clienteRepository.findByDocumento("12345678901"))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculo));

        when(ordemServicosRepository.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList()))
                .thenReturn(Optional.empty());

        when(ordemServicoFactory.criar(request, cliente, veiculo))
                .thenReturn(ordemServico);

        when(ordemServicosRepository.save(ordemServico))
                .thenReturn(ordemServico);

        OrdemServicoDTO resultado = ordemServicoService.save(request);

        assertNotNull(resultado);
        assertEquals(ordemServicoId, resultado.id());
        assertEquals("12345678901", resultado.documentoCliente());
        assertEquals("ABC1D23", resultado.placaVeiculo());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.ordemDeServicoStatus());

        verify(clienteRepository, times(1)).findByDocumento("12345678901");
        verify(veiculoRepository, times(1)).findByPlaca("ABC1D23");
        verify(ordemServicosRepository, times(1))
                .findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList());
        verify(ordemServicoFactory, times(1)).criar(request, cliente, veiculo);
        verify(ordemServicosRepository, times(1)).save(ordemServico);
    }

    @Test
    void naoDeveSalvarQuandoClienteNaoForEncontrado() {
        OrdemServicoDTO request = new OrdemServicoDTO(
                null,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear.",
                null,
                null
        );

        when(clienteRepository.findByDocumento("12345678901"))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoService.save(request)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());

        verify(clienteRepository, times(1)).findByDocumento("12345678901");
        verify(veiculoRepository, never()).findByPlaca(any());
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void naoDeveSalvarQuandoVeiculoNaoForEncontrado() {
        OrdemServicoDTO request = new OrdemServicoDTO(
                null,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear.",
                null,
                null
        );

        when(clienteRepository.findByDocumento("12345678901"))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoService.save(request)
        );

        assertEquals("Veículo não encontrado", exception.getMessage());

        verify(clienteRepository, times(1)).findByDocumento("12345678901");
        verify(veiculoRepository, times(1)).findByPlaca("ABC1D23");
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void naoDeveSalvarQuandoVeiculoNaoPertencerAoCliente() {
        OrdemServicoDTO request = new OrdemServicoDTO(
                null,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear.",
                null,
                null
        );

        Veiculo veiculoDeOutroCliente = Veiculo.builder()
                .id(veiculoId)
                .placa("ABC1D23")
                .marca("Volkswagen")
                .modelo("Gol")
                .ano("2020")
                .clienteDocumento("00000000000")
                .build();

        when(clienteRepository.findByDocumento("12345678901"))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculoDeOutroCliente));

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> ordemServicoService.save(request)
        );

        assertEquals("O veículo não pertence ao cliente informado", exception.getMessage());

        verify(clienteRepository, times(1)).findByDocumento("12345678901");
        verify(veiculoRepository, times(1)).findByPlaca("ABC1D23");
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void naoDeveSalvarQuandoJaExistirOrdemServicoAtivaParaVeiculo() {
        OrdemServicoDTO request = new OrdemServicoDTO(
                null,
                "12345678901",
                "ABC1D23",
                "Veículo apresenta ruído ao frear.",
                null,
                null
        );

        OrdemServico ordemServicoAtiva = OrdemServico.builder()
                .id(UUID.fromString("99999999-9999-9999-9999-999999999999"))
                .documentoCliente("12345678901")
                .placaVeiculo("ABC1D23")
                .descricaoProblema("Outra OS ativa.")
                .dtHoraAbertura(LocalDateTime.now())
                .ordemDeServicoStatus(OrdemDeServicoStatus.EM_DIAGNOSTICO)
                .build();

        when(clienteRepository.findByDocumento("12345678901"))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca("ABC1D23"))
                .thenReturn(Optional.of(veiculo));

        when(ordemServicosRepository.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList()))
                .thenReturn(Optional.of(ordemServicoAtiva));

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> ordemServicoService.save(request)
        );

        assertTrue(exception.getMessage().contains("Já existe uma ordem de serviço ativa para o veículo"));

        verify(clienteRepository, times(1)).findByDocumento("12345678901");
        verify(veiculoRepository, times(1)).findByPlaca("ABC1D23");
        verify(ordemServicosRepository, times(1))
                .findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList());
        verify(ordemServicoFactory, never()).criar(any(), any(), any());
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void deveEditarDescricaoProblemaDaOrdemServicoComSucesso() {
        OrdemServicoDTO dtoAtualizado = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Descrição atualizada do problema.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado = ordemServicoService.edit(ordemServicoId, dtoAtualizado);

        assertNotNull(resultado);
        assertEquals(ordemServicoId, resultado.id());
        assertEquals("Descrição atualizada do problema.", resultado.descricaoProblema());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, times(1)).save(any(OrdemServico.class));
    }

    @Test
    void naoDeveEditarQuandoOrdemServicoNaoForEncontrada() {
        OrdemServicoDTO dtoAtualizado = new OrdemServicoDTO(
                ordemServicoId,
                "12345678901",
                "ABC1D23",
                "Descrição atualizada do problema.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoService.edit(ordemServicoId, dtoAtualizado)
        );

        assertEquals("Ordem de serviço não encontrada", exception.getMessage());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void deveDeletarOrdemServicoComSucesso() {
        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        doNothing().when(ordemServicosRepository)
                .delete(ordemServico);

        ordemServicoService.delete(ordemServicoId);

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, times(1)).delete(ordemServico);
    }

    @Test
    void naoDeveDeletarQuandoOrdemServicoNaoForEncontrada() {
        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoService.delete(ordemServicoId)
        );

        assertEquals("Ordem de serviço não encontrada", exception.getMessage());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, never()).delete(any());
    }

    @Test
    void deveAprovarOrdemServicoComSucesso() {
        ordemServico.setOrdemDeServicoStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(true);

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado = ordemServicoService.aprovar(ordemServicoId, aprovacaoDTO);

        assertNotNull(resultado);
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_APROVADO, resultado.ordemDeServicoStatus());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, times(1)).save(any(OrdemServico.class));
    }

    @Test
    void deveReprovarOrdemServicoComSucesso() {
        ordemServico.setOrdemDeServicoStatus(OrdemDeServicoStatus.AGUARDANDO_APROVACAO);

        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(false);

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado = ordemServicoService.aprovar(ordemServicoId, aprovacaoDTO);

        assertNotNull(resultado);
        assertEquals(OrdemDeServicoStatus.ORCAMENTO_REPROVADO, resultado.ordemDeServicoStatus());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, times(1)).save(any(OrdemServico.class));
    }

    @Test
    void naoDeveAprovarQuandoOrdemServicoNaoForEncontrada() {
        AprovacaoOrdemServicoDTO aprovacaoDTO = new AprovacaoOrdemServicoDTO(true);

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoService.aprovar(ordemServicoId, aprovacaoDTO)
        );

        assertEquals("Ordem de serviço não encontrada", exception.getMessage());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void deveDiagnosticarOrdemServicoComSucesso() {
        DiagnosticoDTO diagnosticoDTO = new DiagnosticoDTO(
                List.of("SRV-TROCA-OLEO"),
                List.of(new DiagnosticoEstoqueDTO("EST-FILTRO-OLEO", 2))
        );

        OrdemServicoServico servicoDaOs = OrdemServicoServico.builder()
                .servicoId(servico.getId())
                .descricao(servico.getDescricao())
                .valor(servico.getValor())
                .build();

        OrdemServicoItemEstoque itemDaOs = OrdemServicoItemEstoque.criar(
                estoque.getId(),
                estoque.getNome(),
                estoque.getValor(),
                2
        );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(servicoRepository.findByCodigoIn(List.of("SRV-TROCA-OLEO")))
                .thenReturn(List.of(servico));

        when(estoqueRepository.findByCodigoIn(List.of("EST-FILTRO-OLEO")))
                .thenReturn(List.of(estoque));

        doNothing().when(diagnosticoValidator)
                .validar(anyList(), anyMap(), anyMap(), anyMap());

        when(diagnosticoFactory.criarServicoDaOs(servico))
                .thenReturn(servicoDaOs);

        when(diagnosticoFactory.criarItemEstoqueDaOs(estoque, 2))
                .thenReturn(itemDaOs);

        when(estoqueRepository.save(any(Estoque.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(ordemServicosRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado = ordemServicoService.diagnosticar(ordemServicoId, diagnosticoDTO);

        assertNotNull(resultado);
        assertEquals(ordemServicoId, resultado.id());
        assertEquals(OrdemDeServicoStatus.AGUARDANDO_APROVACAO, resultado.ordemDeServicoStatus());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(servicoRepository, times(1)).findByCodigoIn(List.of("SRV-TROCA-OLEO"));
        verify(estoqueRepository, times(1)).findByCodigoIn(List.of("EST-FILTRO-OLEO"));
        verify(diagnosticoValidator, times(1))
                .validar(anyList(), anyMap(), anyMap(), anyMap());
        verify(diagnosticoFactory, times(1)).criarServicoDaOs(servico);
        verify(diagnosticoFactory, times(1)).criarItemEstoqueDaOs(estoque, 2);
        verify(estoqueRepository, times(1)).save(any(Estoque.class));
        verify(ordemServicosRepository, times(1)).save(any(OrdemServico.class));
    }

    @Test
    void naoDeveDiagnosticarQuandoOrdemServicoNaoForEncontrada() {
        DiagnosticoDTO diagnosticoDTO = new DiagnosticoDTO(
                List.of("SRV-TROCA-OLEO"),
                List.of(new DiagnosticoEstoqueDTO("EST-FILTRO-OLEO", 2))
        );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoService.diagnosticar(ordemServicoId, diagnosticoDTO)
        );

        assertEquals("Ordem de serviço não encontrada", exception.getMessage());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(servicoRepository, never()).findByCodigoIn(anyList());
        verify(estoqueRepository, never()).findByCodigoIn(anyList());
        verify(ordemServicosRepository, never()).save(any());
    }
}