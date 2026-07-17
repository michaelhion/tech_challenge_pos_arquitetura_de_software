package com.techchallenger.oficina360.frameworks.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "TEMPO_EXECUCAO_SERVICO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempoExecucaoServicoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "SERVICO_ID")
    private UUID servicoID;

    @Column(name = "TEMPO_EXECUCAO_MINUTOS")
    private Integer tempoExecucaoMinutos;

    @Column(name = "DATA_EXECUCAO")
    private LocalDateTime dataExecucao;
}

