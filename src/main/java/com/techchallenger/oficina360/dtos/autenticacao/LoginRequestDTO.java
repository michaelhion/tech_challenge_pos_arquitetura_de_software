package com.techchallenger.oficina360.dtos.autenticacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "LoginRequestDTO",
        description = "DTO utilizado para autenticação de usuários."
)
public record LoginRequestDTO(

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
        String senha
) {
}