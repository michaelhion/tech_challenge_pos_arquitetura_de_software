package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.factories.DiagnosticoFactory;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.loaders.DiagnosticoDados;
import com.techchallenger.oficina360.usecases.loaders.DiagnosticoLoader;
import com.techchallenger.oficina360.usecases.services.ReservaEstoqueService;
import com.techchallenger.oficina360.usecases.validators.DiagnosticoValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.techchallenger.oficina360.mappers.OrdemServicoMapper.toDTO;

public class DiagnosticarOrdemServicoUseCase {

	private final OrdemServicoGateway ordemServicoGateway;
	private final DiagnosticoFactory diagnosticoFactory;
	private final DiagnosticoValidator diagnosticoValidator;
	private final OrdemServicoFinder ordemServicoFinder;
	private final DiagnosticoLoader diagnosticoLoader;
	private final ReservaEstoqueService reservaEstoqueService;

	public DiagnosticarOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway,
			DiagnosticoFactory diagnosticoFactory, DiagnosticoValidator diagnosticoValidator,
			OrdemServicoFinder ordemServicoFinder, DiagnosticoLoader diagnosticoLoader,
			ReservaEstoqueService reservaEstoqueService) {
		this.ordemServicoGateway = ordemServicoGateway;
		this.diagnosticoFactory = diagnosticoFactory;
		this.diagnosticoValidator = diagnosticoValidator;
		this.ordemServicoFinder = ordemServicoFinder;
		this.diagnosticoLoader = diagnosticoLoader;
		this.reservaEstoqueService = reservaEstoqueService;
	}

	public OrdemServicoDTO diagnosticar(UUID id, DiagnosticoDTO dto) {

		OrdemServico ordemServico = ordemServicoFinder.obterOuFalhar(id);

		DiagnosticoDados diagnosticoDados = diagnosticoLoader.carregar(dto);

		diagnosticoValidator.validar(diagnosticoDados);

		List<OrdemServicoServico> servicos = criarServicos(diagnosticoDados);

		List<OrdemServicoItemEstoque> itens = criarItensEstoque(diagnosticoDados);

		reservaEstoqueService.reservar(diagnosticoDados);

		ordemServico.iniciarDiagnostico();
		ordemServico.adicionarDiagnostico(servicos, itens);
		ordemServico.finalizarDiagnostico();

		return toDTO(ordemServicoGateway.save(ordemServico));
	}

	private List<OrdemServicoServico> criarServicos(DiagnosticoDados dados) {

		List<OrdemServicoServico> servicos = new ArrayList<>();

		for (String codigo : dados.codigosServicos()) {
			Servico servico = dados.servicosPorCodigoBanco().get(codigo);

			servicos.add(diagnosticoFactory.criarServicoDaOs(servico));
		}

		return servicos;
	}

	private List<OrdemServicoItemEstoque> criarItensEstoque(DiagnosticoDados dados) {

		List<OrdemServicoItemEstoque> itens = new ArrayList<>();

		for (Map.Entry<String, Integer> entry : dados.estoquesPorCodigoSolicitado().entrySet()) {

			Estoque estoque = dados.estoquesPorCodigoBanco().get(entry.getKey());

			itens.add(diagnosticoFactory.criarItemEstoqueDaOs(estoque, entry.getValue()));
		}

		return itens;
	}
}