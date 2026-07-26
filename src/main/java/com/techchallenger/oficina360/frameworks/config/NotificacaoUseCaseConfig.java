package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.gateways.NotificacaoEmailGateway;
import com.techchallenger.oficina360.usecases.services.NotificarStatusOrdemServicoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificacaoUseCaseConfig {

	@Bean
	public NotificarStatusOrdemServicoService notificarStatusOrdemServicoService(ClienteGateway clienteGateway,
			NotificacaoEmailGateway emailGateway) {
		return new NotificarStatusOrdemServicoService(clienteGateway, emailGateway);
	}
}