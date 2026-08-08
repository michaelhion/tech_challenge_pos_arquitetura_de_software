package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;

public class ClienteCommandMapper {

    private ClienteCommandMapper() {
    }

    public static ClienteCommand domainToCommand(Cliente domain){
        return new ClienteCommand(
                domain.getDocumento(),
                domain.getNome(),
                domain.getEmail(),
                domain.getTelefone()
        );
    }

    public static Cliente domainToCommand(ClienteCommand command){
        return new Cliente(
                command.documento(),
                command.nome(),
                command.email(),
                command.telefone()
        );
    }
}
