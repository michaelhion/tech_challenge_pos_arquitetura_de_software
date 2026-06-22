package com.techchallenger.oficina360.dtos.clientes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "ClienteDTO",
        description = "DTO utilizado para cadastro e atualização de clientes da oficina. " +
                "Contém os dados necessários para identificação do cliente por CPF ou CNPJ."
)
public record ClienteDTO(

        @Schema(
                description = "CPF ou CNPJ do cliente. Deve ser informado somente com números, sem pontos, barras ou hífen.",
                example = "12345678901",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O documento é obrigatório")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "O documento deve conter 11 dígitos para CPF ou 14 dígitos para CNPJ, sem pontuação"
        )
        String documento,

        @Schema(
                description = "Nome completo do cliente ou razão social da empresa.",
                example = "João da Silva",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(
                description = "E-mail de contato do cliente.",
                example = "cliente@email.com"
        )
        @Email(message = "E-mail inválido")
        String email,

        @Schema(
                description = "Telefone de contato do cliente. Preferencialmente com DDD e somente números.",
                example = "11999999999",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(
                regexp = "\\d{10,11}",
                message = "O telefone deve conter 10 ou 11 dígitos"
        )
        String telefone
) {
}