package com.techchallenger.oficina360.docs.api;

import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
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

import static com.techchallenger.oficina360.docs.OrdemServicoSwaggerConstants.*;
import static com.techchallenger.oficina360.docs.SwaggerConstants.*;

@Tag(
        name = TAG_NAME,
        description = TAG_DESCRIPTION
)
public interface OrdemServicoOficinaApi {

    @Operation(
            summary = SUMMARY_LISTAR,
            description = DESCRIPTION_LISTAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_LISTA_ORDENS_SERVICO,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = OrdemServicoDTO.class)
                    )
            )
    })
    ResponseEntity<List<OrdemServicoDTO>> listarOrdensServico();

    @Operation(
            summary = SUMMARY_BUSCAR_POR_ID,
            description = DESCRIPTION_BUSCAR_POR_ID
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_ORDEM_SERVICO_ENCONTRADA,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = OrdemServicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_ID,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ORDEM_SERVICO_NOT_FOUND,
                    content = @Content
            )
    })
    ResponseEntity<OrdemServicoDTO> buscarPorId(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            UUID id
    );

    @Operation(
            summary = SUMMARY_SALVAR,
            description = DESCRIPTION_SALVAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_CREATED,
                    description = RESPONSE_ORDEM_SERVICO_CADASTRADA,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = CriarOrdemServicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_SALVAR,
                    content = @Content
            )
    })
    ResponseEntity<CriarOrdemServicoDTO> salvar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_SALVAR,
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CriarOrdemServicoDTO.class)
                    )
            )
            CriarOrdemServicoDTO criarOrdemServicoDTO
    );

    @Operation(
            summary = SUMMARY_EDITAR,
            description = DESCRIPTION_EDITAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_ORDEM_SERVICO_ATUALIZADA,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = OrdemServicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_EDITAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ORDEM_SERVICO_NOT_FOUND,
                    content = @Content
            )
    })
    ResponseEntity<OrdemServicoDTO> editar(
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
                            schema = @Schema(implementation = OrdemServicoDTO.class)
                    )
            )
            OrdemServicoDTO ordemServicoDTO
    );

    @Operation(
            summary = SUMMARY_DELETAR,
            description = DESCRIPTION_DELETAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_NO_CONTENT,
                    description = RESPONSE_ORDEM_SERVICO_EXCLUIDA,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_ID,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ORDEM_SERVICO_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_ORDEM_SERVICO_CONFLICT_VINCULO,
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
            summary = SUMMARY_DIAGNOSTICAR,
            description = DESCRIPTION_DIAGNOSTICAR
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = RESPONSE_ORDEM_SERVICO_DIAGNOSTICADA,
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = OrdemServicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_DIAGNOSTICAR,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ORDEM_SERVICO_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = DESCRIPTION_ORDEM_SERVICO_CONFLICT_DIAGNOSTICAR,
                    content = @Content
            )
    })
    ResponseEntity<OrdemServicoDTO> diagnosticar(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_DIAGNOSTICAR,
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = DiagnosticoDTO.class)
                    )
            )
            DiagnosticoDTO diagnosticoDTO
    );

    @Operation(
            summary = "Iniciar execução da ordem de serviço",
            description = "Registra o início da execução da ordem de serviço. A ação só é permitida quando o orçamento estiver aprovado."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = "Execução iniciada com sucesso.",
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = OrdemServicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_ID,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ORDEM_SERVICO_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = "A ordem de serviço não está em um status que permita iniciar execução.",
                    content = @Content
            )
    })
    ResponseEntity<OrdemServicoDTO> iniciarExecucao(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            UUID id
    );

    @Operation(
            summary = "Finalizar execução da ordem de serviço",
            description = "Registra o fim da execução da ordem de serviço e calcula o tempo total a partir da data/hora de início."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = STATUS_CODE_OK,
                    description = "Execução finalizada com sucesso.",
                    content = @Content(
                            mediaType = MEDIA_TYPE_JSON,
                            schema = @Schema(implementation = OrdemServicoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_BAD_REQUEST,
                    description = DESCRIPTION_BAD_REQUEST_ID,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_NOT_FOUND,
                    description = DESCRIPTION_ORDEM_SERVICO_NOT_FOUND,
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = STATUS_CODE_CONFLICT,
                    description = "A ordem de serviço não está em execução.",
                    content = @Content
            )
    })
    ResponseEntity<OrdemServicoDTO> finalizarExecucao(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            UUID id
    );
}