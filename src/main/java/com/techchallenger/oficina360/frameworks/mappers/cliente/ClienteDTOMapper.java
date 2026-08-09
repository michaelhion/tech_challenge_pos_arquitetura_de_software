package com.techchallenger.oficina360.frameworks.mappers.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import org.springframework.stereotype.Component;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.mascararDocumento;

@Component
public class ClienteDTOMapper {


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

    public static ClienteDTO toDTO(ClienteEntity clienteEntity) {
        return new ClienteDTO(
                mascararDocumento(clienteEntity.getDocumento()),
                clienteEntity.getNome(),
                clienteEntity.getEmail(),
                clienteEntity.getTelefone()
        );
    }

    public static ClienteEntity toEntity(ClienteDTO clienteDTO) {
        return ClienteEntity.builder()
                .documento(clienteDTO.documento())
                .nome(clienteDTO.nome())
                .email(clienteDTO.email())
                .telefone(clienteDTO.telefone())
                .build();
    }

    public static void updateEntityFromDto(ClienteDTO dto, ClienteEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDocumento(dto.documento());
        entity.setNome(dto.nome());
        entity.setEmail(dto.email());
        entity.setTelefone(dto.telefone());
    }

    public static ClienteDTO commandToDTO(ClienteCommand command){
        return new ClienteDTO(
                command.documento(),
                command.nome(),
                command.email(),
                command.telefone()
        );
    }



    public static ClienteCommand dtoToCommand(ClienteDTO command){
        return new ClienteCommand(
                command.documento(),
                command.nome(),
                command.email(),
                command.telefone()
        );
    }

}