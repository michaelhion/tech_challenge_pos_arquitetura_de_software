package com.techchallenger.oficina360.dominio;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.*;


public class OrdemServico {

    private UUID id;

    private String documentoCliente;

    private String placaVeiculo;

    private LocalDateTime dtHoraAbertura;

    private LocalDateTime dtHoraFechamento;

    private String descricaoProblema;

    private OrdemDeServicoStatus ordemDeServicoStatus;

    private String observacaoCliente;

    private List<OrdemServicoServico> servicos = new ArrayList<>();

    private List<OrdemServicoItemEstoque> itensEstoque = new ArrayList<>();

    private BigDecimal valorServicos = BigDecimal.ZERO;

    private BigDecimal valorPecasInsumos = BigDecimal.ZERO;

    private BigDecimal valorOs = BigDecimal.ZERO;

    private LocalDateTime dtHoraInicioExecucao;

    private LocalDateTime dtHoraFimExecucao;

    public void iniciarDiagnostico() {
        if (this.ordemDeServicoStatus == OrdemDeServicoStatus.RECEBIDA) {
            this.ordemDeServicoStatus = OrdemDeServicoStatus.EM_DIAGNOSTICO;
            return;
        }

        if (this.ordemDeServicoStatus == OrdemDeServicoStatus.EM_DIAGNOSTICO) {
            return;
        }

        throw new ConflitoException(OS_ENTITY_A_ORDEM_DE_SERVICO_NAO_PERMITE_INICIO_DE_DIAGNOSTICO_NO_STATUS_ATUAL);
    }

    public void limparDiagnosticoAtual() {
        if (this.servicos != null) {
            this.servicos.clear();
        }

        if (this.itensEstoque != null) {
            this.itensEstoque.clear();
        }

        recalcularValores();
    }

    public void adicionarServicos(List<OrdemServicoServico> servicos) {
        if (servicos == null || servicos.isEmpty()) {
            throw new IllegalArgumentException(OS_ENTITY_SERVICO_DA_ORDEM_DE_SERVICO_E_OBRIGATORIO);
        }

        this.servicos.addAll(servicos);

        recalcularValores();
    }

    public void adicionarItemEstoque(OrdemServicoItemEstoque itemEstoque) {
        if (itemEstoque == null) {
            throw new IllegalArgumentException(OS_ENTITY_ITEM_DE_ESTOQUE_DA_ORDEM_DE_SERVICO_E_OBRIGATORIO);
        }

//        itemEstoque.setOrdemServico(this);
        this.itensEstoque.add(itemEstoque);

        recalcularValores();
    }

    public void adicionarDiagnostico(
            List<OrdemServicoServico> servicos,
            List<OrdemServicoItemEstoque> itensEstoque
    ) {
        if (servicos == null || servicos.isEmpty()) {
            throw new ConflitoException(
                    OS_ENTITY_A_ORDEM_DE_SERVICO_PRECISA_TER_AO_MENOS_UM_SERVICO_DEFINIDO
            );
        }

        this.servicos.clear();
        this.itensEstoque.clear();

        this.servicos.addAll(servicos);
        this.itensEstoque.addAll(itensEstoque);

        recalcularValores();
    }

    public void finalizarDiagnostico() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.EM_DIAGNOSTICO
                && this.ordemDeServicoStatus != OrdemDeServicoStatus.RECEBIDA) {
            throw new ConflitoException(OS_ENTITY_A_ORDEM_DE_SERVICO_NAO_ESTA_EM_DIAGNOSTICO);
        }

        if (this.servicos == null || this.servicos.isEmpty()) {
            throw new ConflitoException(OS_ENTITY_A_ORDEM_DE_SERVICO_PRECISA_TER_AO_MENOS_UM_SERVICO_DEFINIDO);
        }

        recalcularValores();
        this.ordemDeServicoStatus = OrdemDeServicoStatus.AGUARDANDO_APROVACAO;
    }

    public void registrarAprovacao(Boolean aprovado) {
        if (aprovado == null) {
            throw new IllegalArgumentException(OS_ENTITY_INFORME_SE_O_ORCAMENTO_FOI_APROVADO_OU_REPROVADO);
        }

        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.AGUARDANDO_APROVACAO) {
            throw new ConflitoException(OS_ENTITY_A_ORDEM_DE_SERVICO_NAO_ESTA_AGUARDANDO_APROVACAO);
        }

        if (Boolean.TRUE.equals(aprovado)) {
            this.ordemDeServicoStatus = OrdemDeServicoStatus.ORCAMENTO_APROVADO;
        } else {
            this.ordemDeServicoStatus = OrdemDeServicoStatus.ORCAMENTO_REPROVADO;
        }
    }

    public void iniciarExecucao() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.ORCAMENTO_APROVADO) {
            throw new ConflitoException(OS_ENTITY_A_EXECUCAO_SO_PODE_SER_INICIADA_APOS_APROVACAO_DO_ORCAMENTO);
        }

        this.dtHoraInicioExecucao = LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
        this.dtHoraFimExecucao = null;
        this.ordemDeServicoStatus = OrdemDeServicoStatus.EM_EXECUCAO;
    }

    public void finalizarExecucao() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.EM_EXECUCAO) {
            throw new ConflitoException(OS_ENTITY_A_EXECUCAO_SO_PODE_SER_FINALIZADA_QUANDO_ESTIVER_EM_EXECUCAO);
        }

        if (this.dtHoraInicioExecucao == null) {
            throw new ConflitoException(OS_ENTITY_A_EXECUCAO_NAO_POSSUI_DATA_HORA_DE_INICIO_REGISTRADA);
        }

        this.dtHoraFimExecucao = LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
        this.ordemDeServicoStatus = OrdemDeServicoStatus.FINALIZADA;
    }


    public void entregar() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.FINALIZADA) {
            throw new ConflitoException(OS_ENTITY_A_ORDEM_DE_SERVICO_SO_PODE_SER_ENTREGUE_APOS_FINALIZACAO);
        }

        this.dtHoraFechamento = LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
        this.ordemDeServicoStatus = OrdemDeServicoStatus.ENTREGUE;
    }

    public void recalcularValores() {
        this.valorServicos = calcularValorServicos();
        this.valorPecasInsumos = calcularValorPecasInsumos();
        this.valorOs = this.valorServicos.add(this.valorPecasInsumos);
    }

    private BigDecimal calcularValorServicos() {
        if (this.servicos == null || this.servicos.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return this.servicos.stream()
                .map(OrdemServicoServico::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularValorPecasInsumos() {
        if (this.itensEstoque == null || this.itensEstoque.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return this.itensEstoque.stream()
                .map(OrdemServicoItemEstoque::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public UUID getId() {
        return id;
    }

    public String getDocumentoCliente() {
        return documentoCliente;
    }

    public String getPlacaVeiculo() {
        return placaVeiculo;
    }

    public LocalDateTime getDtHoraAbertura() {
        return dtHoraAbertura;
    }

    public LocalDateTime getDtHoraFechamento() {
        return dtHoraFechamento;
    }

    public String getDescricaoProblema() {
        return descricaoProblema;
    }

    public OrdemDeServicoStatus getOrdemDeServicoStatus() {
        return ordemDeServicoStatus;
    }

    public String getObservacaoCliente() {
        return observacaoCliente;
    }

    public List<OrdemServicoServico> getServicos() {
        return servicos;
    }

    public List<OrdemServicoItemEstoque> getItensEstoque() {
        return itensEstoque;
    }

    public BigDecimal getValorServicos() {
        return valorServicos;
    }

    public BigDecimal getValorPecasInsumos() {
        return valorPecasInsumos;
    }

    public BigDecimal getValorOs() {
        return valorOs;
    }

    public LocalDateTime getDtHoraInicioExecucao() {
        return dtHoraInicioExecucao;
    }

    public LocalDateTime getDtHoraFimExecucao() {
        return dtHoraFimExecucao;
    }

    public OrdemServico(UUID id, String documentoCliente, String placaVeiculo, LocalDateTime dtHoraAbertura,
                        LocalDateTime dtHoraFechamento, String descricaoProblema,
                        OrdemDeServicoStatus ordemDeServicoStatus, String observacaoCliente,
                        List<OrdemServicoServico> servicos, List<OrdemServicoItemEstoque> itensEstoque,
                        BigDecimal valorServicos, BigDecimal valorPecasInsumos, BigDecimal valorOs,
                        LocalDateTime dtHoraInicioExecucao, LocalDateTime dtHoraFimExecucao) {
        this.id = id;
        this.documentoCliente = documentoCliente;
        this.placaVeiculo = placaVeiculo;
        this.dtHoraAbertura = dtHoraAbertura;
        this.dtHoraFechamento = dtHoraFechamento;
        this.descricaoProblema = descricaoProblema;
        this.ordemDeServicoStatus = ordemDeServicoStatus;
        this.observacaoCliente = observacaoCliente;
        this.servicos = servicos!= null
                ? new ArrayList<>(servicos)
                : new ArrayList<>();
        this.itensEstoque = itensEstoque!= null
                ? new ArrayList<>(itensEstoque)
                : new ArrayList<>();
        this.valorServicos = valorServicos;
        this.valorPecasInsumos = valorPecasInsumos;
        this.valorOs = valorOs;
        this.dtHoraInicioExecucao = dtHoraInicioExecucao;
        this.dtHoraFimExecucao = dtHoraFimExecucao;
    }

    public OrdemServico(String documentoCliente, String placaVeiculo, String descricaoProblema,
            LocalDateTime dtHoraAbertura, OrdemDeServicoStatus ordemDeServicoStatus) {
        this.documentoCliente = documentoCliente;
        this.placaVeiculo = placaVeiculo;
        this.descricaoProblema = descricaoProblema;
        this.dtHoraAbertura = dtHoraAbertura;
        this.ordemDeServicoStatus = ordemDeServicoStatus;
    }

	public void setObservacaoCliente(String observacao) {
		this.observacaoCliente = observacao;
	}

    public void setDescricaoProblema(String descricaoProblema) {
        this.descricaoProblema = descricaoProblema;
    }
}