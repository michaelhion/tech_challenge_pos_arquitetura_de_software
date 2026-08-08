package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;

public class VeiculoCommandMapper {

    private VeiculoCommandMapper(){}

    public static VeiculoCommand domainToCommand(Veiculo domain){
        return new VeiculoCommand(
              domain.getPlaca(),
              domain.getMarca(),
              domain.getModelo(),
              domain.getAno(),
              domain.getClienteDocumento()
        );
    }


    public static Veiculo commandToDomain(VeiculoCommand command){
        return new Veiculo(
                null,
                command.placa(),
                command.marca(),
                command.modelo(),
                command.ano(),
                command.clienteDocumento()
        );
    }
}
