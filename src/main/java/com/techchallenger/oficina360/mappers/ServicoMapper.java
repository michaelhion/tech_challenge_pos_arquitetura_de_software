package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;

public class ServicoMapper {

    private ServicoMapper(){}

    public static ServicoDTO toDTO(ServicoEntity servicoEntity) {
        return new ServicoDTO(
                servicoEntity.getCodigo(),
                servicoEntity.getDescricao(),
                servicoEntity.getValor(),
                servicoEntity.getTempoMedioExecucaoMinutos()
        );
    }

    public static ServicoEntity toEntity(ServicoDTO servicoDTO) {
        return ServicoEntity.builder()
                .codigo(servicoDTO.codigo())
                .descricao(servicoDTO.descricao())
                .valor(servicoDTO.valor())
                .tempoMedioExecucaoMinutos(servicoDTO.tempoDeExecucaoMedio())
                .build();
    }


    public static void updateEntityFromDto(ServicoDTO dto, ServicoEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setDescricao(dto.descricao());
        entity.setValor(dto.valor());
    }

}
