package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.ordemservico.DadosFinanceirosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.PecasInsumosAdicionadosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.ServicosAdicionadosDTO;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.entities.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.entities.OrdemServicoServico;

import java.util.List;

public class OrdemServicoMapper {

    private OrdemServicoMapper() {
    }

    public static OrdemServicoDTO toDTO(OrdemServico ordemServicoEntity) {
        if (ordemServicoEntity == null) {
            return null;
        }

        return new OrdemServicoDTO(
                ordemServicoEntity.getId(),
                ordemServicoEntity.getDocumentoCliente(),
                ordemServicoEntity.getPlacaVeiculo(),
                ordemServicoEntity.getDescricaoProblema(),
                ordemServicoEntity.getOrdemDeServicoStatus(),
                toDadosFinanceirosDTO(ordemServicoEntity)
        );
    }

    public static OrdemServico toEntity(OrdemServicoDTO ordemServicoDTO) {
        if (ordemServicoDTO == null) {
            return null;
        }

        return OrdemServico.builder()
                .id(ordemServicoDTO.id())
                .documentoCliente(ordemServicoDTO.documentoCliente())
                .placaVeiculo(ordemServicoDTO.placaVeiculo())
                .descricaoProblema(ordemServicoDTO.descricaoProblema())
                .ordemDeServicoStatus(ordemServicoDTO.ordemDeServicoStatus())
                .build();
    }

    public static void updateEntityFromDto(
            OrdemServicoDTO ordemServicoDTO,
            OrdemServico ordemServicoEntity
    ) {
        if (ordemServicoDTO == null || ordemServicoEntity == null) {
            return;
        }

        ordemServicoEntity.setDocumentoCliente(ordemServicoDTO.documentoCliente());
        ordemServicoEntity.setPlacaVeiculo(ordemServicoDTO.placaVeiculo());
        ordemServicoEntity.setDescricaoProblema(ordemServicoDTO.descricaoProblema());
    }

    private static DadosFinanceirosDTO toDadosFinanceirosDTO(OrdemServico ordemServicoEntity) {
        if (ordemServicoEntity == null) {
            return null;
        }

        return new DadosFinanceirosDTO(
                toServicosAdicionadosDTO(ordemServicoEntity.getServicos()),
                toPecasInsumosAdicionadosDTO(ordemServicoEntity.getItensEstoque()),
                ordemServicoEntity.getValorServicos(),
                ordemServicoEntity.getValorPecasInsumos(),
                ordemServicoEntity.getValorOs()
        );
    }

    private static List<ServicosAdicionadosDTO> toServicosAdicionadosDTO(
            List<OrdemServicoServico> servicos
    ) {
        if (servicos == null || servicos.isEmpty()) {
            return List.of();
        }

        return servicos.stream()
                .map(OrdemServicoMapper::toServicoAdicionadoDTO)
                .toList();
    }

    private static ServicosAdicionadosDTO toServicoAdicionadoDTO(
            OrdemServicoServico servico
    ) {
        if (servico == null) {
            return null;
        }

        return new ServicosAdicionadosDTO(
                servico.getDescricao(),
                servico.getValor()
        );
    }

    private static List<PecasInsumosAdicionadosDTO> toPecasInsumosAdicionadosDTO(
            List<OrdemServicoItemEstoque> itensEstoque
    ) {
        if (itensEstoque == null || itensEstoque.isEmpty()) {
            return List.of();
        }

        return itensEstoque.stream()
                .map(OrdemServicoMapper::toPecaInsumoAdicionadoDTO)
                .toList();
    }

    private static PecasInsumosAdicionadosDTO toPecaInsumoAdicionadoDTO(
            OrdemServicoItemEstoque itemEstoque
    ) {
        if (itemEstoque == null) {
            return null;
        }

        return new PecasInsumosAdicionadosDTO(
                itemEstoque.getNome(),
                itemEstoque.getValorUnitario(),
                itemEstoque.getQuantidade(),
                itemEstoque.getValorTotal()
        );
    }
}