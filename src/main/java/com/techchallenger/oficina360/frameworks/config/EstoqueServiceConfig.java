package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.estoque.BuscarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.CriarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.EditarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ExcluirItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ListarItensEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ReservarEstoqueUseCase;
import com.techchallenger.oficina360.usecases.finders.EstoqueFinder;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EstoqueServiceConfig {

	@Bean
	public EstoqueFinder estoqueFinder(EstoqueGateway estoqueGateway){
		return new EstoqueFinder(estoqueGateway);
	}

	@Bean
	@Transactional
	public CriarItemEstoqueUseCase criarItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		return new CriarItemEstoqueUseCase(estoqueFinder,estoqueGateway);

	}

	@Bean
	@Transactional
	public EditarItemEstoqueUseCase editarItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		return new EditarItemEstoqueUseCase(estoqueFinder,estoqueGateway);

	}
	@Bean
	@Transactional
	public BuscarItemEstoqueUseCase buscarItemEstoqueUseCase(EstoqueGateway estoqueGateway) {
		return new BuscarItemEstoqueUseCase(estoqueGateway);

	}
	@Bean
	@Transactional
	public ListarItensEstoqueUseCase listarItensEstoqueUseCase(EstoqueGateway estoqueGateway) {
		return new ListarItensEstoqueUseCase(estoqueGateway);

	}
	@Bean
	@Transactional
	public ExcluirItemEstoqueUseCase excluirItemEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		return new ExcluirItemEstoqueUseCase(estoqueFinder,estoqueGateway);

	}
	@Bean
	@Transactional
	public ReservarEstoqueUseCase reservarEstoqueUseCase(EstoqueFinder estoqueFinder, EstoqueGateway estoqueGateway) {
		return new ReservarEstoqueUseCase(estoqueFinder,estoqueGateway);

	}
}
