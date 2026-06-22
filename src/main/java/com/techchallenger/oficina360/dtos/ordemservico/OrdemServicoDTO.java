package com.techchallenger.oficina360.dtos.ordemservico;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Schema(
        name = "OrdemServicoDTO",
        description = "DTO utilizado para criação, consulta e atualização de ordens de serviço da oficina."
)
public record OrdemServicoDTO(

        @Schema(
                description = "Identificador da ordem de serviço.",
                example = "7b5a3247-a14a-44f8-872f-016e179a92fd",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        UUID id,

        @Schema(
                description = "CPF ou CNPJ do cliente responsável pela ordem de serviço. Deve ser informado somente com números.",
                example = "12345678901",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "O documento do cliente é obrigatório")
        @Pattern(
                regexp = "\\d{11}|\\d{14}",
                message = "O documento deve conter 11 dígitos para CPF ou 14 dígitos para CNPJ"
        )
        String documentoCliente,

        @Schema(
                description = "Placa do veículo vinculado à ordem de serviço. Aceita padrão antigo ABC1234 ou Mercosul ABC1D23, sem hífen.",
                example = "ABC1D23",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A placa do veículo é obrigatória")
        @Pattern(
                regexp = "^[A-Z]{3}\\d[A-Z0-9]\\d{2}$",
                message = "A placa deve estar no padrão ABC1234 ou ABC1D23, sem hífen"
        )
        String placaVeiculo,

        @Schema(
                description = "Descrição do problema relatado pelo cliente ou observado no atendimento inicial.",
                example = "Veículo apresenta ruído ao frear e vibração no volante.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "A descrição do problema é obrigatória")
        String descricaoProblema,

        @Schema(
                description = "Status atual da ordem de serviço. Este campo representa a etapa atual do fluxo da OS.",
                example = "RECEBIDA",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        OrdemDeServicoStatus ordemDeServicoStatus,

        @Schema(
                description = "Dados financeiros da ordem de serviço, incluindo serviços adicionados, peças/insumos e valor total.",
                accessMode = Schema.AccessMode.READ_ONLY
        )
        DadosFinanceirosDTO dadosFinanceiros
) {
}