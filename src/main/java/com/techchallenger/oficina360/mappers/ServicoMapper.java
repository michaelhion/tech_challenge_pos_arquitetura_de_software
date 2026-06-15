package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.entities.Servico;

public class ServicoMapper {

    public static ServicoDTO toDTO(Servico servicoEntity) {
        return new ServicoDTO(
                servicoEntity.getCodigo(),
                servicoEntity.getDescricao(),
                servicoEntity.getValor()
        );
    }

    public static Servico toEntity(ServicoDTO servicoDTO) {
        return Servico.builder()
                .codigo(servicoDTO.codigo())
                .descricao(servicoDTO.descricao())
                .valor(servicoDTO.valor())
                .build();
    }


    public static void updateEntityFromDto(ServicoDTO dto, Servico entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDescricao(dto.descricao());
        entity.setValor(dto.valor());
    }

}
