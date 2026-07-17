package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoServicoEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrdemServicoServicoMapper {

    public OrdemServicoServico toDomain(
            OrdemServicoServicoEntity entity
    ) {

        return new OrdemServicoServico(
                entity.getId(),
                entity.getServicoId(),
                entity.getDescricao(),
                entity.getValor()
        );
    }

    public List<OrdemServicoServico> toDomainList(
            List<OrdemServicoServicoEntity> entities
    ) {

        if (entities == null) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDomain)
                .toList();
    }
}