package com.techchallenger.oficina360.entities;


import com.techchallenger.oficina360.exceptions.ConflitoException;
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
public class Estoque {

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

    public void reservar(Integer quantidadeReservar) {
        if (quantidadeReservar == null || quantidadeReservar <= 0) {
            throw new ConflitoException("Quantidade a reservar deve ser maior que zero");
        }

        if (quantidadeReservar > getDisponiveis()) {
            throw new ConflitoException("Quantidade indisponível em estoque");
        }

        this.reservados += quantidadeReservar;
    }


}