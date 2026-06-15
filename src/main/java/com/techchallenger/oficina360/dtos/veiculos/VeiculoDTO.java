package com.techchallenger.oficina360.dtos.veiculos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "VeiculoDTO",
        description = "DTO utilizado para cadastro e atualização de veículos vinculados a clientes da oficina."
)
public record VeiculoDTO(

        @Schema(
                description = "Placa do veículo sem hífen. Aceita padrão antigo ABC1234 ou Mercosul ABC1D23.",
                example = "ABC1D23",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A placa é obrigatória")
        @Pattern(
                regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$",
                message = "A placa deve estar no padrão ABC1234 ou ABC1D23, sem hífen"
        )
        String placa,

        @Schema(
                description = "Marca/fabricante do veículo.",
                example = "Volkswagen",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A marca é obrigatória")
        String marca,

        @Schema(
                description = "Modelo do veículo.",
                example = "Gol",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O modelo é obrigatório")
        String modelo,

        @Schema(
                description = "Ano de fabricação ou modelo do veículo.",
                example = "2020",
                minimum = "1900",
                maximum = "2100",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "O ano é obrigatório")
        @Min(value = 1900, message = "O ano deve ser maior ou igual a 1900")
        @Max(value = 2100, message = "O ano deve ser menor ou igual a 2100")
        Integer ano,

        @Schema(
                description = "CPF ou CNPJ do cliente responsável pelo veículo, somente números.",
                example = "12345678901",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O documento do cliente é obrigatório")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "O documento deve conter 11 dígitos para CPF ou 14 dígitos para CNPJ"
        )
        String clienteDocumento
) {
}