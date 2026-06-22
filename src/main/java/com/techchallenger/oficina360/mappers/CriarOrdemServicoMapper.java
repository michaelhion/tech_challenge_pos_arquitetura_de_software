package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.entities.OrdemServico;

public class CriarOrdemServicoMapper {

    private CriarOrdemServicoMapper() {
    }

    public static CriarOrdemServicoDTO toDTO(OrdemServico ordemServicoEntity) {
        if (ordemServicoEntity == null) {
            return null;
        }

        return new CriarOrdemServicoDTO(
                ordemServicoEntity.getId(),
                ordemServicoEntity.getDocumentoCliente(),
                ordemServicoEntity.getPlacaVeiculo(),
                ordemServicoEntity.getDescricaoProblema(),
                ordemServicoEntity.getOrdemDeServicoStatus()
        );
    }

    public static OrdemServico toEntity(CriarOrdemServicoDTO criarOrdemServicoDTO) {
        if (criarOrdemServicoDTO == null) {
            return null;
        }

        return OrdemServico.builder()
                .id(criarOrdemServicoDTO.id())
                .documentoCliente(criarOrdemServicoDTO.documentoCliente())
                .placaVeiculo(criarOrdemServicoDTO.placaVeiculo())
                .descricaoProblema(criarOrdemServicoDTO.descricaoProblema())
                .ordemDeServicoStatus(criarOrdemServicoDTO.ordemDeServicoStatus())
                .build();
    }

    public static void updateEntityFromDto(
            CriarOrdemServicoDTO criarOrdemServicoDTO,
            OrdemServico ordemServicoEntity
    ) {
        if (criarOrdemServicoDTO == null || ordemServicoEntity == null) {
            return;
        }

        ordemServicoEntity.setDocumentoCliente(criarOrdemServicoDTO.documentoCliente());
        ordemServicoEntity.setPlacaVeiculo(criarOrdemServicoDTO.placaVeiculo());
        ordemServicoEntity.setDescricaoProblema(criarOrdemServicoDTO.descricaoProblema());
    }
}