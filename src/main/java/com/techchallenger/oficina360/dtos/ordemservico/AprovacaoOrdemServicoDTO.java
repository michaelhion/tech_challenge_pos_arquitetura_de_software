package com.techchallenger.oficina360.dtos.ordemservico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "AprovacaoOrdemServicoDTO",
        description = "DTO utilizado para aprovar ou reprovar o orçamento de uma ordem de serviço."
)
public record AprovacaoOrdemServicoDTO(

        @Schema(
                description = "Indica se o orçamento foi aprovado pelo cliente.",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O campo aprovado é obrigatório")
        Boolean aprovado,
        @Schema(
                description = "Texto livre e opcional para explicar o motivo da recusa.",
                example = "O cliente discordou de algum serviço",
                requiredMode = Schema.RequiredMode.REQUIRED,
                maxLength = 200
        )
        String observacao

) {
}