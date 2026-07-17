package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.gateways.TempoExecucaoServicoGateway;
import com.techchallenger.oficina360.usecases.servicos.AtualizarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.BuscarServicoPorCodigoUseCase;
import com.techchallenger.oficina360.usecases.servicos.CadastrarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ExcluirServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ListarServicosUseCase;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicosServiceConfig {

	@Bean
	@Transactional
	public CadastrarServicoUseCase cadastrarServicoUseCase(ServicoGateway servicoGateway) {
		return new CadastrarServicoUseCase(servicoGateway);

	}
	@Bean
	@Transactional
	public BuscarServicoPorCodigoUseCase buscarServicoPorCodigoUseCase(ServicoGateway servicoGateway) {
		return new BuscarServicoPorCodigoUseCase(servicoGateway);

	}

	@Bean
	@Transactional
	public ListarServicosUseCase listarServicosUseCase(ServicoGateway servicoGateway,
			TempoExecucaoServicoGateway tempoExecucaoServicoGateway) {
		return new ListarServicosUseCase(servicoGateway,tempoExecucaoServicoGateway);

	}
	
	@Bean
	@Transactional
	public AtualizarServicoUseCase atualizarServicoUseCase(ServicoGateway servicoGateway) {
		return new AtualizarServicoUseCase(servicoGateway);

	}

	@Bean
	@Transactional
	public ExcluirServicoUseCase excluirServicoUseCase(ServicoGateway servicoGateway) {
		return new ExcluirServicoUseCase(servicoGateway);

	}
}
