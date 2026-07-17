package com.techchallenger.oficina360.frameworks.config;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.gateways.TempoExecucaoServicoGateway;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.services.validators.OrdemServicoEstoqueValidator;
import com.techchallenger.oficina360.services.validators.OrdemServicoServicoValidator;
import com.techchallenger.oficina360.usecases.factories.DiagnosticoFactory;
import com.techchallenger.oficina360.usecases.factories.OrdemServicoFactory;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.loaders.DiagnosticoLoader;
import com.techchallenger.oficina360.usecases.ordemservico.AbrirOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.AprovarOrcamentoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DeletarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DiagnosticarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.EditarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.FinalizarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.IniciarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.ListarOrdensServicoUseCase;
import com.techchallenger.oficina360.usecases.services.ReservaEstoqueService;
import com.techchallenger.oficina360.usecases.services.TempoExecucaoService;
import com.techchallenger.oficina360.usecases.validators.DiagnosticoValidator;
import com.techchallenger.oficina360.usecases.validators.OrdemServicoValidator;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdemServicoConfig {

	@Bean
	public OrdemServicoFinder ordemServicoFinder(OrdemServicoGateway gateway) {
		return new OrdemServicoFinder(gateway);
	}

	@Bean
	public DiagnosticoLoader diagnosticoLoader(
			EstoqueGateway estoqueGateway,
			ServicoGateway servicoGateway) {

		return new DiagnosticoLoader(servicoGateway, estoqueGateway);
	}

	@Bean
	public OrdemServicoServicoValidator ordemServicoServicoValidator() {
		return new OrdemServicoServicoValidator();
	}

	@Bean
	public DiagnosticoValidator diagnosticoValidator(
			OrdemServicoServicoValidator servicoValidator,
			OrdemServicoEstoqueValidator estoqueValidator) {

		return new DiagnosticoValidator(servicoValidator, estoqueValidator);
	}

	@Bean
	@Transactional
	public ReservaEstoqueService reservaEstoqueService(
			EstoqueGateway estoqueGateway) {

		return new ReservaEstoqueService(estoqueGateway);
	}

	@Bean
	public DiagnosticoFactory diagnosticoFactory(){
		return new DiagnosticoFactory();
	}

	@Bean
	@Transactional
	public DiagnosticarOrdemServicoUseCase diagnosticarOrdemServicoUseCase(
			OrdemServicoGateway ordemServicoGateway,
			OrdemServicoFinder ordemServicoFinder,
			DiagnosticoLoader diagnosticoLoader,
			DiagnosticoValidator diagnosticoValidator,
			DiagnosticoFactory diagnosticoFactory,
			ReservaEstoqueService reservaEstoqueService) {

		return new DiagnosticarOrdemServicoUseCase(
				ordemServicoGateway,
				diagnosticoFactory,
				diagnosticoValidator,
				ordemServicoFinder,
				diagnosticoLoader,
				reservaEstoqueService);
	}

	@Bean
	@Transactional
	TempoExecucaoService tempoExecucaoServic(TempoExecucaoServicoGateway tempoExecucaoServicoGateway) {
		return new TempoExecucaoService(tempoExecucaoServicoGateway);
	}

	@Bean
	@Transactional
	public FinalizarExecucaoUseCase finalizarExecucaoUseCase(OrdemServicoGateway gateway,
			TempoExecucaoService tempoExecucaoService, OrdemServicoFinder loader) {
		return new FinalizarExecucaoUseCase(gateway, tempoExecucaoService, loader);
	}

	@Bean
	public ClienteFinder clienteFinder(ClienteGateway clienteGateway) {
		return new ClienteFinder(clienteGateway);
	}

	@Bean
	public VeiculoFinder veiculoFinder(VeiculoGateway veiculoGateway) {
		return new VeiculoFinder(veiculoGateway);
	}

	@Bean
	public OrdemServicoValidator validator(OrdemServicoGateway ordemServicoGateway) {
		return new OrdemServicoValidator(ordemServicoGateway);
	}

	@Bean
	public OrdemServicoFactory ordemServicoFactory(){
		return new OrdemServicoFactory();
	}

	@Bean
	@Transactional
	public AbrirOrdemServicoUseCase abrirOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway,
			OrdemServicoFactory ordemServicoFactory,
			OrdemServicoValidator validator,
			ClienteFinder clienteFinder, VeiculoFinder veiculoFinder) {
		return new AbrirOrdemServicoUseCase(ordemServicoGateway, ordemServicoFactory, validator, clienteFinder,
				veiculoFinder);

	}

	@Bean
	@Transactional
	public AprovarOrcamentoUseCase aprovarOrcamentoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		return new AprovarOrcamentoUseCase(gateway, loader);
	}

	@Bean
	public BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase(OrdemServicoGateway gateway) {
		return new BuscarOrdemServicoPorIdUseCase(gateway);
	}

	@Bean
	@Transactional
	public EditarOrdemServicoUseCase editarOrdemServicoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		return new EditarOrdemServicoUseCase(gateway, loader);
	}

	@Bean
	@Transactional
	public IniciarExecucaoUseCase iniciarExecucaoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		return new IniciarExecucaoUseCase(gateway, loader);
	}

	@Bean
	public ListarOrdensServicoUseCase listarOrdensServicoUseCase(OrdemServicoGateway gateway) {
		return new ListarOrdensServicoUseCase(gateway);
	}

	@Bean
	public DeletarOrdemServicoUseCase deletarOrdemServicoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader) {
		return new DeletarOrdemServicoUseCase(gateway,loader);
	}

}
