package com.techchallenger.oficina360.docs.api;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static com.techchallenger.oficina360.docs.EstoqueSwaggerConstants.*;
import static com.techchallenger.oficina360.docs.SwaggerConstants.*;

@Tag(
        name = TAG_NAME,
        description = TAG_DESCRIPTION
)
public interface EstoqueApi {

    @Operation(
            summary = SUMMARY_BUSCAR_POR_CODIGO,
            description = DESCRIPTION_BUSCAR_POR_CODIGO
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_ESTOQUE_ENCONTRADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = EstoqueDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_ID,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ESTOQUE_NOT_FOUND,
                    content = @Content
            )
    })
    ResponseEntity<EstoqueDTO> buscarPorId(
            @Parameter(
                    description = PARAM_CODIGO_DESCRIPTION,
                    example = EXAMPLE_CODIGO,
                    required = true
            )
            String codigo
    );

    @Operation(
            summary = SUMMARY_SALVAR,
            description = DESCRIPTION_SALVAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_CREATED,
                    description = RESPONSE_ESTOQUE_CADASTRADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = EstoqueDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_SALVAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_ESTOQUE_CONFLICT,
                    content = @Content
            )
    })
    ResponseEntity<EstoqueDTO> salvar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_SALVAR,
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EstoqueDTO.class)
                    )
            )
            EstoqueDTO estoqueDTO
    );

    @Operation(
            summary = SUMMARY_EDITAR,
            description = DESCRIPTION_EDITAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_ESTOQUE_ATUALIZADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = EstoqueDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_EDITAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ESTOQUE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_ESTOQUE_CONFLICT_EDITAR,
                    content = @Content
            )
    })
    ResponseEntity<EstoqueDTO> editar(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_EDITAR,
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = EstoqueDTO.class)
                    )
            )
            EstoqueDTO estoqueDTO
    );

    @Operation(
            summary = SUMMARY_DELETAR,
            description = DESCRIPTION_DELETAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_NO_CONTENT,
                    description = RESPONSE_ESTOQUE_EXCLUIDO,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_ID,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ESTOQUE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_ESTOQUE_CONFLICT_VINCULO,
                    content = @Content
            )
    })
    ResponseEntity<Void> deletar(
            @Parameter(
                    description = PARAM_ID_DELETE_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            UUID id
    );

    @Operation(
            summary = SUMMARY_LISTAR,
            description = DESCRIPTION_LISTAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_LISTA_ESTOQUE,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = EstoqueDTO.class)
                    )
            )
    })
    ResponseEntity<List<EstoqueDTO>> listarEstoques();

    @Operation(
            summary = SUMMARY_RESERVAR,
            description = DESCRIPTION_RESERVAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_ESTOQUE_RESERVADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = EstoqueDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_RESERVAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ESTOQUE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_ESTOQUE_INDISPONIVEL,
                    content = @Content
            )
    })
    ResponseEntity<EstoqueDTO> reservar(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_RESERVAR,
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ReservaEstoqueDTO.class)
                    )
            )
            ReservaEstoqueDTO reservaDTO
    );
}



