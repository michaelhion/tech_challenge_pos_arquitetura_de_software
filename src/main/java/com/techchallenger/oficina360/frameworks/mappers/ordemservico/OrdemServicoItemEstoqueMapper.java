package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoItemEstoqueEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdemServicoItemEstoqueMapper {

    public OrdemServicoItemEstoque toDomain(
            OrdemServicoItemEstoqueEntity entity
    ) {

        return new OrdemServicoItemEstoque(
                entity.getId(),
                entity.getEstoqueId(),
                entity.getNome(),
                entity.getValorUnitario(),
                entity.getQuantidade()
        );
    }

    public OrdemServicoItemEstoqueEntity toEntity(OrdemServicoItemEstoque domain) {
        if (domain == null) {
            return null;
        }

        return OrdemServicoItemEstoqueEntity.builder()
                .id(domain.getId())
                .estoqueId(domain.getEstoqueId())
                .nome(domain.getNome())
                .valorUnitario(domain.getValorUnitario())
                .quantidade(domain.getQuantidade())
                .valorTotal(domain.getValorTotal())
                .build();
    }

    public List<OrdemServicoItemEstoque> toDomainList(
            List<OrdemServicoItemEstoqueEntity> entities
    ) {

        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDomain)
                .toList();
    }

    public List<OrdemServicoItemEstoqueEntity> toEntityList(
            List<OrdemServicoItemEstoque> domains
    ) {

        if (domains == null) {
            return List.of();
        }

        return domains.stream()
                .map(this::toEntity)
                .toList();
    }
}
