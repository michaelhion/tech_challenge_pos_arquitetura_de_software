package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.*;

public class VeiculoMapper {

    private VeiculoMapper(){}

    public static VeiculoDTO toDTO(VeiculoEntity veiculoEntity) {
        return new VeiculoDTO(
                mascararPlaca(veiculoEntity.getPlaca()),
                veiculoEntity.getMarca(),
                veiculoEntity.getModelo(),
                Integer.parseInt(veiculoEntity.getAno()),
                mascararDocumento(veiculoEntity.getClienteDocumento())
        );
    }

    public static VeiculoDTO domainToDTO(Veiculo domain) {
        return new VeiculoDTO(
                mascararPlaca(domain.getPlaca()),
                domain.getMarca(),
                domain.getModelo(),
                Integer.parseInt(domain.getAno()),
                mascararDocumento(domain.getClienteDocumento())
        );
    }

    public static VeiculoEntity toEntity(VeiculoDTO veiculoDTO) {
        return VeiculoEntity.builder()
                .placa(veiculoDTO.placa())
                .modelo(veiculoDTO.modelo())
                .marca(veiculoDTO.marca())
                .ano(String.valueOf(veiculoDTO.ano()))
                .clienteDocumento(veiculoDTO.clienteDocumento())
                .build();
    }

    public static Veiculo toDomain(VeiculoDTO veiculoDTO) {
        return new Veiculo(
                null,
                normalizarPlaca(veiculoDTO.placa()),
                veiculoDTO.marca(),
                veiculoDTO.modelo(),
                String.valueOf(veiculoDTO.ano()),
                normalizarDocumento(veiculoDTO.clienteDocumento())
        );
    }

    private static Veiculo internalDtoDomain(VeiculoDTO veiculoDTO) {
        return new Veiculo(
                null,
                normalizarPlaca(veiculoDTO.placa()),
                veiculoDTO.marca(),
                veiculoDTO.modelo(),
                String.valueOf(veiculoDTO.ano()),
                normalizarDocumento(veiculoDTO.clienteDocumento())
        );
    }


    public static void updateEntityFromDto(VeiculoDTO dto, VeiculoEntity entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setPlaca(dto.placa());
        entity.setMarca(dto.marca());
        entity.setModelo(dto.modelo());
        entity.setAno(dto.ano().toString());
    }

    public static void updateDomainFromDto(VeiculoDTO dto, Veiculo veiculo) {
        if (dto == null || veiculo == null) {
            return;
        }
        veiculo.atualizarVeiculo(internalDtoDomain(dto));
    }

}
