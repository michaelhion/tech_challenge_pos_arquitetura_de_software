package com.techchallenger.oficina360.frameworks.mappers.servico;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {


    public Servico toDomain(ServicoEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Servico(
                entity.getId(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getCodigo(),
                entity.getTempoMedioExecucaoMinutos()
                );
    }

    public ServicoEntity toEntity(Servico domain) {
        if (domain == null) {
            return null;
        }

        return ServicoEntity.builder()
                .id(domain.getId())
                .descricao(domain.getDescricao())
                .valor(domain.getValor())
                .codigo(domain.getCodigo())
                .tempoMedioExecucaoMinutos(domain.getTempoMedioExecucaoMinutos())
                .build();
    }


}