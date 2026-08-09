package com.techchallenger.oficina360.utils;

public class FormataDadosUtils {

	private FormataDadosUtils() {
	}

	public static String mascararDocumento(String documento) {
		if (documento == null || documento.length() < 4) {
			return "***";
		}

		return "***" + documento.substring(documento.length() - 4);
	}

	public static String normalizarDocumento(String documento) {
		if (documento == null) {
			return null;
		}

		return documento.trim().replaceAll("\\D", "");
	}

	public static String normalizarPlaca(String placa) {
		if (placa == null) {
			return null;
		}

		return placa.trim().replace("-", "").replace(" ", "").toUpperCase();
	}

	public static String mascararPlaca(String placa) {

		if (placa == null || placa.isBlank()) {
			return placa;
		}

		String placaNormalizada = normalizarPlaca(placa);

		if (placaNormalizada.length() < 5) {
			return "***";
		}

		return placaNormalizada.substring(0, 3) + "***" + placaNormalizada.substring(placaNormalizada.length() - 2);
	}
}