package com.techchallenger.oficina360.dominio.shared.utils;

import java.util.Collection;
import java.util.Objects;

public final class CollectionUtils {

	private CollectionUtils() {
	}

	public static boolean ehNulaOuVazia(Collection<?> colecao) {
		return colecao == null || colecao.isEmpty();
	}

	public static boolean possuiElementoNulo(Collection<?> colecao) {
		return colecao != null && colecao.stream().anyMatch(Objects::isNull);
	}
}