package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.veiculo.AtualizarVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.BuscarVeiculoPorPlacaUseCase;
import com.techchallenger.oficina360.usecases.veiculo.CadastrarVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.ExcluirVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.ListarVeiculosUseCase;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VeiculoServiceConfig {

	@Bean
	@Transactional
	public CadastrarVeiculoUseCase cadastrarVeiculoUseCase(VeiculoGateway veiculoGateway, ClienteFinder clienteFinder) {
		return new CadastrarVeiculoUseCase(veiculoGateway,clienteFinder);

	}

	@Bean
	@Transactional
	public AtualizarVeiculoUseCase atualizarVeiculoUseCase(VeiculoGateway veiculoGateway, VeiculoFinder veiculoFinder, ClienteFinder clienteFinder) {
		return new AtualizarVeiculoUseCase(veiculoGateway,veiculoFinder, clienteFinder);

	}
	@Bean
	@Transactional
	public BuscarVeiculoPorPlacaUseCase buscarVeiculoPorPlacaUseCase(VeiculoGateway veiculoGateway) {
		return new BuscarVeiculoPorPlacaUseCase(veiculoGateway);

	}
	@Bean
	public ListarVeiculosUseCase listarVeiculosUseCase(VeiculoGateway veiculoGateway) {
		return new ListarVeiculosUseCase(veiculoGateway);

	}
	@Bean
	@Transactional
	public ExcluirVeiculoUseCase excluirVeiculoUseCase(VeiculoGateway veiculoGateway, VeiculoFinder veiculoFinder) {
		return new ExcluirVeiculoUseCase(veiculoGateway,veiculoFinder);

	}
}
