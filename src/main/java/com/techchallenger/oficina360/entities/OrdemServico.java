package com.techchallenger.oficina360.entities;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.exceptions.ConflitoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "ORDEM_SERVICO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServico {

    private static final String AMERICA_SAO_PAULO = "America/Sao_Paulo";
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "DOCUMENTO_CLIENTE", nullable = false, length = 50)
    private String documentoCliente;

    @Column(name = "PLACA_VEICULO", nullable = false, updatable = false, length = 20)
    private String placaVeiculo;

    @Column(name = "DT_HORA_ABERTURA", nullable = false)
    private LocalDateTime dtHoraAbertura;

    @Column(name = "DT_HORA_FECHAMENTO")
    private LocalDateTime dtHoraFechamento;

    @Column(name = "DESCRICAO_PROBLEMA", length = 2000)
    private String descricaoProblema;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDEM_DE_SERVICO_STATUS", nullable = false, length = 50)
    private OrdemDeServicoStatus ordemDeServicoStatus;

    @Column(name = "OBSERVACAO_CLIENTE", length = 200)
    private String observacaoCliente;

    @Builder.Default
    @OneToMany(
            mappedBy = "ordemServico",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrdemServicoServico> servicos = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "ordemServico",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrdemServicoItemEstoque> itensEstoque = new ArrayList<>();

    @Builder.Default
    @Column(name = "VALOR_SERVICOS", precision = 10, scale = 2)
    private BigDecimal valorServicos = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "VALOR_PECAS_INSUMOS", precision = 10, scale = 2)
    private BigDecimal valorPecasInsumos = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "VALOR_OS", precision = 10, scale = 2)
    private BigDecimal valorOs = BigDecimal.ZERO;

    @Column(name = "DT_HORA_INICIO_EXECUCAO")
    private LocalDateTime dtHoraInicioExecucao;

    @Column(name = "DT_HORA_FIM_EXECUCAO")
    private LocalDateTime dtHoraFimExecucao;

    public void iniciarDiagnostico() {
        if (this.ordemDeServicoStatus == OrdemDeServicoStatus.RECEBIDA) {
            this.ordemDeServicoStatus = OrdemDeServicoStatus.EM_DIAGNOSTICO;
            return;
        }

        if (this.ordemDeServicoStatus == OrdemDeServicoStatus.EM_DIAGNOSTICO) {
            return;
        }

        throw new ConflitoException("A ordem de serviço não permite início de diagnóstico no status atual");
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

    public void adicionarServico(OrdemServicoServico servico) {
        if (servico == null) {
            throw new IllegalArgumentException("Serviço da ordem de serviço é obrigatório");
        }

        servico.setOrdemServico(this);
        this.servicos.add(servico);

        recalcularValores();
    }

    public void adicionarItemEstoque(OrdemServicoItemEstoque itemEstoque) {
        if (itemEstoque == null) {
            throw new IllegalArgumentException("Item de estoque da ordem de serviço é obrigatório");
        }

        itemEstoque.setOrdemServico(this);
        this.itensEstoque.add(itemEstoque);

        recalcularValores();
    }

    public void finalizarDiagnostico() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.EM_DIAGNOSTICO
                && this.ordemDeServicoStatus != OrdemDeServicoStatus.RECEBIDA) {
            throw new ConflitoException("A ordem de serviço não está em diagnóstico");
        }

        if (this.servicos == null || this.servicos.isEmpty()) {
            throw new ConflitoException("A ordem de serviço precisa ter ao menos um serviço definido");
        }

        recalcularValores();
        this.ordemDeServicoStatus = OrdemDeServicoStatus.AGUARDANDO_APROVACAO;
    }

    public void registrarAprovacao(Boolean aprovado) {
        if (aprovado == null) {
            throw new IllegalArgumentException("Informe se o orçamento foi aprovado ou reprovado");
        }

        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.AGUARDANDO_APROVACAO) {
            throw new ConflitoException("A ordem de serviço não está aguardando aprovação");
        }

        if (Boolean.TRUE.equals(aprovado)) {
            this.ordemDeServicoStatus = OrdemDeServicoStatus.ORCAMENTO_APROVADO;
        } else {
            this.ordemDeServicoStatus = OrdemDeServicoStatus.ORCAMENTO_REPROVADO;
        }
    }

    public void iniciarExecucao() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.ORCAMENTO_APROVADO) {
            throw new ConflitoException("A execução só pode ser iniciada após aprovação do orçamento");
        }

        this.dtHoraInicioExecucao = LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
        this.dtHoraFimExecucao = null;
        this.ordemDeServicoStatus = OrdemDeServicoStatus.EM_EXECUCAO;
    }

    public void finalizarExecucao() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.EM_EXECUCAO) {
            throw new ConflitoException("A execução só pode ser finalizada quando estiver em execução");
        }

        if (this.dtHoraInicioExecucao == null) {
            throw new ConflitoException("A execução não possui data/hora de início registrada");
        }

        this.dtHoraFimExecucao = LocalDateTime.now(ZoneId.of(AMERICA_SAO_PAULO));
        this.ordemDeServicoStatus = OrdemDeServicoStatus.FINALIZADA;
    }


    public void entregar() {
        if (this.ordemDeServicoStatus != OrdemDeServicoStatus.FINALIZADA) {
            throw new ConflitoException("A ordem de serviço só pode ser entregue após finalização");
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


}