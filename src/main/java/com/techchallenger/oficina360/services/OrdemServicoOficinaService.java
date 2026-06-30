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
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.mappers.CriarOrdemServicoMapper;
import com.techchallenger.oficina360.mappers.OrdemServicoMapper;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.EstoqueRepository;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.repositories.ServicoRepository;
import com.techchallenger.oficina360.repositories.TempoExecucaoServicoRepository;
import com.techchallenger.oficina360.repositories.VeiculoRepository;
import com.techchallenger.oficina360.services.factories.DiagnosticoFactory;
import com.techchallenger.oficina360.services.factories.OrdemServicoFactory;
import com.techchallenger.oficina360.services.validators.DiagnosticoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.*;
import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdemServicoOficinaService {


    private static final List<OrdemDeServicoStatus> STATUS_OS_ATIVA = List.of(
            OrdemDeServicoStatus.RECEBIDA,
            OrdemDeServicoStatus.EM_DIAGNOSTICO,
            OrdemDeServicoStatus.AGUARDANDO_APROVACAO,
            OrdemDeServicoStatus.ORCAMENTO_APROVADO,
            OrdemDeServicoStatus.EM_EXECUCAO,
            OrdemDeServicoStatus.FINALIZADA
    );
    private static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";

    private final OrdemServicosRepository ordemServicosRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;
    private final EstoqueRepository estoqueRepository;
    private final OrdemServicoFactory ordemServicoFactory;
    private final DiagnosticoFactory diagnosticoFactory;
    private final DiagnosticoValidator diagnosticoValidator;
    private final TempoExecucaoServicoRepository tempoExecucaoServicoRepository;

    public List<OrdemServicoDTO> findAll() {
        return ordemServicosRepository.findAll()
                .stream()
                .map(OrdemServicoMapper::toDTO)
                .toList();
    }

    public Optional<OrdemServicoDTO> findById(UUID id) {
        return ordemServicosRepository.findById(id)
                .map(OrdemServicoMapper::toDTO);
    }

    public CriarOrdemServicoDTO save(CriarOrdemServicoDTO criarOrdemServicoDTO) {
        String documentoCliente = normalizarDocumento(criarOrdemServicoDTO.documentoCliente());
        String placaVeiculo = normalizarPlaca(criarOrdemServicoDTO.placaVeiculo());

        Cliente cliente = buscarClientePorDocumento(documentoCliente);
        Veiculo veiculo = buscarVeiculoPorPlaca(placaVeiculo);

        validarVeiculoPertenceAoCliente(veiculo.getClienteDocumento(), cliente.getDocumento());
        validarNaoExisteOrdemServicoAtivaParaVeiculo(placaVeiculo);

        OrdemServico ordemServico = ordemServicoFactory.criar(criarOrdemServicoDTO, cliente, veiculo);

        OrdemServico ordemServicoSalva = ordemServicosRepository.save(ordemServico);

        return CriarOrdemServicoMapper.toDTO(ordemServicoSalva);
    }

    public OrdemServicoDTO edit(UUID id, OrdemServicoDTO dto) {
        OrdemServico ordemServico = buscarOrdemServicoPorId(id);

        ordemServico.setDescricaoProblema(dto.descricaoProblema());

        OrdemServico ordemServicoAtualizada = ordemServicosRepository.save(ordemServico);

        return toDTO(ordemServicoAtualizada);
    }

    public void delete(UUID id) {
        OrdemServico ordemServico = buscarOrdemServicoPorId(id);
        ordemServicosRepository.delete(ordemServico);
    }



    @Transactional
    public OrdemServicoDTO diagnosticar(UUID id, DiagnosticoDTO diagnosticoDTO) {
        OrdemServico ordemServico = buscarOrdemServicoPorId(id);

        List<String> codigosServicos = obterCodigosServicos(diagnosticoDTO);
        List<DiagnosticoEstoqueDTO> itensEstoque = obterItensEstoque(diagnosticoDTO);

        Map<String, Integer> quantidadeEstoquePorCodigo =
                agruparQuantidadeEstoquePorCodigo(itensEstoque);

        Map<String, Servico> servicosPorCodigo =
                buscarServicosPorCodigo(codigosServicos);

        Map<String, Estoque> estoquesPorCodigo =
                buscarEstoquesPorCodigo(quantidadeEstoquePorCodigo);

        diagnosticoValidator.validar(
                codigosServicos,
                quantidadeEstoquePorCodigo,
                servicosPorCodigo,
                estoquesPorCodigo
        );

        ordemServico.iniciarDiagnostico();
        ordemServico.limparDiagnosticoAtual();

        adicionarServicosNaOrdemServico(ordemServico, codigosServicos, servicosPorCodigo);
        adicionarItensEstoqueNaOrdemServico(ordemServico, quantidadeEstoquePorCodigo, estoquesPorCodigo);

        ordemServico.finalizarDiagnostico();

        OrdemServico ordemServicoAtualizada = ordemServicosRepository.save(ordemServico);

        return toDTO(ordemServicoAtualizada);
    }

    @Transactional
    public OrdemServicoDTO iniciarExecucao(UUID id) {
        OrdemServico ordemServico = buscarOrdemServicoPorId(id);

        ordemServico.iniciarExecucao();

        OrdemServico ordemServicoAtualizada = ordemServicosRepository.save(ordemServico);

        return toDTO(ordemServicoAtualizada);
    }

    @Transactional
    public OrdemServicoDTO finalizarExecucao(UUID id) {
        OrdemServico ordemServico = buscarOrdemServicoPorId(id);

        ordemServico.finalizarExecucao();

        OrdemServico ordemServicoAtualizada = ordemServicosRepository.save(ordemServico);
        calcularTempoExecucaoESalvar(ordemServico);
        return toDTO(ordemServicoAtualizada);
    }

    private void calcularTempoExecucaoESalvar(OrdemServico ordemServico) {

        int tempoTotal = (int) Duration
                .between(ordemServico.getDtHoraInicioExecucao(), ordemServico.getDtHoraFimExecucao())
                .toMinutes();

        int quantidadeServicos = ordemServico.getServicos().size();

        int tempoPorServico = quantidadeServicos > 0
                ? tempoTotal / quantidadeServicos
                : tempoTotal;

        for (OrdemServicoServico oss : ordemServico.getServicos()) {

            TempoExecucaoServico tempoExecucaoServico = TempoExecucaoServico.builder()
                    .servicoID(oss.getServicoId())
                    .tempoExecucaoMinutos(tempoPorServico)
                    .dataExecucao(LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO)))
                    .build();

            tempoExecucaoServicoRepository.save(tempoExecucaoServico);
        }
    }

    private OrdemServico buscarOrdemServicoPorId(UUID id) {
        return ordemServicosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(OS_ORDEM_DE_SERVICO_NAO_ENCONTRADA));
    }

    private Cliente buscarClientePorDocumento(String documentoCliente) {
        return clienteRepository.findByDocumento(documentoCliente)
                .orElseThrow(() -> new RecursoNaoEncontradoException(OS_CLIENTE_NAO_ENCONTRADO));
    }

    private Veiculo buscarVeiculoPorPlaca(String placaVeiculo) {
        return veiculoRepository.findByPlaca(placaVeiculo)
                .orElseThrow(() -> new RecursoNaoEncontradoException(OS_VEICULO_NAO_ENCONTRADO));
    }

    private void validarVeiculoPertenceAoCliente(String documentoVeiculo, String documentoCliente) {
        if (!documentoVeiculo.equals(documentoCliente)) {
            throw new RegraDeNegocioException(OS_VEICULO_NAO_PERTENCE_AO_CLIENTE);
        }
    }

    private void validarNaoExisteOrdemServicoAtivaParaVeiculo(String placaVeiculo) {
        ordemServicosRepository
                .findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(placaVeiculo, STATUS_OS_ATIVA)
                .ifPresent(os -> {
                    throw new RegraDeNegocioException(
                            String.format(
                                    OS_ORDEM_DE_SERVICO_ATIVA_PARA_O_VEICULO,
                                    os.getPlacaVeiculo(),
                                    os.getId(),
                                    os.getOrdemDeServicoStatus()
                            )
                    );
                });
    }

    private List<String> obterCodigosServicos(DiagnosticoDTO diagnosticoDTO) {
        if (diagnosticoDTO == null || diagnosticoDTO.codigosServicos() == null) {
            return List.of();
        }

        return diagnosticoDTO.codigosServicos()
                .stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(codigo -> !codigo.isBlank())
                .distinct()
                .toList();
    }

    private List<DiagnosticoEstoqueDTO> obterItensEstoque(DiagnosticoDTO diagnosticoDTO) {
        if (diagnosticoDTO == null || diagnosticoDTO.itensEstoque() == null) {
            return List.of();
        }

        return diagnosticoDTO.itensEstoque()
                .stream()
                .filter(Objects::nonNull)
                .toList();
    }

    private Map<String, Integer> agruparQuantidadeEstoquePorCodigo(List<DiagnosticoEstoqueDTO> itensEstoque) {
        if (itensEstoque == null || itensEstoque.isEmpty()) {
            return new LinkedHashMap<>();
        }

        return itensEstoque.stream()
                .filter(item -> item.codigo() != null)
                .collect(Collectors.toMap(
                        item -> item.codigo().trim(),
                        DiagnosticoEstoqueDTO::quantidade,
                        Integer::sum,
                        LinkedHashMap::new
                ));
    }

    private Map<String, Servico> buscarServicosPorCodigo(List<String> codigosServicos) {
        if (codigosServicos == null || codigosServicos.isEmpty()) {
            return Map.of();
        }

        return servicoRepository.findByCodigoIn(codigosServicos)
                .stream()
                .collect(Collectors.toMap(
                        Servico::getCodigo,
                        servico -> servico
                ));
    }

    private Map<String, Estoque> buscarEstoquesPorCodigo(Map<String, Integer> quantidadeEstoquePorCodigo) {
        if (quantidadeEstoquePorCodigo == null || quantidadeEstoquePorCodigo.isEmpty()) {
            return Map.of();
        }

        return estoqueRepository.findByCodigoIn(new ArrayList<>(quantidadeEstoquePorCodigo.keySet()))
                .stream()
                .collect(Collectors.toMap(
                        Estoque::getCodigo,
                        estoque -> estoque
                ));
    }

    private void adicionarServicosNaOrdemServico(
            OrdemServico ordemServico,
            List<String> codigosServicos,
            Map<String, Servico> servicosPorCodigo
    ) {
        for (String codigoServico : codigosServicos) {
            Servico servico = servicosPorCodigo.get(codigoServico);
            OrdemServicoServico servicoDaOs = diagnosticoFactory.criarServicoDaOs(servico);
            ordemServico.adicionarServico(servicoDaOs);
        }
    }

    private void adicionarItensEstoqueNaOrdemServico(
            OrdemServico ordemServico,
            Map<String, Integer> quantidadeEstoquePorCodigo,
            Map<String, Estoque> estoquesPorCodigo
    ) {
        for (Map.Entry<String, Integer> entry : quantidadeEstoquePorCodigo.entrySet()) {
            String codigoEstoque = entry.getKey();
            Integer quantidade = entry.getValue();

            Estoque estoque = estoquesPorCodigo.get(codigoEstoque);

            estoque.reservar(quantidade);

            OrdemServicoItemEstoque itemDaOs =
                    diagnosticoFactory.criarItemEstoqueDaOs(estoque, quantidade);

            ordemServico.adicionarItemEstoque(itemDaOs);

            estoqueRepository.save(estoque);
        }
    }


}