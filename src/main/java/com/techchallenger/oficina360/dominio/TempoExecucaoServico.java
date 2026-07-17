package com.techchallenger.oficina360.dominio;

import java.time.LocalDateTime;
import java.util.UUID;


public class TempoExecucaoServico {

    private UUID id;

    private UUID servicoID;

    private Integer tempoExecucaoMinutos;

    private LocalDateTime dataExecucao;

    public TempoExecucaoServico(UUID id, UUID servicoID, Integer tempoExecucaoMinutos, LocalDateTime dataExecucao) {
        this.id = id;
        this.servicoID = servicoID;
        this.tempoExecucaoMinutos = tempoExecucaoMinutos;
        this.dataExecucao = dataExecucao;
    }

    public TempoExecucaoServico( UUID servicoID, Integer tempoExecucaoMinutos, LocalDateTime dataExecucao) {
        this.servicoID = servicoID;
        this.tempoExecucaoMinutos = tempoExecucaoMinutos;
        this.dataExecucao = dataExecucao;
    }

    public TempoExecucaoServico() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getServicoID() {
        return servicoID;
    }

    public Integer getTempoExecucaoMinutos() {
        return tempoExecucaoMinutos;
    }

    public LocalDateTime getDataExecucao() {
        return dataExecucao;
    }
}

