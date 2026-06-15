package com.techchallenger.oficina360.dtos.ordemservico.diagnostico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "ReservaEstoqueDTO",
        description = "DTO utilizado para reservar uma quantidade de um item de estoque."
)
public record ReservaEstoqueDTO(

        @Schema(
                description = "Quantidade que será reservada no estoque.",
                example = "3",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}