package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;

public class EstoqueMapper {

    private EstoqueMapper(){}

    public static EstoqueDTO toDTO(EstoqueEntity estoqueEntity) {
        return new EstoqueDTO(
                estoqueEntity.getId(),
                estoqueEntity.getCodigo(),
                estoqueEntity.getNome(),
                estoqueEntity.getValor(),
                estoqueEntity.getQuantidade(),
                estoqueEntity.getReservados(),
                estoqueEntity.getDisponiveis()
        );
    }

    public static EstoqueDTO domaintoDTO(Estoque estoque) {
        return new EstoqueDTO(
                estoque.getId(),
                estoque.getCodigo(),
                estoque.getNome(),
                estoque.getValor(),
                estoque.getQuantidade(),
                estoque.getReservados(),
                estoque.getDisponiveis()
        );
    }

    public static EstoqueEntity toEntity(EstoqueDTO estoqueDTO) {
        return EstoqueEntity.builder()
                .id(estoqueDTO.id())
                .codigo(estoqueDTO.codigo())
                .nome(estoqueDTO.nome())
                .valor(estoqueDTO.valor())
                .quantidade(estoqueDTO.quantidade())
                .reservados(estoqueDTO.reservados())
                .build();
    }

    public static Estoque toDomain(EstoqueDTO estoqueDTO) {
        return new Estoque(
                estoqueDTO.id(),
                estoqueDTO.nome(),
                estoqueDTO.valor(),
                estoqueDTO.quantidade(),
                estoqueDTO.reservados(),
                estoqueDTO.codigo());
    }


    public static void updateEntityFromDto(EstoqueDTO dto, EstoqueEntity estoqueEntity) {
        if (dto == null || estoqueEntity == null) {
            return;
        }

        estoqueEntity.setNome(dto.nome());
        estoqueEntity.setValor(dto.valor());
        estoqueEntity.setQuantidade(dto.quantidade());
        estoqueEntity.setReservados(dto.reservados());
    }

    public static void updateDomainFromDto(EstoqueDTO dto, Estoque estoque) {
        if (dto == null || estoque == null) {
            return;
        }
        Estoque newEstoque = new Estoque(
                estoque.getId(),
                estoque.getNome(),
                estoque.getValor(),
                estoque.getQuantidade(),
                estoque.getReservados(),
                estoque.getCodigo()
        );
        estoque = newEstoque;

    }

}
