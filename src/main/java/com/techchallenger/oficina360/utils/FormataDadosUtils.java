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
}