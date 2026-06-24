package com.techchallenger.oficina360.dtos.validators;

public final class CodigoPattern {

    private CodigoPattern() {
    }

    public static final String REGEX = "^[A-Z0-9]+([_-][A-Z0-9]+)*$";

    public static final String MESSAGE =
            "O código deve conter apenas letras maiúsculas, números, hífen ou underline, sem espaços ou acentos. " +
                    "Exemplos válidos: SRV-TROCA-OLEO, EST_FILTRO_OLEO";
}