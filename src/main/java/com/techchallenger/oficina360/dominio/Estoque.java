package com.techchallenger.oficina360.dominio;


import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;

import java.math.BigDecimal;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_ENTITY_QUANTIDADE_A_RESERVAR_DEVE_SER_MAIOR_QUE_ZERO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_ENTITY_QUANTIDADE_INDISPONIVEL_EM_ESTOQUE;


public class Estoque {


    private UUID id;

    private String nome;

    private BigDecimal valor;

    private Integer quantidade = 0;

    private Integer reservados = 0;

    private String codigo;


    public Integer getDisponiveis() {
        int qtd = quantidade != null ? quantidade : 0;
        int res = reservados != null ? reservados : 0;

        return qtd - res;
    }

    public void reservar(Integer quantidadeReservar) {
        if (quantidadeReservar == null || quantidadeReservar <= 0) {
            throw new ConflitoException(ESTOQUE_ENTITY_QUANTIDADE_A_RESERVAR_DEVE_SER_MAIOR_QUE_ZERO);
        }

        if (quantidadeReservar > getDisponiveis()) {
            throw new ConflitoException(ESTOQUE_ENTITY_QUANTIDADE_INDISPONIVEL_EM_ESTOQUE);
        }

        this.reservados += quantidadeReservar;
    }

    public Estoque(UUID id, String nome, BigDecimal valor, Integer quantidade, Integer reservados, String codigo) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.quantidade = quantidade;
        this.reservados = reservados;
        this.codigo = codigo;
    }

    public Estoque() {
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