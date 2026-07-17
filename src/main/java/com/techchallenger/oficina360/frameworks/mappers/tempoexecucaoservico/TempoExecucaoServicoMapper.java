package com.techchallenger.oficina360.frameworks.mappers.tempoexecucaoservico;

import com.techchallenger.oficina360.dominio.TempoExecucaoServico;
import com.techchallenger.oficina360.frameworks.persistence.entities.TempoExecucaoServicoEntity;
import org.springframework.stereotype.Component;

@Component
public class TempoExecucaoServicoMapper {


    public TempoExecucaoServico toDomain(TempoExecucaoServicoEntity entity) {

        if (entity == null) {
            return null;
        }

        return new TempoExecucaoServico(
                entity.getId(),
                entity.getServicoID(),
                entity.getTempoExecucaoMinutos(),
                entity.getDataExecucao()
                );
    }

    public TempoExecucaoServicoEntity toEntity(TempoExecucaoServico domain) {
        if (domain == null) {
            return null;
        }

        return TempoExecucaoServicoEntity.builder()
                .id(domain.getId())
                .servicoID(domain.getServicoID())
                .tempoExecucaoMinutos(domain.getTempoExecucaoMinutos())
                .dataExecucao(domain.getDataExecucao())
                .build();
    }


}