package com.techchallenger.oficina360.dominio;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.Roles.ADMIN;
import static com.techchallenger.oficina360.constants.Roles.CLIENTE;

public class Usuario {

    private UUID id;

    private String email;

    private String senha;

    private String role;

    private String documento;

    public String getPassword() {
        return senha;
    }

    public String getUsername() {
        return email;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public Usuario(UUID id, String email, String senha, String role, String documento) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.documento = documento;
    }

    public Usuario() {
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getRole() {
        return role;
    }

    public String getDocumento() {
        return documento;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSenha(String criptografar) {
        this.senha = criptografar;
    }

    public static Usuario criarCliente(
            String email,
            String senha,
            String documento
    ){

        return new Usuario(
                null,
                email,
                senha,
                CLIENTE,
                documento
        );
    }


    public static Usuario criarAdmin(
            String email,
            String senha
    ){

        return new Usuario(
                null,
                email,
                senha,
                ADMIN,
                null
        );
    }

}

