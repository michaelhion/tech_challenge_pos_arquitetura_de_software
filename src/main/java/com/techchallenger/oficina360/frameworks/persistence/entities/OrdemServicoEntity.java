package com.techchallenger.oficina360.frameworks.persistence.entities;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class OrdemServicoEntity {

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
            mappedBy = "ordemServicoEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrdemServicoServicoEntity> servicos = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "ordemServicoEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<OrdemServicoItemEstoqueEntity> itensEstoque = new ArrayList<>();

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
}