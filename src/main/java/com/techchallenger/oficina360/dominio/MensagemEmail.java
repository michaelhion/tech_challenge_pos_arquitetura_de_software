package com.techchallenger.oficina360.dominio;

public record MensagemEmail(
	String destinatario,
	String assunto,
	String mensagem
) {
}
