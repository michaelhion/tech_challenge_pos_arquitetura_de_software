package com.techchallenger.oficina360.dtos.servicos;

import com.techchallenger.oficina360.validators.CodigoValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(
        name = "ServicoDTO",
        description = "DTO utilizado para cadastro e atualização dos serviços oferecidos pela oficina."
)
public record ServicoDTO(

        @Schema(
                description = "Código funcional do serviço. Deve conter apenas letras maiúsculas, números, hífen ou underline, sem espaços ou acentos.",
                example = "SRV-TROCA-OLEO",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O código do serviço é obrigatório")
        @CodigoValido
        String codigo,

        @Schema(
                description = "Descrição do serviço oferecido pela oficina.",
                example = "Troca de óleo",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A descrição do serviço é obrigatória")
        String descricao,

        @Schema(
                description = "Valor cobrado pelo serviço.",
                example = "150.00",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O valor do serviço é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor do serviço deve ser maior que zero")
        BigDecimal valor
) {
}