package com.techchallenger.oficina360.docs.api;

import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.techchallenger.oficina360.docs.SwaggerConstants.*;
import static com.techchallenger.oficina360.docs.VeiculoSwaggerConstants.*;

@Tag(
        name = TAG_NAME,
        description = TAG_DESCRIPTION
)
public interface VeiculosApi {

    @Operation(
            summary = SUMMARY_BUSCAR_POR_PLACA,
            description = DESCRIPTION_BUSCAR_POR_PLACA
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_VEICULO_ENCONTRADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = VeiculoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_PLACA,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_VEICULO_NOT_FOUND,
                    content = @Content
            )
    })
    ResponseEntity<VeiculoDTO> buscarPorPlaca(
            @Parameter(
                    description = PARAM_PLACA_DESCRIPTION,
                    example = EXAMPLE_PLACA_MERCOSUL,
                    required = true
            )
            String placa
    );

    @Operation(
            summary = SUMMARY_SALVAR,
            description = DESCRIPTION_SALVAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_CREATED,
                    description = RESPONSE_VEICULO_CADASTRADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = VeiculoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_SALVAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_VEICULO_CONFLICT,
                    content = @Content
            )
    })
    ResponseEntity<VeiculoDTO> salvar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_SALVAR,
                    required = true,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = VeiculoDTO.class)
                    )
            )
            VeiculoDTO veiculoDTO
    );

    @Operation(
            summary = SUMMARY_EDITAR_POR_PLACA,
            description = DESCRIPTION_EDITAR_POR_PLACA
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_VEICULO_ATUALIZADO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = VeiculoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_EDITAR_POR_PLACA,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_VEICULO_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_VEICULO_CONFLICT_EDITAR,
                    content = @Content
            )
    })
    ResponseEntity<VeiculoDTO> editar(
            @Parameter(
                    description = PARAM_PLACA_ATUAL_DESCRIPTION,
                    example = EXAMPLE_PLACA_MERCOSUL,
                    required = true
            )
            String placa,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_EDITAR,
                    required = true,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = VeiculoDTO.class)
                    )
            )
            VeiculoDTO veiculoDTO
    );

    @Operation(
            summary = SUMMARY_DELETAR_POR_PLACA,
            description = DESCRIPTION_DELETAR_POR_PLACA
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_NO_CONTENT,
                    description = RESPONSE_VEICULO_EXCLUIDO,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_PLACA,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_VEICULO_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_VEICULO_CONFLICT_VINCULO,
                    content = @Content
            )
    })
    ResponseEntity<Void> deletar(
            @Parameter(
                    description = PARAM_PLACA_DELETE_DESCRIPTION,
                    example = EXAMPLE_PLACA_MERCOSUL,
                    required = true
            )
            String placa
    );

    @Operation(
            summary = SUMMARY_LISTAR,
            description = DESCRIPTION_LISTAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_LISTA_VEICULOS,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = VeiculoDTO.class)
                    )
            )
    })
    ResponseEntity<List<VeiculoDTO>> listarVeiculos();
}

