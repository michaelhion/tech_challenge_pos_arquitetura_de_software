package com.techchallenger.oficina360.dominio;

import com.techchallenger.oficina360.frameworks.web.exceptions.RegraDeNegocioException;

import java.math.BigDecimal;
import java.util.UUID;

public class Servico {

    private UUID id;

    private String descricao;

    private BigDecimal valor;

    private String codigo;

    private Integer tempoMedioExecucaoMinutos;

    public Servico(UUID id, String descricao, BigDecimal valor, String codigo, Integer tempoMedioExecucaoMinutos) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.codigo = codigo;
        this.tempoMedioExecucaoMinutos = tempoMedioExecucaoMinutos;
    }

    public Servico(UUID id, String descricao, BigDecimal valor, String codigo) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.codigo = codigo;
    }

    public Servico() {
    }

    public UUID getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public Integer getTempoMedioExecucaoMinutos() {
        return tempoMedioExecucaoMinutos;
    }

    public void inicializaTempoDeExecucao(int tempoInicial) {
        if(this.tempoMedioExecucaoMinutos == null || this.tempoMedioExecucaoMinutos == 0){
            this.tempoMedioExecucaoMinutos = tempoInicial;
        }
        throw new RegraDeNegocioException("N\u00E3o pode alterar tempo j\u00E1 computado!");
    }
}