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
import com.techchallenger.oficina360.mappers.OrdemServicoMapper;
import com.techchallenger.oficina360.repositories.ClienteRepository;
import com.techchallenger.oficina360.repositories.EstoqueRepository;
import com.techchallenger.oficina360.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.repositories.ServicoRepository;
import com.techchallenger.oficina360.repositories.VeiculoRepository;
import com.techchallenger.oficina360.services.factories.DiagnosticoFactory;
import com.techchallenger.oficina360.services.factories.OrdemServicoFactory;
import com.techchallenger.oficina360.services.validators.DiagnosticoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

@Service
@RequiredArgsConstructor
public class OrdemServicoService {

    private static final String ORDEM_DE_SERVICO_NAO_ENCONTRADA = "Ordem de servi\u00E7o n\u00E3o encontrada";
    private static final String CLIENTE_NAO_ENCONTRADO = "Cliente n\u00E3o encontrado";
    private static final String VEICULO_NAO_ENCONTRADO = "Ve\u00EDculo n\u00E3o encontrado";
    private static final String O_VEICULO_NAO_PERTENCE_AO_CLIENTE_INFORMADO = "O ve\u00EDculo n\u00E3o pertence ao cliente informado";

    private static final List<OrdemDeServicoStatus> STATUS_OS_ATIVA = List.of(
            OrdemDeServicoStatus.RECEBIDA,
            OrdemDeServicoStatus.EM_DIAGNOSTICO,
            OrdemDeServicoStatus.AGUARDANDO_APROVACAO,
            OrdemDeServicoStatus.ORCAMENTO_APROVADO,
            OrdemDeServicoStatus.EM_EXECUCAO,
            OrdemDeServicoStatus.FINALIZADA
    );

    private final OrdemServicosRepository ordemServicosRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;
    private final EstoqueRepository estoqueRepository;
    private final OrdemServicoFactory ordemServicoFactory;
    private final DiagnosticoFactory diagnosticoFactory;
    private final DiagnosticoValidator diagnosticoValidator;

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

    public OrdemServicoDTO save(OrdemServicoDTO ordemServicoDTO) {
        String documentoCliente = normalizarDocumento(ordemServicoDTO.documentoCliente());
        String placaVeiculo = normalizarPlaca(ordemServicoDTO.placaVeiculo());

        Cliente cliente = buscarClientePorDocumento(documentoCliente);
        Veiculo veiculo = buscarVeiculoPorPlaca(placaVeiculo);

        validarVeiculoPertenceAoCliente(veiculo, cliente);
        validarNaoExisteOrdemServicoAtivaParaVeiculo(placaVeiculo);

        OrdemServico ordemServico = ordemServicoFactory.criar(ordemServicoDTO, cliente, veiculo);

        OrdemServico ordemServicoSalva = ordemServicosRepository.save(ordemServico);

        return toDTO(ordemServicoSalva);
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

    public OrdemServicoDTO aprovar(UUID id, AprovacaoOrdemServicoDTO aprovacaoDTO) {
        OrdemServico ordemServico = buscarOrdemServicoPorId(id);

        ordemServico.registrarAprovacao(aprovacaoDTO.aprovado());

        OrdemServico ordemServicoAtualizada = ordemServicosRepository.save(ordemServico);

        return toDTO(ordemServicoAtualizada);
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

        return toDTO(ordemServicoAtualizada);
    }

    private OrdemServico buscarOrdemServicoPorId(UUID id) {
        return ordemServicosRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(ORDEM_DE_SERVICO_NAO_ENCONTRADA));
    }

    private Cliente buscarClientePorDocumento(String documentoCliente) {
        return clienteRepository.findByDocumento(documentoCliente)
                .orElseThrow(() -> new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO));
    }

    private Veiculo buscarVeiculoPorPlaca(String placaVeiculo) {
        return veiculoRepository.findByPlaca(placaVeiculo)
                .orElseThrow(() -> new RecursoNaoEncontradoException(VEICULO_NAO_ENCONTRADO));
    }

    private void validarVeiculoPertenceAoCliente(Veiculo veiculo, Cliente cliente) {
        if (!veiculo.getClienteDocumento().equals(cliente.getDocumento())) {
            throw new RegraDeNegocioException(O_VEICULO_NAO_PERTENCE_AO_CLIENTE_INFORMADO);
        }
    }

    private void validarNaoExisteOrdemServicoAtivaParaVeiculo(String placaVeiculo) {
        ordemServicosRepository
                .findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(placaVeiculo, STATUS_OS_ATIVA)
                .ifPresent(os -> {
                    throw new RegraDeNegocioException(
                            String.format(
                                    "Já existe uma ordem de serviço ativa para o veículo de placa %s. OS ativa: %s, status: %s",
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

    private String normalizarDocumento(String documento) {
        return documento == null ? null : documento.trim();
    }

    private String normalizarPlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase();
    }
}