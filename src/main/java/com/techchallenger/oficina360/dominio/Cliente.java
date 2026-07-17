package com.techchallenger.oficina360.dominio;

import java.util.UUID;


public class Cliente {

    private UUID id;

    private String documento;

    private String nome;

    private String email;

    private String telefone;

    public Cliente(UUID id, String documento, String nome, String email, String telefone) {
        this.id = id;
        this.documento = documento;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public Cliente() {
    }

    public UUID getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }
}