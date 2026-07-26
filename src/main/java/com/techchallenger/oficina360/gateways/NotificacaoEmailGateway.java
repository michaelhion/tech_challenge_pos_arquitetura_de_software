package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.MensagemEmail;

public interface NotificacaoEmailGateway {

	void enviar(MensagemEmail mensagem);
}