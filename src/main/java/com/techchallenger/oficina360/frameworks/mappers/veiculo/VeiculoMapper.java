package com.techchallenger.oficina360.frameworks.mappers.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {


    public Veiculo toDomain(VeiculoEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Veiculo(
                entity.getId(),
                entity.getPlaca(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAno(),
                entity.getClienteDocumento()
                );
    }

    public VeiculoEntity toEntity(Veiculo domain) {
        if (domain == null) {
            return null;
        }

        return VeiculoEntity.builder()
                .id(domain.getId())
                .placa(domain.getPlaca())
                .marca(domain.getMarca())
                .modelo(domain.getModelo())
                .ano(domain.getAno())
                .clienteDocumento(domain.getClienteDocumento())
                .build();
    }


}