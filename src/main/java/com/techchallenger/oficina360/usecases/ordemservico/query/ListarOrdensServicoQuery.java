package com.techchallenger.oficina360.usecases.ordemservico.query;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.shared.paginacao.DirecaoOrdenacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ListarOrdensServicoQuery(

		OrdemDeServicoStatus status,

		String documentoCliente,

		String placa,

		LocalDateTime aberturaInicial,

		LocalDateTime aberturaFinal,

		BigDecimal valorMinimo,

		BigDecimal valorMaximo,

		int pagina,

		int tamanho,

		OrdemServicoOrdenacao ordenarPor,

		DirecaoOrdenacao direcao

) {

	public ListarOrdensServicoQuery {
		if (pagina < 0) {
			throw new IllegalArgumentException("A página não pode ser negativa.");
		}

		if (tamanho < 1 || tamanho > 100) {
			throw new IllegalArgumentException("O tamanho da página deve estar entre 1 e 100.");
		}

		if (ordenarPor == null) {
			ordenarPor = OrdemServicoOrdenacao.DATA_ABERTURA;
		}

		if (direcao == null) {
			direcao = DirecaoOrdenacao.ASC;
		}
	}
}