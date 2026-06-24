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

import java.util.UUID;

@Entity
@Table(name = "VEICULO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "PLACA", length = 20)
    private String placa;

    @Column(name = "MARCA", length = 100)
    private String marca;

    @Column(name = "MODELO", length = 100)
    private String modelo;

    @Column(name = "ANO", length = 10)
    private String ano;

    @Column(name = "ClIENTE_DOCUMENTO")
    private String clienteDocumento;
}