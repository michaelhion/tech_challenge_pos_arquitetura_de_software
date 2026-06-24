package com.techchallenger.oficina360.dtos.autenticacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(
        name = "LoginRequestDTO",
        description = "DTO utilizado para autenticação de usuários."
)
public record CriarUsuarioRequestDTO(

        @Schema(
                description = "E-mail do usuário.",
                example = "admin@oficina360.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @Schema(
                description = "Senha do usuário.",
                example = "123456",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A senha é obrigatória")
        String senha,
        @Schema(
                description = "CPF ou CNPJ do cliente. Deve ser informado somente com números, sem pontos, barras ou hífen.",
                example = "12345678901",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O documento é obrigatório")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "O documento deve conter 11 dígitos para CPF ou 14 dígitos para CNPJ"
        )
        String documento,
        @Schema(
                description = "Tipo de usuário do sistema como ADMIN, CLIENTE,MECANICO.",
                example = "CLIENTE",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Role é obrigatório")
        String role
) {
}