package com.techchallenger.oficina360.dtos.ordemservico.detalhes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        name = "PecasInsumosAdicionadosDTO",
        description = "DTO utilizado para representar uma peça ou insumo adicionado à ordem de serviço."
)
public record PecasInsumosAdicionadosDTO(

        @Schema(
                description = "Nome da peça ou insumo adicionado à ordem de serviço.",
                example = "Filtro de óleo",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        String nome,

        @Schema(
                description = "Valor unitário da peça ou insumo no momento em que foi adicionado à ordem de serviço.",
                example = "45.90",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        BigDecimal valorUnitario,

        @Schema(
                description = "Quantidade da peça ou insumo adicionada à ordem de serviço.",
                example = "2",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer quantidade,
        @Schema(
                description = "Valor total da peça ou insumo no momento em que foi adicionado à ordem de serviço.",
                example = "91.80",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        BigDecimal valorTotal
) {
}