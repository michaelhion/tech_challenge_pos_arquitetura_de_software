package com.techchallenger.oficina360.frameworks.web.exceptions;

import java.util.UUID;

public class ConflitoConcorrenciaEstoqueException extends RuntimeException {

	public ConflitoConcorrenciaEstoqueException(UUID estoqueId, Throwable causa) {
		super("O estoque foi alterado por outra operação: " + estoqueId, causa);
	}
}
