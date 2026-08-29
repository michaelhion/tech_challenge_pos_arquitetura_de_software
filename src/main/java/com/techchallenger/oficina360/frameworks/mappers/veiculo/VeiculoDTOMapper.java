package com.techchallenger.oficina360.frameworks.mappers.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.frameworks.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;
import org.springframework.stereotype.Component;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.mascararPlaca;

@Component
public class VeiculoDTOMapper {

    public static VeiculoDTO commandToDTO(VeiculoCommand command){
        return new VeiculoDTO(
                mascararPlaca(command.placa()),
                command.marca(),
                command.modelo(),
                Integer.valueOf(command.ano()),
                command.clienteDocumento()
        );
    }

    public static VeiculoCommand dtoToCommand(VeiculoDTO dto){
        return new VeiculoCommand(
                dto.placa(),
                dto.marca(),
                dto.modelo(),
                dto.ano().toString(),
                dto.clienteDocumento()
        );
    }

    public Veiculo toDomain(VeiculoEntity entity){
        return new Veiculo(
                null,
                entity.getPlaca(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAno(),
                entity.getClienteDocumento()
        );
    }

    public VeiculoEntity toEntity(Veiculo veiculo){
        return new VeiculoEntity(
                null,
                veiculo.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getClienteDocumento()
        );
    }
}