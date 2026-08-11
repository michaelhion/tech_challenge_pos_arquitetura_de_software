package com.techchallenger.oficina360.usecases.ordemservico.query;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.shared.paginacao.DirecaoOrdenacao;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ListarOrdensServicoQueryTest {

	@Test
	void deveCriarQueryComDadosValidos() {

		ListarOrdensServicoQuery query = new ListarOrdensServicoQuery(OrdemDeServicoStatus.RECEBIDA, "12345678901",
				"ABC1234", LocalDateTime.now().minusDays(10), LocalDateTime.now(), BigDecimal.TEN,
				BigDecimal.valueOf(100), 0, 10, OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.DESC);

		assertEquals(0, query.pagina());
		assertEquals(10, query.tamanho());
		assertEquals(OrdemServicoOrdenacao.DATA_ABERTURA, query.ordenarPor());
		assertEquals(DirecaoOrdenacao.DESC, query.direcao());
	}

	@Test
	void deveLancarExcecaoQuandoPaginaForNegativa() {

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new ListarOrdensServicoQuery(null, null, null, null, null, null, null, -1, 10,
						OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC));

		assertEquals("A página não pode ser negativa.", exception.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoTamanhoForMenorQueUm() {

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new ListarOrdensServicoQuery(null, null, null, null, null, null, null, 0, 0,
						OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC));

		assertEquals("O tamanho da página deve estar entre 1 e 100.", exception.getMessage());
	}

	@Test
	void deveLancarExcecaoQuandoTamanhoForMaiorQueCem() {

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new ListarOrdensServicoQuery(null, null, null, null, null, null, null, 0, 101,
						OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC));

		assertEquals("O tamanho da página deve estar entre 1 e 100.", exception.getMessage());
	}

	@Test
	void deveAplicarOrdenacaoPadraoQuandoOrdenacaoForNula() {

		ListarOrdensServicoQuery query = new ListarOrdensServicoQuery(null, null, null, null, null, null, null, 0, 10,
				null, DirecaoOrdenacao.DESC);

		assertEquals(OrdemServicoOrdenacao.DATA_ABERTURA, query.ordenarPor());
	}

	@Test
	void deveAplicarDirecaoPadraoQuandoDirecaoForNula() {

		ListarOrdensServicoQuery query = new ListarOrdensServicoQuery(null, null, null, null, null, null, null, 0, 10,
				OrdemServicoOrdenacao.DATA_ABERTURA, null);

		assertEquals(DirecaoOrdenacao.ASC, query.direcao());
	}

	@Test
	void deveManterValoresInformadosParaOrdenacaoEDirecao() {

		ListarOrdensServicoQuery query = new ListarOrdensServicoQuery(null, null, null, null, null, null, null, 0, 10,
				OrdemServicoOrdenacao.VALOR_TOTAL, DirecaoOrdenacao.DESC);

		assertEquals(OrdemServicoOrdenacao.VALOR_TOTAL, query.ordenarPor());

		assertEquals(DirecaoOrdenacao.DESC, query.direcao());
	}
}