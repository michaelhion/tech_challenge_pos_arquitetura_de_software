package com.techchallenger.oficina360.mappers;

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

}
