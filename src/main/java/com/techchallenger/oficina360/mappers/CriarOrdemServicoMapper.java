package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.mascararPlaca;

public class CriarOrdemServicoMapper {

    private CriarOrdemServicoMapper() {
    }

    public static CriarOrdemServicoDTO toDTO(OrdemServico ordemServico) {
        if (ordemServico == null) {
            return null;
        }

        return new CriarOrdemServicoDTO(
                ordemServico.getId(),
                ordemServico.getDocumentoCliente(),
                mascararPlaca(ordemServico.getPlacaVeiculo()),
                ordemServico.getDescricaoProblema(),
                ordemServico.getOrdemDeServicoStatus()
        );
    }

    public static OrdemServicoEntity toEntity(CriarOrdemServicoDTO criarOrdemServicoDTO) {
        if (criarOrdemServicoDTO == null) {
            return null;
        }

        return OrdemServicoEntity.builder()
                .id(criarOrdemServicoDTO.id())
                .documentoCliente(criarOrdemServicoDTO.documentoCliente())
                .placaVeiculo(criarOrdemServicoDTO.placaVeiculo())
                .descricaoProblema(criarOrdemServicoDTO.descricaoProblema())
                .ordemDeServicoStatus(criarOrdemServicoDTO.ordemDeServicoStatus())
                .build();
    }

    public static void updateEntityFromDto(
            CriarOrdemServicoDTO criarOrdemServicoDTO,
            OrdemServicoEntity ordemServicoEntity
    ) {
        if (criarOrdemServicoDTO == null || ordemServicoEntity == null) {
            return;
        }

        ordemServicoEntity.setDocumentoCliente(criarOrdemServicoDTO.documentoCliente());
        ordemServicoEntity.setPlacaVeiculo(criarOrdemServicoDTO.placaVeiculo());
        ordemServicoEntity.setDescricaoProblema(criarOrdemServicoDTO.descricaoProblema());
    }
}