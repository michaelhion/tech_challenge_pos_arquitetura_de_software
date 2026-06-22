package com.techchallenger.oficina360.docs.api;

import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

import static com.techchallenger.oficina360.docs.OrdemServicoSwaggerConstants.PARAM_ID_DESCRIPTION;
import static com.techchallenger.oficina360.docs.OrdemServicoSwaggerConstants.REQUEST_BODY_APROVAR;
import static com.techchallenger.oficina360.docs.SwaggerConstants.EXAMPLE_UUID;

@Tag(
        name = "7 - Ordem de serviços Cliente",
    description = "Endpoints de ordem de serviço que o usuario do tipo cliente tem acesso"
)
public interface OrdemServicoClienteApi {
    @GetMapping("/listar/{id}")
    ResponseEntity<OrdemServicoDTO> buscarPorId(
            @PathVariable UUID id
    );

    @PatchMapping("/aprovacao/{id}")
    ResponseEntity<OrdemServicoDTO> aprovar(
            @Parameter(
                    description = PARAM_ID_DESCRIPTION,
                    example = EXAMPLE_UUID,
                    required = true
            )
            @PathVariable UUID id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = REQUEST_BODY_APROVAR,
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AprovacaoOrdemServicoDTO.class)
                    )
            ) AprovacaoOrdemServicoDTO aprovacaoDTO
    );

}
