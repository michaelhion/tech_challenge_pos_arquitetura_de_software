package com.techchallenger.oficina360.dominio;

import java.math.BigDecimal;
import java.util.UUID;

public class OrdemServicoServico {

    private UUID id;


    private UUID servicoId;

    private String descricao;

    private BigDecimal valor;

    public BigDecimal getValorTotal() {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    public OrdemServicoServico(UUID id, UUID servicoId, String descricao, BigDecimal valor) {
        this.id = id;
        this.servicoId = servicoId;
        this.descricao = descricao;
        this.valor = valor;
    }

    public OrdemServicoServico(UUID id, String descricao, BigDecimal valor) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getServicoId() {
        return servicoId;
    }

    public void setServicoId(UUID servicoId) {
        this.servicoId = servicoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}