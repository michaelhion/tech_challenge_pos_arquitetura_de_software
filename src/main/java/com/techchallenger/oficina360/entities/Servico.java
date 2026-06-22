package com.techchallenger.oficina360.entities;

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

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "SERVICO")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "DESCRICAO", nullable = false, length = 500)
    private String descricao;

    @Column(name = "VALOR", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "CODIGO", nullable = false, unique = true, length = 100)
    private String codigo;

    @Column(name = "TEMPO_MEDIO_EXECUCAO_MINUTOS")
    private Integer tempoMedioExecucaoMinutos;


}