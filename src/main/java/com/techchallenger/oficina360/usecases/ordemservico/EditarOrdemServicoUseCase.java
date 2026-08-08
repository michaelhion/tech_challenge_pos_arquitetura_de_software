package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.OrdemServicoFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoReqCommand;
import com.techchallenger.oficina360.usecases.validators.OrdemServicoValidator;

import java.util.UUID;

public class EditarOrdemServicoUseCase {

	private final OrdemServicoGateway gateway;
	private final OrdemServicoFinder loader;
	private final OrdemServicoValidator ordemServicoValidator;
	private final ClienteFinder clienteFinder;
	private final VeiculoFinder veiculoFinder;

	public EditarOrdemServicoUseCase(OrdemServicoGateway gateway, OrdemServicoFinder loader,
			OrdemServicoValidator ordemServicoValidator, ClienteFinder clienteFinder, VeiculoFinder veiculoFinder) {
		this.gateway = gateway;
		this.loader = loader;
		this.ordemServicoValidator = ordemServicoValidator;
		this.clienteFinder = clienteFinder;
		this.veiculoFinder = veiculoFinder;
	}

	public OrdemServico edit(UUID id, OrdemServicoReqCommand command) {
		OrdemServico ordemServico = loader.obterOuFalhar(id);
		Cliente cliente = clienteFinder.buscarPorDocumentoOuFalhar(command.documentoCliente());
		Veiculo veiculo = veiculoFinder.buscarPorPlacaOuFalhar(command.placaVeiculo());
		ordemServicoValidator.validarVeiculoPertenceAoCliente(veiculo, cliente);
		ordemServico.editarOSRecebida(command.documentoCliente(), command.placaVeiculo(), command.descricaoProblema());

		return gateway.save(ordemServico);

	}
}
