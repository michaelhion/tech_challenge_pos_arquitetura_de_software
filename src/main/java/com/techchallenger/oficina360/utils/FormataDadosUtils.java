package com.techchallenger.oficina360.utils;

public class FormataDadosUtils {

    private FormataDadosUtils(){}

    public static String mascararDocumento(String documento) {
        if (documento == null || documento.length() < 4) {
            return "***";
        }

        return "***" + documento.substring(documento.length() - 4);
    }

    public static String normalizarDocumento(String documento) {
        return documento == null ? null : documento.trim();
    }

    public static String normalizarPlaca(String placa) {
        return placa == null ? null : placa.trim().toUpperCase();
    }

    public static String mascararPlaca(String placa) {

        if (placa == null || placa.isBlank()) {
            return placa;
        }

        String placaNormalizada = normalizarPlaca(placa);

        if (placaNormalizada.length() < 5) {
            return "***";
        }

        return placaNormalizada.substring(0, 3)
                + "***"
                + placaNormalizada.substring(placaNormalizada.length() - 2);
    }
}