package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.entities.Usuario;

public class UsuarioMapper {

    public static CriarUsuarioRequestDTO toDTO(Usuario usuario) {
        return new CriarUsuarioRequestDTO(
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getDocumento(),
                usuario.getRole()
        );
    }

    public static Usuario toEntity(CriarUsuarioRequestDTO usuarioRequestDTO) {
        return Usuario.builder()
                .documento(usuarioRequestDTO.documento())
                .senha(usuarioRequestDTO.senha())
                .email(usuarioRequestDTO.email())
                .role(usuarioRequestDTO.role())
                .build();
    }


    public static void updateEntityFromDto(CriarUsuarioRequestDTO dto, Usuario entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDocumento(dto.documento());
        entity.setSenha(dto.senha());
        entity.setEmail(dto.email());
    }

}
