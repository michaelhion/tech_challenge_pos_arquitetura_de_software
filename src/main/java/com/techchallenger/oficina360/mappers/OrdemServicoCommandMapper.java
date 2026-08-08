package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.usecases.ordemservico.command.DadosFinanceirosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoDiagnosticoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.PecasInsumosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ServicosAdicionadosCommand;

import java.util.ArrayList;
import java.util.List;

public class OrdemServicoCommandMapper {

    private OrdemServicoCommandMapper() {
    }
    public static OrdemServicoRespCommand domainToCommand(OrdemServico domain) {
        return new OrdemServicoRespCommand(
                domain.getId(),
                domain.getDocumentoCliente(),
                domain.getPlacaVeiculo(),
                domain.getDescricaoProblema(),
                domain.getOrdemDeServicoStatus()
        );
    }

    public static OrdemServicoDiagnosticoRespCommand domainToDiagnosticoCommand(OrdemServico domain){
        return new OrdemServicoDiagnosticoRespCommand(
                domain.getId(),
                domain.getDocumentoCliente(),
                domain.getPlacaVeiculo(),
                domain.getDescricaoProblema(),
                domain.getOrdemDeServicoStatus(),
                toDadosFinanceiros(domain)
        );
    }

    private static DadosFinanceirosCommand toDadosFinanceiros(OrdemServico domain) {
        return new DadosFinanceirosCommand(
                toServicosAdicionadosCommand(domain.getServicos()),
                toPecasInsumosAdicionadosCommand(domain.getItensEstoque()),
                domain.getValorServicos(),
                domain.getValorPecasInsumos(),
                domain.getValorOs()
        );
    }

    private static List<PecasInsumosAdicionadosCommand> toPecasInsumosAdicionadosCommand(List<OrdemServicoItemEstoque> itensEstoque) {
        List<PecasInsumosAdicionadosCommand> commands = new ArrayList<>();
        for (OrdemServicoItemEstoque estoque: itensEstoque){
            commands.add(
                    new PecasInsumosAdicionadosCommand(
                          estoque.getNome(),
                          estoque.getValorUnitario(),
                          estoque.getQuantidade(),
                          estoque.getValorTotal()
                    )
            );
        }
        return commands;
    }

    private static List<ServicosAdicionadosCommand> toServicosAdicionadosCommand(List<OrdemServicoServico> servicos) {
        List<ServicosAdicionadosCommand> commands = new ArrayList<>();
        for (OrdemServicoServico servico : servicos){
            commands.add(
                    new ServicosAdicionadosCommand(
                            servico.getDescricao(),
                            servico.getValor()
                    )
            );
        }
        return commands;
    }
}