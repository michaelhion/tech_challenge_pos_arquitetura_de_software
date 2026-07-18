package com.techchallenger.oficina360.it.fixtures.ordemservico;

import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import java.math.BigDecimal;

public final class ServicoFixture {

	public static ServicoDTO criarValido() {
		return new ServicoDTO(
				"SRV-TROCA-OLEO",
				"Troca de óleo e filtro de motor",
				new BigDecimal("150.00"),
				null
		);
	}

	public static ServicoDTO criarComValorInvalido() {
		return new ServicoDTO(
				"SRV-ALINHAMENTO",
				"Alinhamento 3D",
				new BigDecimal("0.00"), // Vai falhar na validação @DecimalMin
				45
		);
	}
}
