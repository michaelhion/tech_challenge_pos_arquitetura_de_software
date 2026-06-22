package com.techchallenger.oficina360.dtos.estoques;

import com.techchallenger.oficina360.validators.CodigoValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(
        name = "EstoqueDTO",
        description = "DTO utilizado para cadastro, atualização e consulta de itens de estoque da oficina."
)
public record EstoqueDTO(
        @Schema(
                description = "Identificador da ordem de serviço.",
                example = "7b5a3247-a14a-44f8-872f-016e179a92fd",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        UUID id,
        @Schema(
                description = "Código funcional do item de estoque. Deve conter apenas letras maiúsculas, números, hífen ou underline, sem espaços ou acentos.",
                example = "EST-FILTRO-OLEO",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O código do item de estoque é obrigatório")
        @CodigoValido
        String codigo,

        @Schema(
                description = "Nome da peça, insumo ou item de estoque.",
                example = "Filtro de óleo",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O nome do item é obrigatório")
        String nome,

        @Schema(
                description = "Valor unitário da peça ou insumo.",
                example = "45.90",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @Schema(
                description = "Quantidade total existente no estoque.",
                example = "20",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 0, message = "A quantidade não pode ser negativa")
        Integer quantidade,

        @Schema(
                description = "Quantidade reservada para ordens de serviço ainda não finalizadas.",
                example = "5",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "A quantidade reservada é obrigatória")
        @Min(value = 0, message = "A quantidade reservada não pode ser negativa")
        Integer reservados,

        @Schema(
                description = "Quantidade disponível para uso. Valor calculado com base em quantidade menos reservados.",
                example = "15",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        Integer disponiveis
) {
}