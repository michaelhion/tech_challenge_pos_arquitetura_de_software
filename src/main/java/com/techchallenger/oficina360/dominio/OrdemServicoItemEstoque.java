package com.techchallenger.oficina360.dominio;

import java.math.BigDecimal;
import java.util.UUID;

public class OrdemServicoItemEstoque {

	private UUID id;

	private UUID estoqueId;

	private String nome;

	private BigDecimal valorUnitario;

	private Integer quantidade;

	private BigDecimal valorTotal;

	public OrdemServicoItemEstoque(UUID id, UUID estoqueId, String nome, BigDecimal valorUnitario, Integer quantidade) {
		this.id = id;
		this.estoqueId = estoqueId;
		this.nome = nome;
		this.valorUnitario = valorUnitario;
		this.quantidade = quantidade;
		this.valorTotal = calcularValorTotal(valorUnitario, quantidade);
	}

	public OrdemServicoItemEstoque(UUID estoqueId, String nome, BigDecimal valorUnitario, Integer quantidade) {
		this.estoqueId = estoqueId;
		this.nome = nome;
		this.valorUnitario = valorUnitario;
		this.quantidade = quantidade;
		this.valorTotal = calcularValorTotal(valorUnitario, quantidade);
	}

	private static BigDecimal calcularValorTotal(BigDecimal valorUnitario, Integer quantidade) {
		return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
	}

	public static OrdemServicoItemEstoque criar(UUID estoqueId, String nome, BigDecimal valorUnitario,
			Integer quantidade) {
		BigDecimal valorUnitarioSeguro = valorUnitario != null ? valorUnitario : BigDecimal.ZERO;
		Integer quantidadeSegura = quantidade != null ? quantidade : 0;

		return new OrdemServicoItemEstoque(estoqueId, nome, valorUnitarioSeguro, quantidadeSegura);
	}

	public BigDecimal getValorTotal() {
		return valorTotal != null ? valorTotal : BigDecimal.ZERO;
	}

	public UUID getId() {
		return id;
	}

	public UUID getEstoqueId() {
		return estoqueId;
	}

	public String getNome() {
		return nome;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public Integer getQuantidade() {
		return quantidade;
	}
}