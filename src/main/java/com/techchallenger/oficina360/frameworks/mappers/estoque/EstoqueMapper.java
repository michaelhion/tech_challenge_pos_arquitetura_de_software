package com.techchallenger.oficina360.frameworks.mappers.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EstoqueMapper {


    public Estoque toDomain(EstoqueEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Estoque(
                entity.getId(),
                entity.getNome(),
                entity.getValor(),
                entity.getQuantidade(),
                entity.getReservados(),
                entity.getCodigo()
                );
    }

    public EstoqueEntity toEntity(Estoque domain) {
        if (domain == null) {
            return null;
        }

        return EstoqueEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .valor(domain.getValor())
                .quantidade(domain.getQuantidade())
                .reservados(domain.getReservados())
                .codigo(domain.getCodigo())
                .build();
    }

    public List<EstoqueEntity> domainListToEntityList(List<Estoque> estoques) {
        return estoques.stream()
                .map(this::toEntity)
                .toList();
    }

    public List<Estoque> entityListToDomainList(List<EstoqueEntity> estoqueEntities) {
        return estoqueEntities.stream()
                .map(this::toDomain)
                .toList();
    }


}