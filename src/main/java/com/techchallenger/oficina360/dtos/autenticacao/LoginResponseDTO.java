package com.techchallenger.oficina360.dtos.autenticacao;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "LoginResponseDTO",
        description = "DTO retornado após autenticação bem-sucedida."
)
public record LoginResponseDTO(

        @Schema(
                description = "Token JWT que deve ser enviado no header Authorization.",
                example = "eyJhbGciOiJIUzI1NiJ9..."
        )
        String token,

        @Schema(
                description = "Tipo do token.",
                example = "Bearer"
        )
        String tipo
) {
}