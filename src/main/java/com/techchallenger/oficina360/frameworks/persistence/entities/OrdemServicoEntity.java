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

	@Column(name = "DOCUMENTO_CLIENTE", nullable = false)
	private String documentoCliente;

	@Column(name = "PLACA_VEICULO", nullable = false)
	private String placaVeiculo;

	@Column(name = "DT_HORA_ABERTURA", nullable = false)
	private LocalDateTime dtHoraAbertura;

	@Column(name = "DT_HORA_FECHAMENTO")
	private LocalDateTime dtHoraFechamento;

	@Column(name = "DESCRICAO_PROBLEMA", nullable = false)
	private String descricaoProblema;

	@Enumerated(EnumType.STRING)
	@Column(name = "ORDEM_DE_SERVICO_STATUS", nullable = false)
	private OrdemDeServicoStatus ordemDeServicoStatus;

	@Column(name = "OBSERVACAO_CLIENTE")
	private String observacaoCliente;

	@Column(name = "VALOR_SERVICOS", nullable = false)
	private BigDecimal valorServicos;

	@Column(name = "VALOR_PECAS_INSUMOS", nullable = false)
	private BigDecimal valorPecasInsumos;

	@Column(name = "VALOR_OS", nullable = false)
	private BigDecimal valorOs;

	@Column(name = "DT_HORA_INICIO_EXECUCAO")
	private LocalDateTime dtHoraInicioExecucao;

	@Column(name = "DT_HORA_FIM_EXECUCAO")
	private LocalDateTime dtHoraFimExecucao;

	@Builder.Default
	@OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrdemServicoServicoEntity> servicos = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "ordemServico", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrdemServicoItemEstoqueEntity> itensEstoque = new ArrayList<>();

	public void adicionarServico(OrdemServicoServicoEntity servico) {
		if (servico == null) {
			return;
		}

		servico.setOrdemServico(this);
		this.servicos.add(servico);
	}

	public void adicionarItemEstoque(OrdemServicoItemEstoqueEntity itemEstoque) {
		if (itemEstoque == null) {
			return;
		}

		itemEstoque.setOrdemServico(this);
		this.itensEstoque.add(itemEstoque);
	}

	public void removerServico(OrdemServicoServicoEntity servico) {
		if (servico == null) {
			return;
		}

		this.servicos.remove(servico);
		servico.setOrdemServico(null);
	}

	public void removerItemEstoque(OrdemServicoItemEstoqueEntity itemEstoque) {
		if (itemEstoque == null) {
			return;
		}

		this.itensEstoque.remove(itemEstoque);
		itemEstoque.setOrdemServico(null);
	}

	public void substituirServicos(List<OrdemServicoServicoEntity> novosServicos) {
		this.servicos.forEach(servico -> servico.setOrdemServico(null));

		this.servicos.clear();

		if (novosServicos != null) {
			novosServicos.forEach(this::adicionarServico);
		}
	}

	public void substituirItensEstoque(List<OrdemServicoItemEstoqueEntity> novosItens) {
		this.itensEstoque.forEach(item -> item.setOrdemServico(null));

		this.itensEstoque.clear();

		if (novosItens != null) {
			novosItens.forEach(this::adicionarItemEstoque);
		}
	}
}