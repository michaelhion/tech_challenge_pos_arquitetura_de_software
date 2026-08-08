package com.techchallenger.oficina360.usecases.loaders;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.DiagnosticoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.DiagnosticoEstoqueCommand;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiagnosticoLoader {

	private final ServicoGateway servicoGateway;
	private final EstoqueGateway estoqueGateway;

	public DiagnosticoLoader(ServicoGateway servicoGateway, EstoqueGateway estoqueGateway) {
		this.servicoGateway = servicoGateway;
		this.estoqueGateway = estoqueGateway;
	}

	public DiagnosticoDados carregar(DiagnosticoCommand command){
		List<String> codigosServicos = command.codigosServicos();
		List<DiagnosticoEstoqueCommand> itensEstoque = command.itensEstoque();
		List<String> codigosEstoque = buildCodigosEstoque(itensEstoque);
		Map<String, Estoque> estoqueDB = buildMapEstoquePorCodigo(codigosEstoque);
		return new DiagnosticoDados(
				codigosServicos,
				codigosEstoque,
				buildMapCodigoPorServico(codigosServicos),
				buildMapQuantidadeEstoquePorCodigo(itensEstoque),
				estoqueDB
		);
	}

	private List<String> buildCodigosEstoque(List<DiagnosticoEstoqueCommand> itensEstoque) {
		return itensEstoque.stream()
				.map(DiagnosticoEstoqueCommand::codigo)
				.toList();
	}

	private Map<String, Estoque> buildMapEstoquePorCodigo(List<String> codigosEstoque) {
		return estoqueGateway.findByCodigoIn(codigosEstoque).stream().collect(Collectors.toMap(Estoque::getCodigo, estoque -> estoque));
	}

	private Map<String, Integer> buildMapQuantidadeEstoquePorCodigo(List<DiagnosticoEstoqueCommand> itensEstoque) {
		return itensEstoque.stream().collect(Collectors.toMap(DiagnosticoEstoqueCommand::codigo,DiagnosticoEstoqueCommand::quantidade));
	}

	private Map<String, Servico> buildMapCodigoPorServico(List<String> codigosServicos) {
		return servicoGateway.findByCodigoIn(codigosServicos)
				.stream()
				.collect(Collectors.toMap(Servico::getCodigo, servico -> servico));
	}

}
