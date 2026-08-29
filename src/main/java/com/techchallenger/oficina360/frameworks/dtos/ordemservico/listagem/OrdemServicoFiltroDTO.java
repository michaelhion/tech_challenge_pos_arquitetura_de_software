package com.techchallenger.oficina360.frameworks.dtos.ordemservico.listagem;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdemServicoFiltroDTO(

		OrdemDeServicoStatus status,

		String documentoCliente,

		String placa,

		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		LocalDateTime aberturaInicial,

		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
		LocalDateTime aberturaFinal,

		BigDecimal valorMinimo,

		BigDecimal valorMaximo

) {
}