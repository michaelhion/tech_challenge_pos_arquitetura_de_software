package com.techchallenger.oficina360.services;

import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.entities.Cliente;
import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.entities.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.entities.OrdemServicoServico;
import com.techchallenger.oficina360.entities.Servico;
import com.techchallenger.oficina360.entities.TempoExecucaoServico;
import com.techchallenger.oficina360.entities.Usuario;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.exceptions.ConflitoException;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.EstoqueRepository;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.repositories.ServicoRepository;
import com.techchallenger.oficina360.repositories.TempoExecucaoServicoRepository;
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
class OrdemServicoOficinaServiceTest {

    private static final String CPF = "***8901";
    private static final String PLACA = "ABC***23";

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

    @Mock
    private TempoExecucaoServicoRepository tempoExecucaoServicoRepository;

    @InjectMocks
    private OrdemServicoOficinaService ordemServicoOficinaService;

    private UUID ordemServicoId;
    private UUID clienteId;
    private UUID veiculoId;
    private UUID servicoId;
    private UUID estoqueId;

    private Cliente cliente;
    private Veiculo veiculo;
    private OrdemServico ordemServico;
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
                .documento(OrdemServicoOficinaServiceTest.CPF)
                .nome("João da Silva")
                .email("joao.silva@email.com")
                .telefone("11999999999")
                .build();

        veiculo = Veiculo.builder()
                .id(veiculoId)
                .placa(PLACA)
                .marca("Volkswagen")
                .modelo("Gol")
                .ano("2020")
                .clienteDocumento(OrdemServicoOficinaServiceTest.CPF)
                .build();

        ordemServico = OrdemServico.builder()
                .id(ordemServicoId)
                .documentoCliente(OrdemServicoOficinaServiceTest.CPF)
                .placaVeiculo(PLACA)
                .descricaoProblema("Veículo apresenta ruído ao frear.")
                .dtHoraAbertura(LocalDateTime.now())
                .ordemDeServicoStatus(OrdemDeServicoStatus.RECEBIDA)
                .valorServicos(BigDecimal.ZERO)
                .valorPecasInsumos(BigDecimal.ZERO)
                .valorOs(BigDecimal.ZERO)
                .build();

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

        List<OrdemServicoDTO> resultado = ordemServicoOficinaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(ordemServicoId, resultado.get(0).id());
        assertEquals(CPF, resultado.get(0).documentoCliente());
        assertEquals(PLACA, resultado.get(0).placaVeiculo());
        assertEquals("Veículo apresenta ruído ao frear.", resultado.get(0).descricaoProblema());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.get(0).ordemDeServicoStatus());

        verify(ordemServicosRepository, times(1)).findAll();
    }

    @Test
    void deveBuscarOrdemServicoPorIdQuandoExistir() {
        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        Optional<OrdemServicoDTO> resultado = ordemServicoOficinaService.findById(ordemServicoId);

        assertTrue(resultado.isPresent());
        assertEquals(ordemServicoId, resultado.get().id());
        assertEquals(CPF, resultado.get().documentoCliente());
        assertEquals(PLACA, resultado.get().placaVeiculo());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveRetornarOptionalVazioQuandoOrdemServicoNaoExistirPorId() {
        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        Optional<OrdemServicoDTO> resultado = ordemServicoOficinaService.findById(ordemServicoId);

        assertTrue(resultado.isEmpty());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
    }

    @Test
    void deveSalvarOrdemServicoComSucesso() {
        CriarOrdemServicoDTO request = new CriarOrdemServicoDTO(
                null,
                OrdemServicoOficinaServiceTest.CPF,
                PLACA,
                "Veículo apresenta ruído ao frear.",
                null
        );

        when(clienteRepository.findByDocumento(OrdemServicoOficinaServiceTest.CPF))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca(PLACA))
                .thenReturn(Optional.of(veiculo));

        when(ordemServicosRepository.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList()))
                .thenReturn(Optional.empty());

        when(ordemServicoFactory.criar(request, cliente, veiculo))
                .thenReturn(ordemServico);

        when(ordemServicosRepository.save(ordemServico))
                .thenReturn(ordemServico);

        CriarOrdemServicoDTO resultado = ordemServicoOficinaService.save(request);

        assertNotNull(resultado);
        assertEquals(ordemServicoId, resultado.id());
        assertEquals(OrdemServicoOficinaServiceTest.CPF, resultado.documentoCliente());
        assertEquals(PLACA, resultado.placaVeiculo());
        assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.ordemDeServicoStatus());

        verify(clienteRepository, times(1)).findByDocumento(OrdemServicoOficinaServiceTest.CPF);
        verify(veiculoRepository, times(1)).findByPlaca(PLACA);
        verify(ordemServicosRepository, times(1))
                .findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList());
        verify(ordemServicoFactory, times(1)).criar(request, cliente, veiculo);
        verify(ordemServicosRepository, times(1)).save(ordemServico);
    }

    @Test
    void naoDeveSalvarQuandoClienteNaoForEncontrado() {
        CriarOrdemServicoDTO request = new CriarOrdemServicoDTO(
                null,
                OrdemServicoOficinaServiceTest.CPF,
                PLACA,
                "Veículo apresenta ruído ao frear.",
                null
        );

        when(clienteRepository.findByDocumento(OrdemServicoOficinaServiceTest.CPF))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoOficinaService.save(request)
        );

        assertEquals("Cliente não encontrado", exception.getMessage());

        verify(clienteRepository, times(1)).findByDocumento(OrdemServicoOficinaServiceTest.CPF);
        verify(veiculoRepository, never()).findByPlaca(any());
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void naoDeveSalvarQuandoVeiculoNaoForEncontrado() {
        CriarOrdemServicoDTO request = new CriarOrdemServicoDTO(
                null,
                OrdemServicoOficinaServiceTest.CPF,
                PLACA,
                "Veículo apresenta ruído ao frear.",
                null
        );

        when(clienteRepository.findByDocumento(OrdemServicoOficinaServiceTest.CPF))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca(PLACA))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoOficinaService.save(request)
        );

        assertEquals("Veículo não encontrado", exception.getMessage());

        verify(clienteRepository, times(1)).findByDocumento(OrdemServicoOficinaServiceTest.CPF);
        verify(veiculoRepository, times(1)).findByPlaca(PLACA);
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void naoDeveSalvarQuandoVeiculoNaoPertencerAoCliente() {
        CriarOrdemServicoDTO request = new CriarOrdemServicoDTO(
                null,
                OrdemServicoOficinaServiceTest.CPF,
                PLACA,
                "Veículo apresenta ruído ao frear.",
                null
        );

        Veiculo veiculoDeOutroCliente = Veiculo.builder()
                .id(veiculoId)
                .placa(PLACA)
                .marca("Volkswagen")
                .modelo("Gol")
                .ano("2020")
                .clienteDocumento("00000000000")
                .build();

        when(clienteRepository.findByDocumento(OrdemServicoOficinaServiceTest.CPF))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca(PLACA))
                .thenReturn(Optional.of(veiculoDeOutroCliente));

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> ordemServicoOficinaService.save(request)
        );

        assertEquals("O veículo não pertence ao cliente informado", exception.getMessage());

        verify(clienteRepository, times(1)).findByDocumento(OrdemServicoOficinaServiceTest.CPF);
        verify(veiculoRepository, times(1)).findByPlaca(PLACA);
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void naoDeveSalvarQuandoJaExistirOrdemServicoAtivaParaVeiculo() {
        CriarOrdemServicoDTO request = new CriarOrdemServicoDTO(
                null,
                OrdemServicoOficinaServiceTest.CPF,
                PLACA,
                "Veículo apresenta ruído ao frear.",
                null
        );

        OrdemServico ordemServicoAtiva = OrdemServico.builder()
                .id(UUID.fromString("99999999-9999-9999-9999-999999999999"))
                .documentoCliente(OrdemServicoOficinaServiceTest.CPF)
                .placaVeiculo(PLACA)
                .descricaoProblema("Outra OS ativa.")
                .dtHoraAbertura(LocalDateTime.now())
                .ordemDeServicoStatus(OrdemDeServicoStatus.EM_DIAGNOSTICO)
                .build();

        when(clienteRepository.findByDocumento(OrdemServicoOficinaServiceTest.CPF))
                .thenReturn(Optional.of(cliente));

        when(veiculoRepository.findByPlaca(PLACA))
                .thenReturn(Optional.of(veiculo));

        when(ordemServicosRepository.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList()))
                .thenReturn(Optional.of(ordemServicoAtiva));

        RegraDeNegocioException exception = assertThrows(
                RegraDeNegocioException.class,
                () -> ordemServicoOficinaService.save(request)
        );

        assertTrue(exception.getMessage().contains("Já existe uma ordem de serviço ativa para o veículo"));

        verify(clienteRepository, times(1)).findByDocumento(OrdemServicoOficinaServiceTest.CPF);
        verify(veiculoRepository, times(1)).findByPlaca(PLACA);
        verify(ordemServicosRepository, times(1))
                .findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(any(), anyList());
        verify(ordemServicoFactory, never()).criar(any(), any(), any());
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void deveEditarDescricaoProblemaDaOrdemServicoComSucesso() {
        OrdemServicoDTO dtoAtualizado = new OrdemServicoDTO(
                ordemServicoId,
                OrdemServicoOficinaServiceTest.CPF,
                PLACA,
                "Descrição atualizada do problema.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any(OrdemServico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado = ordemServicoOficinaService.edit(ordemServicoId, dtoAtualizado);

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
                OrdemServicoOficinaServiceTest.CPF,
                PLACA,
                "Descrição atualizada do problema.",
                OrdemDeServicoStatus.RECEBIDA,
                null
        );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoOficinaService.edit(ordemServicoId, dtoAtualizado)
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

        ordemServicoOficinaService.delete(ordemServicoId);

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, times(1)).delete(ordemServico);
    }

    @Test
    void naoDeveDeletarQuandoOrdemServicoNaoForEncontrada() {
        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException exception = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoOficinaService.delete(ordemServicoId)
        );

        assertEquals("Ordem de serviço não encontrada", exception.getMessage());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(ordemServicosRepository, never()).delete(any());
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

        OrdemServicoDTO resultado = ordemServicoOficinaService.diagnosticar(ordemServicoId, diagnosticoDTO);

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
                () -> ordemServicoOficinaService.diagnosticar(ordemServicoId, diagnosticoDTO)
        );

        assertEquals("Ordem de serviço não encontrada", exception.getMessage());

        verify(ordemServicosRepository, times(1)).findById(ordemServicoId);
        verify(servicoRepository, never()).findByCodigoIn(anyList());
        verify(estoqueRepository, never()).findByCodigoIn(anyList());
        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void deveIniciarExecucaoComSucesso() {

        ordemServico.setOrdemDeServicoStatus(
                OrdemDeServicoStatus.ORCAMENTO_APROVADO
        );

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado =
                ordemServicoOficinaService.iniciarExecucao(ordemServicoId);

        assertNotNull(resultado);
        assertEquals(OrdemDeServicoStatus.EM_EXECUCAO,resultado.ordemDeServicoStatus());

        verify(ordemServicosRepository)
                .findById(ordemServicoId);

        verify(ordemServicosRepository)
                .save(any(OrdemServico.class));
    }

    @Test
    void naoDeveIniciarExecucaoQuandoOrdemServicoNaoExistir() {

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> ordemServicoOficinaService
                        .iniciarExecucao(ordemServicoId)
        );
    }

    @Test
    void deveFinalizarExecucaoComSucesso() {

        ordemServico.setDtHoraInicioExecucao(
                LocalDateTime.now().minusMinutes(30)
        );

        ordemServico.setDtHoraFimExecucao(
                LocalDateTime.now()
        );

        OrdemServicoServico servicoDaOs = OrdemServicoServico.builder()
                        .servicoId(servicoId)
                        .build();

        ordemServico.adicionarServico(servicoDaOs);

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ordemServico.setOrdemDeServicoStatus(OrdemDeServicoStatus.EM_EXECUCAO);

        OrdemServicoDTO resultado = ordemServicoOficinaService
                        .finalizarExecucao(ordemServicoId);

        assertNotNull(resultado);
        assertEquals(OrdemDeServicoStatus.FINALIZADA,resultado.ordemDeServicoStatus());

        verify(ordemServicosRepository).save(any());

        verify(tempoExecucaoServicoRepository).save(any(TempoExecucaoServico.class));
    }

    @Test
    void naoDeveFinalizarDiagnosticoSemServicos() {

        DiagnosticoDTO dto = new DiagnosticoDTO(
                        List.of(),
                        List.of());

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        assertThrows(
                ConflitoException.class,
                () -> ordemServicoOficinaService
                        .diagnosticar(ordemServicoId, dto));

        verify(ordemServicosRepository, never()).save(any());
    }

    @Test
    void deveRemoverCodigosDuplicados() {

        DiagnosticoDTO dto =
                new DiagnosticoDTO(
                        List.of(
                                "SRV-TROCA-OLEO",
                                "SRV-TROCA-OLEO"
                        ),
                        List.of());

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(servicoRepository.findByCodigoIn(anyList()))
                .thenReturn(List.of(servico));

        when(diagnosticoFactory.criarServicoDaOs(any()))
                .thenReturn(
                        OrdemServicoServico.builder()
                                .servicoId(servicoId)
                                .build());

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ordemServicoOficinaService.diagnosticar(ordemServicoId, dto);

        verify(servicoRepository).findByCodigoIn(List.of("SRV-TROCA-OLEO"));
    }

    @Test
    void deveTratarDiagnosticoNulo() {

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        assertThrows(
                ConflitoException.class,
                () -> ordemServicoOficinaService
                        .diagnosticar(ordemServicoId, null));

        verify(diagnosticoValidator).validar(
                        anyList(),
                        anyMap(),
                        anyMap(),
                        anyMap());
    }

    @Test
    void deveSomarQuantidadeDeItensDuplicadosNoDiagnostico() {

        DiagnosticoDTO diagnosticoDTO =
                new DiagnosticoDTO(
                        List.of("SRV-TROCA-OLEO"),
                        List.of(
                                new DiagnosticoEstoqueDTO(
                                        "EST-FILTRO-OLEO",
                                        2
                                ),
                                new DiagnosticoEstoqueDTO(
                                        "EST-FILTRO-OLEO",
                                        3)));

        OrdemServicoServico servicoDaOs = OrdemServicoServico.builder()
                        .servicoId(servicoId)
                        .build();

        OrdemServicoItemEstoque itemDaOs = OrdemServicoItemEstoque.criar(
                        estoque.getId(),
                        estoque.getNome(),
                        estoque.getValor(),
                        5);

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(servicoRepository.findByCodigoIn(anyList()))
                .thenReturn(List.of(servico));

        when(estoqueRepository.findByCodigoIn(anyList()))
                .thenReturn(List.of(estoque));

        when(diagnosticoFactory.criarServicoDaOs(any()))
                .thenReturn(servicoDaOs);

        when(diagnosticoFactory.criarItemEstoqueDaOs(
                any(),
                eq(5)))
                .thenReturn(itemDaOs);

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(estoqueRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ordemServicoOficinaService
                .diagnosticar(
                        ordemServicoId,
                        diagnosticoDTO);

        verify(diagnosticoFactory).criarItemEstoqueDaOs(any(), eq(5));
    }

    @Test
    void deveFinalizarExecucaoSemSalvarTempoQuandoNaoExistiremServicos() {

        ordemServico.setOrdemDeServicoStatus(OrdemDeServicoStatus.EM_EXECUCAO);

        ordemServico.setDtHoraInicioExecucao(LocalDateTime.now().minusMinutes(30));

        ordemServico.setDtHoraFimExecucao(LocalDateTime.now());

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        OrdemServicoDTO resultado =
                ordemServicoOficinaService.finalizarExecucao(ordemServicoId);

        assertNotNull(resultado);

        verify(tempoExecucaoServicoRepository,never()).save(any());
    }

    @Test
    void deveIgnorarCodigosEmBrancoNoDiagnostico() {

        DiagnosticoDTO dto = new DiagnosticoDTO(
                        List.of(
                                " ",
                                "\t",
                                "SRV-TROCA-OLEO"),
                        List.of());

        when(ordemServicosRepository.findById(ordemServicoId))
                .thenReturn(Optional.of(ordemServico));

        when(servicoRepository.findByCodigoIn(anyList()))
                .thenReturn(List.of(servico));

        when(diagnosticoFactory.criarServicoDaOs(any()))
                .thenReturn(OrdemServicoServico.builder()
                                .servicoId(servicoId)
                                .build());

        when(ordemServicosRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ordemServicoOficinaService.diagnosticar(ordemServicoId, dto);

        verify(servicoRepository).findByCodigoIn(List.of("SRV-TROCA-OLEO"));
    }
}