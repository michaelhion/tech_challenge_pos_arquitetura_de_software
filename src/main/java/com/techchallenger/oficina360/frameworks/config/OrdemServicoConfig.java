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
import com.techchallenger.oficina360.usecases.ordemservico.*;
import com.techchallenger.oficina360.usecases.services.NotificarStatusOrdemServicoService;
import com.techchallenger.oficina360.usecases.services.ReservaEstoqueService;
import com.techchallenger.oficina360.usecases.services.TempoExecucaoService;
import com.techchallenger.oficina360.usecases.servicos.MovimentacaoEstoqueService;
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
	@Transactional
	public DiagnosticoLoader diagnosticoLoader(EstoqueGateway estoqueGateway, ServicoGateway servicoGateway) {
		return new DiagnosticoLoader(servicoGateway, estoqueGateway);
	}

	@Bean
	public OrdemServicoServicoValidator ordemServicoServicoValidator() {
		return new OrdemServicoServicoValidator();
	}

	@Bean
	public DiagnosticoValidator diagnosticoValidator(OrdemServicoServicoValidator servicoValidator,
			OrdemServicoEstoqueValidator estoqueValidator) {

		return new DiagnosticoValidator(servicoValidator, estoqueValidator);
	}

	@Bean
	@Transactional
	public MovimentacaoEstoqueService movimentacaoEstoqueService(EstoqueGateway estoqueGateway){
		return new MovimentacaoEstoqueService(estoqueGateway);
	}

	@Bean
	@Transactional
	public ReservaEstoqueService reservaEstoqueService(EstoqueGateway estoqueGateway) {

		return new ReservaEstoqueService(estoqueGateway);
	}

	@Bean
	public DiagnosticoFactory diagnosticoFactory() {
		return new DiagnosticoFactory();
	}

	@Bean
	@Transactional
	public DiagnosticarOrdemServicoUseCase diagnosticarOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway,
			OrdemServicoFinder ordemServicoFinder, DiagnosticoLoader diagnosticoLoader,
			DiagnosticoValidator diagnosticoValidator, DiagnosticoFactory diagnosticoFactory,
			ReservaEstoqueService reservaEstoqueService, NotificarStatusOrdemServicoService notificarStatusOrdemServicoService) {

		return new DiagnosticarOrdemServicoUseCase(ordemServicoGateway, diagnosticoFactory, diagnosticoValidator,
				ordemServicoFinder, diagnosticoLoader, reservaEstoqueService,notificarStatusOrdemServicoService);
	}

	@Bean
	@Transactional
	TempoExecucaoService tempoExecucaoServic(TempoExecucaoServicoGateway tempoExecucaoServicoGateway) {
		return new TempoExecucaoService(tempoExecucaoServicoGateway);
	}

	@Bean
	@Transactional
	public FinalizarExecucaoUseCase finalizarExecucaoUseCase(OrdemServicoGateway gateway,
			TempoExecucaoService tempoExecucaoService, OrdemServicoFinder loader, MovimentacaoEstoqueService movimentacaoEstoqueService,NotificarStatusOrdemServicoService notificarStatusOrdemServicoService) {
		return new FinalizarExecucaoUseCase(gateway, tempoExecucaoService, loader,movimentacaoEstoqueService,notificarStatusOrdemServicoService);
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
	public OrdemServicoFactory ordemServicoFactory() {
		return new OrdemServicoFactory();
	}

	@Bean
	public ConsultarStatusOsUseCase consultarStatusOsUseCase(OrdemServicoFinder ordemServicoFinder) {
		return new ConsultarStatusOsUseCase(ordemServicoFinder);
	}

	@Bean
	@Transactional
	public AbrirOrdemServicoUseCase abrirOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway,
			OrdemServicoFactory ordemServicoFactory, OrdemServicoValidator validator, ClienteFinder clienteFinder,
			VeiculoFinder veiculoFinder) {
		return new AbrirOrdemServicoUseCase(ordemServicoGateway, ordemServicoFactory, validator, clienteFinder,
				veiculoFinder);

	}

	@Bean
	@Transactional
	public AprovarOrcamentoUseCase aprovarOrcamentoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader,MovimentacaoEstoqueService movimentacaoEstoqueService) {
		return new AprovarOrcamentoUseCase(gateway, loader,movimentacaoEstoqueService);
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
	public DeletarOrdemServicoUseCase deletarOrdemServicoUseCase(OrdemServicoGateway gateway,
			OrdemServicoFinder loader) {
		return new DeletarOrdemServicoUseCase(gateway, loader);
	}

}
