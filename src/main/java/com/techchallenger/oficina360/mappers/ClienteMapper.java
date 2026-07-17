package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.mascararDocumento;

public class ClienteMapper {

    private ClienteMapper() {
    }

    public static ClienteDTO toDTO(ClienteEntity clienteEntity) {
        return new ClienteDTO(
                mascararDocumento(clienteEntity.getDocumento()),
                clienteEntity.getNome(),
                clienteEntity.getEmail(),
                clienteEntity.getTelefone()
        );
    }

    public static ClienteDTO domainToDTO(Cliente cliente) {
        return new ClienteDTO(
                mascararDocumento(cliente.getDocumento()),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getTelefone()
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

    public static Cliente toDomain(ClienteDTO clienteDTO) {
        return new Cliente(
                null,
                clienteDTO.documento(),
                clienteDTO.nome(),
                clienteDTO.email(),
                clienteDTO.telefone()
        );

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

    public static void updateDomainFromDto(ClienteDTO dto, Cliente domain) {
        if (dto == null || domain == null) {
            return;
        }
        Cliente newCliente = new Cliente(
                domain.getId(),
                domain.getDocumento(),
                domain.getNome(),
                domain.getEmail(),
                domain.getTelefone()
        );
        domain = newCliente;

    }

}
