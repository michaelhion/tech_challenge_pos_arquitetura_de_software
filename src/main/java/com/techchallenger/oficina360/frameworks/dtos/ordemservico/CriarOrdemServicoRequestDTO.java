package com.techchallenger.oficina360.frameworks.dtos.ordemservico;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "OrdemServicoDTO",
        description = "DTO utilizado para criação, consulta e atualização de ordens de serviço da oficina."
)
public record CriarOrdemServicoRequestDTO(
        @Schema(
                description = "CPF ou CNPJ do cliente responsável pela ordem de serviço. Deve ser informado somente com números.",
                example = "12345678901",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O documento do cliente é obrigatório")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "O documento deve conter 11 dígitos para CPF ou 14 dígitos para CNPJ"
        )
        String documentoCliente,

        @Schema(
                description = "Placa do veículo vinculado à ordem de serviço. Aceita padrão antigo ABC1234 ou Mercosul ABC1D23, sem hífen.",
                example = "ABC1D23",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A placa do veículo é obrigatória")
        @Pattern(
                regexp = "^[A-Z]{3}\\d[A-Z0-9]\\d{2}$",
                message = "A placa deve estar no padrão ABC1234 ou ABC1D23, sem hífen"
        )
        String placaVeiculo,

        @Schema(
                description = "Descrição do problema relatado pelo cliente ou observado no atendimento inicial.",
                example = "Veículo apresenta ruído ao frear e vibração no volante.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A descrição do problema é obrigatória")
        String descricaoProblema
) {
}