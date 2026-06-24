package com.techchallenger.oficina360.services.factories;

import com.techchallenger.oficina360.entities.Estoque;
import com.techchallenger.oficina360.entities.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.entities.OrdemServicoServico;
import com.techchallenger.oficina360.entities.Servico;
import org.springframework.stereotype.Component;

@Component
public class DiagnosticoFactory {

    public OrdemServicoServico criarServicoDaOs(Servico servico) {
        return OrdemServicoServico.builder()
                .servicoId(servico.getId())
                .descricao(servico.getDescricao())
                .valor(servico.getValor())
                .build();
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