package com.techchallenger.oficina360.utils;

import org.junit.jupiter.api.Test;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FormataDadosUtilsTest {

    @Test
    void deveMascararDocumentoComSucesso() {

        String resultado = FormataDadosUtils.mascararDocumento("12345678901");

        assertEquals("***8901", resultado);
    }

    @Test
    void deveRetornarTresAsteriscosQuandoDocumentoForNulo() {

        String resultado = FormataDadosUtils.mascararDocumento(null);

        assertEquals("***", resultado);
    }

    @Test
    void deveRetornarTresAsteriscosQuandoDocumentoTiverMenosDeQuatroCaracteres() {

        String resultado = FormataDadosUtils.mascararDocumento("123");

        assertEquals("***", resultado);
    }

    @Test
    void deveNormalizarDocumentoComSucesso() {

        String resultado = normalizarDocumento(" 12345678901 ");

        assertEquals("12345678901", resultado);
    }

    @Test
    void deveRetornarNullAoNormalizarDocumentoNulo() {

        String resultado = normalizarDocumento(null);

        assertNull(resultado);
    }

    @Test
    void deveNormalizarPlacaComSucesso() {

        String resultado = normalizarPlaca(" abc1d23 ");

        assertEquals("ABC1D23", resultado);
    }

    @Test
    void deveRetornarNullAoNormalizarPlacaNula() {

        String resultado = normalizarPlaca(null);

        assertNull(resultado);
    }

    @Test
    void deveMascararPlacaComSucesso() {

        String resultado = mascararPlaca("abc1d23");

        assertEquals("ABC***23", resultado);
    }

    @Test
    void deveRetornarNullQuandoPlacaForNula() {

        String resultado =mascararPlaca(null);

        assertNull(resultado);
    }

    @Test
    void deveRetornarPlacaQuandoEstiverEmBranco() {

        String resultado = mascararPlaca("   ");

        assertEquals("   ", resultado);
    }

    @Test
    void deveRetornarTresAsteriscosQuandoPlacaTiverMenosDeCincoCaracteres() {

        String resultado = mascararPlaca("AB12");

        assertEquals("***", resultado);
    }

    @Test
    void deveMascararPlacaNormalizandoAntesDoProcessamento() {

        String resultado = mascararPlaca(" abc1d23 ");

        assertEquals("ABC***23", resultado);
    }
}