package com.techchallenger.oficina360.docs;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SwaggerConstants {

    /*
     * Status codes
     */
    public static final String STATUS_CODE_OK = "200";
    public static final String STATUS_CODE_CREATED = "201";
    public static final String STATUS_CODE_NO_CONTENT = "204";
    public static final String STATUS_CODE_BAD_REQUEST = "400";
    public static final String STATUS_CODE_NOT_FOUND = "404";
    public static final String STATUS_CODE_CONFLICT = "409";

    /*
     * Media types
     */
    public static final String MEDIA_TYPE_JSON = MediaType.APPLICATION_JSON_VALUE;

    /*
     * Exemplos comuns
     */
    public static final String EXAMPLE_UUID = "7b5a3247-a14a-44f8-872f-016e179a92fd";
    public static final String EXAMPLE_CODIGO = "OLEO-5W30";
    public static final String EXAMPLE_CODIGO_SERVICO = "TROCA-OLEO";

    public static final String EXAMPLE_DOCUMENTO = "12345678901";
    public static final String EXAMPLE_PLACA_MERCOSUL = "ABC1D23";
    public static final String DESCRIPTION_BAD_REQUEST_DOCUMENTO =
            "Documento inválido. Informe um CPF com 11 dígitos ou CNPJ com 14 dígitos, somente números.";

    /*
     * Descrições comuns
     */
    public static final String DESCRIPTION_ID_UUID =
            "Identificador único no formato UUID.";

    public static final String DESCRIPTION_BAD_REQUEST_CODIGO =
            "Codigo inválido. Codigo deve estar em maiusculo, ser separado por hifen ou underline caso precise e não ter caracter especial";

    public static final String DESCRIPTION_BAD_REQUEST_PLACA =
            "Placa inválida. Informe a placa no padrão antigo ABC1234 ou Mercosul ABC1D23, sem hífen.";

}