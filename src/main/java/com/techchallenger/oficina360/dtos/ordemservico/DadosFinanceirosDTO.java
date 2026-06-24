package com.techchallenger.oficina360.dtos.ordemservico;

import com.techchallenger.oficina360.dtos.ordemservico.detalhes.PecasInsumosAdicionadosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.ServicosAdicionadosDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(
        name = "DadosFinanceirosDTO",
        description = "DTO utilizado para apresentar os dados financeiros da ordem de serviço, incluindo serviços, peças/insumos e valor total."
)
public record DadosFinanceirosDTO(

        @Schema(
                description = "Lista de serviços adicionados à ordem de serviço durante o diagnóstico.",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        List<ServicosAdicionadosDTO> servicos,

        @Schema(
                description = "Lista de peças ou insumos adicionados à ordem de serviço durante o diagnóstico.",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        List<PecasInsumosAdicionadosDTO> pecasInsumos,
        @Schema(
                description = "Valor total dos serviços da ordem de serviço.",
                example = "300.00",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        BigDecimal valorServicos,
        @Schema(
                description = "Valor total dos peças e insumos da ordem de serviço.",
                example = "190.00",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        BigDecimal valorPecasInsumos,
        @Schema(
                description = "Valor total da ordem de serviço, considerando serviços, peças e insumos.",
                example = "490.00",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        BigDecimal valorTotal
) {
}