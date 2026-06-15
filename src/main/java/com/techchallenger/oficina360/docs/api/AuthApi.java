package com.techchallenger.oficina360.docs.api;

import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "0 - Autenticação",
        description = "Endpoints para autenticação e geração de token JWT."
)
public interface AuthApi {

    @Operation(
            summary = "Autenticar usuário",
            description = "Autentica um usuário administrativo e retorna um token JWT para acesso aos endpoints protegidos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário autenticado com sucesso.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados de login inválidos ou mal formatados.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciais inválidas.",
                    content = @Content
            )
    })
    ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO);
}