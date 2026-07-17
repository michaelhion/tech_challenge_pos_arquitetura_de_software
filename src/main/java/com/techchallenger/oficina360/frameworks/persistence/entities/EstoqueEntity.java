package com.techchallenger.oficina360.frameworks.persistence.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "ESTOQUE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "NOME", nullable = false, length = 255)
    private String nome;

    @Column(name = "VALOR", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade = 0;

    @Column(name = "RESERVADOS", nullable = false)
    private Integer reservados = 0;

    @Column(name = "CODIGO", nullable = false, unique = true, length = 100)
    private String codigo;


    @Transient
    public Integer getDisponiveis() {
        int qtd = quantidade != null ? quantidade : 0;
        int res = reservados != null ? reservados : 0;

        return qtd - res;
    }
}