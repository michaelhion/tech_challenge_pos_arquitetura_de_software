package com.techchallenger.oficina360.usecases.factories;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dominio.Servico;

public class DiagnosticoFactory {

    public OrdemServicoServico criarServicoDaOs(Servico servico) {
        return new OrdemServicoServico(
                null,
                servico.getId(),
                servico.getDescricao(),
                servico.getValor());
    }

    public OrdemServicoItemEstoque criarItemEstoqueDaOs(Estoque estoque, Integer quantidade) {
        return OrdemServicoItemEstoque.criar(
                estoque.getId(),
                estoque.getNome(),
                estoque.getValor(),
                quantidade
        );
    }
}