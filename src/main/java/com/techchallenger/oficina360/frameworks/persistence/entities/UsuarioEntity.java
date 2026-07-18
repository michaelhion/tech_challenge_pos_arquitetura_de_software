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

import java.util.UUID;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "SENHA", nullable = false)
    private String senha;

    @Column(name = "ROLE", nullable = false, length = 50)
    private String role;

    @Column(name = "DOCUMENTO")
    private String documento;
}

