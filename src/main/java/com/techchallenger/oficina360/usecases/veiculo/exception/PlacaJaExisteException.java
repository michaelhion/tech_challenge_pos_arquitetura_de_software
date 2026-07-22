package com.techchallenger.oficina360.usecases.veiculo.exception;

import com.techchallenger.oficina360.usecases.shared.exception.AplicacaoException;

public class PlacaJaExisteException extends AplicacaoException {

	public PlacaJaExisteException(String mensagem) {
		super(mensagem);
	}
}
