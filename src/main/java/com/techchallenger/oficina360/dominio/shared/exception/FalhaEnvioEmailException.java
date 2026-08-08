package com.techchallenger.oficina360.dominio.shared.exception;

public class FalhaEnvioEmailException extends RuntimeException {

	public FalhaEnvioEmailException(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
}