package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;

public class UsuarioMapper {

    private UsuarioMapper(){}

    public static CriarUsuarioRequestDTO toDTO(UsuarioEntity usuarioEntity) {
        return new CriarUsuarioRequestDTO(
                usuarioEntity.getEmail(),
                usuarioEntity.getSenha(),
                usuarioEntity.getDocumento(),
                usuarioEntity.getRole()
        );
    }

    public static UsuarioEntity toEntity(CriarUsuarioRequestDTO usuarioRequestDTO) {
        return UsuarioEntity.builder()
                .documento(usuarioRequestDTO.documento())
                .senha(usuarioRequestDTO.senha())
                .email(usuarioRequestDTO.email())
                .role(usuarioRequestDTO.role())
                .build();
    }


    public static void updateEntityFromDto(CriarUsuarioRequestDTO dto, UsuarioEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDocumento(dto.documento());
        entity.setSenha(dto.senha());
        entity.setEmail(dto.email());
    }

	public static Usuario toDomain(CriarUsuarioRequestDTO dto) {
        return new Usuario(
                null,
                dto.email(),
                dto.senha(),
                dto.role(),
                dto.documento()
                );
	}

    public static Usuario entityToDomain(UsuarioEntity entity) {

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
}
