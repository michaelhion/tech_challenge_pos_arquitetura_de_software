package com.techchallenger.oficina360.dominio;

import com.techchallenger.oficina360.dominio.shared.exception.ItemEstoqueInvalidoException;

import java.math.BigDecimal;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_ENTITY_QUANTIDADE_A_RESERVAR_DEVE_SER_MAIOR_QUE_ZERO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_ENTITY_QUANTIDADE_INDISPONIVEL_EM_ESTOQUE;

public class Estoque {

	private UUID id;
	private String nome;
	private BigDecimal valor;
	private Integer quantidade;
	private Integer reservados;
	private String codigo;

	public Estoque(UUID id, String nome, BigDecimal valor, Integer quantidade, Integer reservados, String codigo) {
		validarValor(valor);
		validarQuantidadeTotal(quantidade);
		validarQuantidadeReservada(quantidade, reservados);

		this.id = id;
		this.nome = nome;
		this.valor = valor;
		this.quantidade = quantidade == null ? 0 : quantidade;
		this.reservados = reservados == null ? 0 : reservados;
		this.codigo = codigo;
	}

	public Integer getDisponiveis() {
		return quantidade - reservados;
	}

	public void reservar(Integer quantidadeSolicitada) {
		validarQuantidadePositiva(quantidadeSolicitada);

		if (quantidadeSolicitada > getDisponiveis()) {
			throw new ItemEstoqueInvalidoException(ESTOQUE_ENTITY_QUANTIDADE_INDISPONIVEL_EM_ESTOQUE);
		}

		this.reservados += quantidadeSolicitada;
	}

	public void liberarReserva(Integer quantidadeALiberar) {
		validarQuantidadePositiva(quantidadeALiberar);

		if (quantidadeALiberar > reservados) {
			throw new ItemEstoqueInvalidoException(
					"A quantidade a liberar não pode ser maior " + "que a quantidade reservada.");
		}

		this.reservados -= quantidadeALiberar;
	}

	public void consumirReserva(Integer quantidadeAConsumir) {
		validarQuantidadePositiva(quantidadeAConsumir);

		if (quantidadeAConsumir > reservados) {
			throw new ItemEstoqueInvalidoException(
					"A quantidade a consumir não pode ser maior " + "que a quantidade reservada.");
		}

		if (quantidadeAConsumir > quantidade) {
			throw new ItemEstoqueInvalidoException(
					"A quantidade a consumir não pode ser maior " + "que a quantidade total.");
		}

		this.reservados -= quantidadeAConsumir;
		this.quantidade -= quantidadeAConsumir;
	}

	private void validarQuantidadePositiva(Integer quantidade) {
		if (quantidade == null || quantidade <= 0) {
			throw new ItemEstoqueInvalidoException(ESTOQUE_ENTITY_QUANTIDADE_A_RESERVAR_DEVE_SER_MAIOR_QUE_ZERO);
		}
	}

	private void validarValor(BigDecimal valor) {
		if (valor == null) {
			throw new ItemEstoqueInvalidoException("O valor do item de estoque deve ser informado.");
		}

		if (valor.signum() <= 0) {
			throw new ItemEstoqueInvalidoException("O valor do item de estoque deve ser maior que zero.");
		}
	}

	private void validarQuantidadeTotal(Integer quantidade) {
		if (quantidade != null && quantidade < 0) {
			throw new ItemEstoqueInvalidoException("A quantidade total não pode ser negativa.");
		}
	}

	private void validarQuantidadeReservada(Integer quantidade, Integer reservados) {
		int quantidadeNormalizada = quantidade == null ? 0 : quantidade;

		int reservadosNormalizados = reservados == null ? 0 : reservados;

		if (reservadosNormalizados < 0) {
			throw new ItemEstoqueInvalidoException("A quantidade reservada não pode ser negativa.");
		}

		if (reservadosNormalizados > quantidadeNormalizada) {
			throw new ItemEstoqueInvalidoException(
					"A quantidade reservada não pode ser maior " + "que a quantidade total.");
		}
	}

	public UUID getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public Integer getReservados() {
		return reservados;
	}

	public String getCodigo() {
		return codigo;
	}
}