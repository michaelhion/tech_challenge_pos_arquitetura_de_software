package com.techchallenger.oficina360.docs.api;

import com.techchallenger.oficina360.dtos.autenticacao.CriarUsuarioRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginRequestDTO;
import com.techchallenger.oficina360.dtos.autenticacao.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static com.techchallenger.oficina360.docs.SwaggerConstants.MEDIA_TYPE_JSON;

@Tag(
        name = "1 - Autenticação",
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
    ResponseEntity<LoginResponseDTO> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(

            required = true,
            content = @Content(
                    mediaType = MEDIA_TYPE_JSON,
                    schema = @Schema(implementation = LoginRequestDTO.class)
            )
    )LoginRequestDTO loginRequestDTO);

    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso negado"
            )
    })
    ResponseEntity<String> criarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = CriarUsuarioRequestDTO.class)
                    )
            )
            CriarUsuarioRequestDTO criarUsuarioRequestDTO
    );


}