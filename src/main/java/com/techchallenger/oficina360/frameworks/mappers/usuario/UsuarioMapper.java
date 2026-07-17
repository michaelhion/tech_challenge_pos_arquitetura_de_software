package com.techchallenger.oficina360.frameworks.mappers.usuario;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {


    public Usuario toDomain(UsuarioEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Usuario(
                entity.getId(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getRole(),
                entity.getDocumento()
                );
    }

    public UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }

        return UsuarioEntity.builder()
                .id(domain.getId())
                .email(domain.getEmail())
                .senha(domain.getSenha())
                .role(domain.getRole())
                .documento(domain.getDocumento())
                .build();
    }


}