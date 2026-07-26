package com.techchallenger.oficina360.usecases.shared.paginacao;

import java.util.List;
import java.util.function.Function;

public record ResultadoPaginado<T>(

		List<T> conteudo,

		int pagina,

		int tamanho,

		long totalElementos,

		int totalPaginas,

		boolean primeiraPagina,

		boolean ultimaPagina,

		boolean possuiProximaPagina

) {

	public <R> ResultadoPaginado<R> map(Function<T, R> mapper) {
		List<R> novoConteudo = conteudo.stream().map(mapper).toList();

		return new ResultadoPaginado<>(novoConteudo, pagina, tamanho, totalElementos, totalPaginas, primeiraPagina,
				ultimaPagina, possuiProximaPagina);
	}
}
