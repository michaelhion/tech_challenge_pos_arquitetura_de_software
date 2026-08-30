package com.techchallenger.oficina360.dominio;

import java.io.Serializable;
import java.util.UUID;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String email;

    private String password;

    private String role;

    private String documento;

    public boolean isEnabled() {
        return true;
    }

    public Usuario(UUID id, String email, String password, String role, String documento) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.documento = documento;
    }

    public Usuario(String email, String password, String role, String documento) {
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
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

    public void setPassword(String criptografar) {
        this.password = criptografar;
    }

}

