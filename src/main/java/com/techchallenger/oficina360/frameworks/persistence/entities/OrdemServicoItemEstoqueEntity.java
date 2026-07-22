package com.techchallenger.oficina360.frameworks.persistence.entities;

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
public class OrdemServicoItemEstoqueEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "ID", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "ESTOQUE_ID", nullable = false, updatable = false)
	private UUID estoqueId;

	@Column(name = "NOME", nullable = false)
	private String nome;

	@Column(name = "VALOR_UNITARIO", precision = 10, scale = 2, nullable = false)
	private BigDecimal valorUnitario;

	@Column(name = "QUANTIDADE", nullable = false)
	private Integer quantidade;

	@Column(name = "VALOR_TOTAL", precision = 10, scale = 2, nullable = false)
	private BigDecimal valorTotal;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ORDEM_SERVICO_ID", nullable = false, updatable = false)
	private OrdemServicoEntity ordemServico;
}
