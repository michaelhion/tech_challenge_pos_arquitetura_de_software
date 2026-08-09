package com.techchallenger.oficina360.usecases.ordemservico.output;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrdemServicoResumoOutput(
		UUID id,

		String documentoCliente,

		String placaVeiculo,

		String descricaoProblema,

		OrdemDeServicoStatus status,

		LocalDateTime dataHoraAbertura,

		BigDecimal valorServicos,

		BigDecimal valorPecasInsumos,

		BigDecimal valorTotal

) {
}