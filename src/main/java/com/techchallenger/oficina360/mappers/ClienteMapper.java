package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.entities.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente clienteEntity) {
        return new ClienteDTO(
                clienteEntity.getDocumento(),
                clienteEntity.getNome(),
                clienteEntity.getEmail(),
                clienteEntity.getTelefone()
        );
    }

    public static Cliente toEntity(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .documento(clienteDTO.documento())
                .nome(clienteDTO.nome())
                .email(clienteDTO.email())
                .telefone(clienteDTO.telefone())
                .build();
    }


    public static void updateEntityFromDto(ClienteDTO dto, Cliente entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDocumento(dto.documento());
        entity.setNome(dto.nome());
        entity.setEmail(dto.email());
        entity.setTelefone(dto.telefone());
    }

}
