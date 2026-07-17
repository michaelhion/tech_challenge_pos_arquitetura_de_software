package com.techchallenger.oficina360.dominio;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class Usuario {

    private UUID id;

    private String email;

    private String senha;

    private String role;

    private String documento;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

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
}

