package com.techchallenger.oficina360.dtos.ordemservico.detalhes;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(
        name = "ServicosAdicionadosDTO",
        description = "DTO utilizado para representar um serviço adicionado à ordem de serviço."
)
public record ServicosAdicionadosDTO(

        @Schema(
                description = "Nome ou descrição do serviço adicionado à ordem de serviço.",
                example = "Troca de óleo",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        String nome,

        @Schema(
                description = "Valor do serviço no momento em que foi adicionado à ordem de serviço.",
                example = "150.00",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        BigDecimal valor
) {
}