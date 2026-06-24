package com.techchallenger.oficina360.dtos.ordemservico.diagnostico;

import com.techchallenger.oficina360.dtos.validators.CodigoPattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "DiagnosticoEstoqueDTO",
        description = "DTO utilizado para informar uma peça ou insumo no diagnóstico."
)
public record DiagnosticoEstoqueDTO(

        @Schema(
                description = "Código funcional do item de estoque. Deve seguir o padrão sem espaços, usando hífen ou underline.",
                example = "EST-FILTRO-OLEO",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O código do item de estoque é obrigatório")
        @Pattern(
                regexp = CodigoPattern.REGEX,
                message = CodigoPattern.MESSAGE
        )
        String codigo,

        @Schema(
                description = "Quantidade necessária do item de estoque.",
                example = "2",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {
}