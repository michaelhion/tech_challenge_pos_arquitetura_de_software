package com.techchallenger.oficina360.dtos.ordemservico.diagnostico;

import com.techchallenger.oficina360.dtos.validators.CodigoPattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Schema(
        name = "DiagnosticoDTO",
        description = "DTO utilizado para registrar o diagnóstico da ordem de serviço com serviços e itens de estoque."
)
public record DiagnosticoDTO(

        @Schema(
                description = "Lista de códigos dos serviços definidos no diagnóstico. Os códigos devem seguir o padrão sem espaços, usando hífen ou underline.",
                example = "[\"SRV-TROCA-OLEO\", \"SRV-ALINHAMENTO\"]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "Informe ao menos um serviço")
        List<
                @Pattern(
                        regexp = CodigoPattern.REGEX,
                        message = CodigoPattern.MESSAGE
                )
                        String
                > codigosServicos,

        @Schema(
                description = "Lista de peças ou insumos necessários para a ordem de serviço."
        )
        @Valid
        List<DiagnosticoEstoqueDTO> itensEstoque
) {
}