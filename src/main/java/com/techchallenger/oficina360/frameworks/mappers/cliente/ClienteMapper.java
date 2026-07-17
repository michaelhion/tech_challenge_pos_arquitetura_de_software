package com.techchallenger.oficina360.frameworks.mappers.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {


    public Cliente toDomain(ClienteEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Cliente(
                entity.getId(),
                entity.getDocumento(),
                entity.getNome(),
                entity.getEmail(),
                entity.getTelefone()
                );
    }

    public ClienteEntity toEntity(Cliente domain) {
        if (domain == null) {
            return null;
        }

        return ClienteEntity.builder()
                .id(domain.getId())
                .documento(domain.getDocumento())
                .nome(domain.getNome())
                .email(domain.getEmail())
                .telefone(domain.getTelefone())
                .build();
    }


}