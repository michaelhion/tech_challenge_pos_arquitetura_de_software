package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.entities.Estoque;

public class EstoqueMapper {

    public static EstoqueDTO toDTO(Estoque estoqueEntity) {
        return new EstoqueDTO(
                estoqueEntity.getCodigo(),
                estoqueEntity.getNome(),
                estoqueEntity.getValor(),
                estoqueEntity.getQuantidade(),
                estoqueEntity.getReservados(),
                estoqueEntity.getDisponiveis()
        );
    }

    public static Estoque toEntity(EstoqueDTO estoqueDTO) {
        return Estoque.builder()
                .codigo(estoqueDTO.codigo())
                .nome(estoqueDTO.nome())
                .valor(estoqueDTO.valor())
                .quantidade(estoqueDTO.quantidade())
                .reservados(estoqueDTO.reservados())
                .build();
    }


    public static void updateEntityFromDto(EstoqueDTO dto, Estoque estoqueEntity) {
        if (dto == null || estoqueEntity == null) {
            return;
        }

        estoqueEntity.setNome(dto.nome());
        estoqueEntity.setValor(dto.valor());
        estoqueEntity.setQuantidade(dto.quantidade());
        estoqueEntity.setReservados(dto.reservados());
    }

}
