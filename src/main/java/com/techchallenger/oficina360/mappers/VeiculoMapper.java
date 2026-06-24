package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.entities.Veiculo;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.mascararDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.mascararPlaca;

public class VeiculoMapper {

    private VeiculoMapper(){}

    public static VeiculoDTO toDTO(Veiculo veiculoEntity) {
        return new VeiculoDTO(
                mascararPlaca(veiculoEntity.getPlaca()),
                veiculoEntity.getMarca(),
                veiculoEntity.getModelo(),
                Integer.parseInt(veiculoEntity.getAno()),
                mascararDocumento(veiculoEntity.getClienteDocumento())
        );
    }

    public static Veiculo toEntity(VeiculoDTO veiculoDTO) {
        return Veiculo.builder()
                .placa(veiculoDTO.placa())
                .modelo(veiculoDTO.modelo())
                .marca(veiculoDTO.marca())
                .ano(String.valueOf(veiculoDTO.ano()))
                .clienteDocumento(veiculoDTO.clienteDocumento())
                .build();
    }


    public static void updateEntityFromDto(VeiculoDTO dto, Veiculo entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setPlaca(dto.placa());
        entity.setMarca(dto.marca());
        entity.setModelo(dto.modelo());
        entity.setAno(dto.ano().toString());
    }

}
