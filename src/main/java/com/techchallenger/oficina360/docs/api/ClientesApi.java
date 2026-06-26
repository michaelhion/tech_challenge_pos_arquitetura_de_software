package com.techchallenger.oficina360.docs.api;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
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

import static com.techchallenger.oficina360.docs.ClienteSwaggerConstants.*;
import static com.techchallenger.oficina360.docs.SwaggerConstants.*;

@Tag(
        name = TAG_NAME,
        description = TAG_DESCRIPTION
)
public interface ClientesApi {

    @Operation(
            summary = SUMMARY_BUSCAR_POR_DOCUMENTO,
            description = DESCRIPTION_BUSCAR_POR_DOCUMENTO
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_CLIENTE_ENCONTRADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = ClienteDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_CLIENTE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_DOCUMENTO,
                    content = @Content
            )
    })
    ResponseEntity<ClienteDTO> buscarPorDocumento(
            @Parameter(
                    description = PARAM_DOCUMENTO_DESCRIPTION,
                    example = EXAMPLE_DOCUMENTO,
                    required = true
            )
            String documento
    );

    @Operation(
            summary = SUMMARY_SALVAR,
            description = DESCRIPTION_SALVAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_CREATED,
                    description = RESPONSE_CLIENTE_CADASTRADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = ClienteDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_SALVAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_CLIENTE_CONFLICT,
                    content = @Content
            )
    })
    ResponseEntity<ClienteDTO> salvar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_SALVAR,
                    required = true,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = ClienteDTO.class)
                    )
            )
            ClienteDTO cliente
    );

    @Operation(
            summary = SUMMARY_EDITAR,
            description = DESCRIPTION_EDITAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_CLIENTE_ATUALIZADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = ClienteDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_EDITAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_CLIENTE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_CLIENTE_CONFLICT_EDITAR,
                    content = @Content
            )
    })
    ResponseEntity<ClienteDTO> editar(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_DOCUMENTO,
                    required = true
            )
            String documento,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_EDITAR,
                    required = true,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = ClienteDTO.class)
                    )
            )
            ClienteDTO cliente
    );

    @Operation(
            summary = SUMMARY_DELETAR,
            description = DESCRIPTION_DELETAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_NO_CONTENT,
                    description = RESPONSE_CLIENTE_EXCLUIDO,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_DOCUMENTO,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_CLIENTE_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_CLIENTE_CONFLICT_VINCULOS,
                    content = @Content
            )
    })
    ResponseEntity<Void> deletar(
            @Parameter(
                    description = PARAM_DOCUMENTO_DELETE_DESCRIPTION,
                    example = EXAMPLE_DOCUMENTO,
                    required = true
            )
            String documento
    );

    @Operation(
            summary = SUMMARY_LISTAR,
            description = DESCRIPTION_LISTAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_LISTA_CLIENTES,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = ClienteDTO.class)
                    )
            )
    })
    ResponseEntity<List<ClienteDTO>> listarClientes();
}
