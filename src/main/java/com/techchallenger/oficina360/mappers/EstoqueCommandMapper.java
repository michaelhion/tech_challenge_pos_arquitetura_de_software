package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;

public class EstoqueCommandMapper {

    private EstoqueCommandMapper(){}


    public static EstoqueCommand domaintoCommand(Estoque estoque) {
        return new EstoqueCommand(
                estoque.getCodigo(),
                estoque.getNome(),
                estoque.getValor(),
                estoque.getQuantidade(),
                estoque.getReservados(),
                estoque.getDisponiveis()
        );
    }

    public static Estoque commandToDomain(EstoqueCommand command) {
        Estoque estoque = new Estoque();
        return estoque.criar(
                command.codigo(),
                command.nome(),
                command.valor(),
                command.quantidade()
        );
    }
}
