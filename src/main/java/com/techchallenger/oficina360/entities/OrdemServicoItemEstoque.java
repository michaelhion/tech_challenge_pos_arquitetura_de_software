package com.techchallenger.oficina360.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ORDEM_SERVICO_ITEM_ESTOQUE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServicoItemEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDEM_SERVICO_ID", nullable = false)
    private OrdemServico ordemServico;

    @Column(name = "ESTOQUE_ID", nullable = false)
    private UUID estoqueId;

    @Column(name = "NOME", nullable = false, length = 255)
    private String nome;

    @Column(name = "VALOR_UNITARIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "VALOR_TOTAL", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    public BigDecimal getValorTotal() {
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }

    public static OrdemServicoItemEstoque criar(
            UUID estoqueId,
            String nome,
            BigDecimal valorUnitario,
            Integer quantidade
    ) {
        BigDecimal valorSeguro = valorUnitario != null ? valorUnitario : BigDecimal.ZERO;
        Integer quantidadeSegura = quantidade != null ? quantidade : 0;

        return OrdemServicoItemEstoque.builder()
                .estoqueId(estoqueId)
                .nome(nome)
                .valorUnitario(valorSeguro)
                .quantidade(quantidadeSegura)
                .valorTotal(valorSeguro.multiply(BigDecimal.valueOf(quantidadeSegura)))
                .build();
    }
}