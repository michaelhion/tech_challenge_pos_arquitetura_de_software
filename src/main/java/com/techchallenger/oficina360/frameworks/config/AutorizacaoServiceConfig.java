package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.gateways.UsuarioAutenticadoGateway;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.autorizacao.AutorizacaoClienteUseCase;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutorizacaoServiceConfig {

	@Bean
	@Transactional
	public AutorizacaoClienteUseCase autorizacaoClienteUseCase(VeiculoGateway veiculoGateway,
			OrdemServicoGateway ordemServicoGateway,
			UsuarioAutenticadoGateway usuarioAutenticadoGateway) {
		return new AutorizacaoClienteUseCase(veiculoGateway,ordemServicoGateway,usuarioAutenticadoGateway);

	}


}
