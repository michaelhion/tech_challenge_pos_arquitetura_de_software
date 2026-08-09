package com.techchallenger.oficina360.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class FormataDadosUtilsTest {

	@Test
	void deveMascararCpfMantendoUltimosQuatroDigitos() {
		String resultado = FormataDadosUtils.mascararDocumento("12345678901");

		assertEquals("***8901", resultado);
	}

	@Test
	void deveMascararCnpjMantendoUltimosQuatroDigitos() {
		String resultado = FormataDadosUtils.mascararDocumento("11222333000181");

		assertEquals("***0181", resultado);
	}

	@Test
	void deveMascararDocumentoComExatamenteQuatroCaracteres() {
		String resultado = FormataDadosUtils.mascararDocumento("1234");

		assertEquals("***1234", resultado);
	}

	@Test
	void deveRetornarMascaraQuandoDocumentoForNulo() {
		String resultado = FormataDadosUtils.mascararDocumento(null);

		assertEquals("***", resultado);
	}

	@Test
	void deveRetornarMascaraQuandoDocumentoPossuirMenosDeQuatroCaracteres() {
		assertAll(() -> assertEquals("***", FormataDadosUtils.mascararDocumento("")),
				() -> assertEquals("***", FormataDadosUtils.mascararDocumento("1")),
				() -> assertEquals("***", FormataDadosUtils.mascararDocumento("123")));
	}

	@Test
	void deveNormalizarCpfRemovendoFormatacao() {
		String resultado = FormataDadosUtils.normalizarDocumento("123.456.789-01");

		assertEquals("12345678901", resultado);
	}

	@Test
	void deveNormalizarCnpjRemovendoFormatacao() {
		String resultado = FormataDadosUtils.normalizarDocumento("11.222.333/0001-81");

		assertEquals("11222333000181", resultado);
	}

	@Test
	void deveRemoverEspacosExternosDoDocumento() {
		String resultado = FormataDadosUtils.normalizarDocumento("  12345678901  ");

		assertEquals("12345678901", resultado);
	}

	@Test
	void deveRemoverTodosOsCaracteresNaoNumericosDoDocumento() {
		String resultado = FormataDadosUtils.normalizarDocumento("CPF: 123.456.789-01");

		assertEquals("12345678901", resultado);
	}

	@Test
	void deveRetornarTextoVazioQuandoDocumentoNaoPossuirNumeros() {
		String resultado = FormataDadosUtils.normalizarDocumento("documento");

		assertEquals("", resultado);
	}

	@Test
	void deveRetornarNullAoNormalizarDocumentoNulo() {
		String resultado = FormataDadosUtils.normalizarDocumento(null);

		assertNull(resultado);
	}

	@Test
	void deveNormalizarPlacaConvertendoParaMaiusculas() {
		String resultado = FormataDadosUtils.normalizarPlaca("abc1d23");

		assertEquals("ABC1D23", resultado);
	}

	@Test
	void deveNormalizarPlacaRemovendoHifen() {
		String resultado = FormataDadosUtils.normalizarPlaca("ABC-1D23");

		assertEquals("ABC1D23", resultado);
	}

	@Test
	void deveNormalizarPlacaRemovendoEspacos() {
		String resultado = FormataDadosUtils.normalizarPlaca(" AB C1 D23 ");

		assertEquals("ABC1D23", resultado);
	}

	@Test
	void deveNormalizarPlacaComHifenEspacosEMinusculas() {
		String resultado = FormataDadosUtils.normalizarPlaca(" abc-1d23 ");

		assertEquals("ABC1D23", resultado);
	}

	@Test
	void deveRetornarNullAoNormalizarPlacaNula() {
		String resultado = FormataDadosUtils.normalizarPlaca(null);

		assertNull(resultado);
	}

	@Test
	void deveMascararPlacaMantendoInicioEFim() {
		String resultado = FormataDadosUtils.mascararPlaca("ABC1D23");

		assertEquals("ABC***23", resultado);
	}

	@Test
	void deveNormalizarPlacaAntesDeMascarar() {
		String resultado = FormataDadosUtils.mascararPlaca(" abc-1d23 ");

		assertEquals("ABC***23", resultado);
	}

	@Test
	void deveRetornarNullAoMascararPlacaNula() {
		String resultado = FormataDadosUtils.mascararPlaca(null);

		assertNull(resultado);
	}

	@Test
	void deveRetornarTextoEmBrancoAoMascararPlacaEmBranco() {
		String placaEmBranco = "   ";

		String resultado = FormataDadosUtils.mascararPlaca(placaEmBranco);

		assertEquals(placaEmBranco, resultado);
	}

	@Test
	void deveRetornarTextoVazioAoMascararPlacaVazia() {
		String resultado = FormataDadosUtils.mascararPlaca("");

		assertEquals("", resultado);
	}

	@Test
	void deveRetornarMascaraQuandoPlacaNormalizadaPossuirMenosDeCincoCaracteres() {
		assertAll(() -> assertEquals("***", FormataDadosUtils.mascararPlaca("ABC1")),
				() -> assertEquals("***", FormataDadosUtils.mascararPlaca("A-B C")));
	}

	@Test
	void deveMascararPlacaComExatamenteCincoCaracteres() {
		String resultado = FormataDadosUtils.mascararPlaca("ABC12");

		assertEquals("ABC***12", resultado);
	}

	@Test
	void devePossuirConstrutorPrivado() throws Exception {
		Constructor<FormataDadosUtils> constructor = FormataDadosUtils.class.getDeclaredConstructor();

		assertTrue(Modifier.isPrivate(constructor.getModifiers()));

		constructor.setAccessible(true);

		FormataDadosUtils instancia = constructor.newInstance();

		assertNotNull(instancia);
	}
}