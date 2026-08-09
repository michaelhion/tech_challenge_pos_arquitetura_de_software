package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.gateways.Relogio;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoReqCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.validators.OrdemServicoValidator;

import static com.techchallenger.oficina360.mappers.OrdemServicoCommandMapper.domainToCommand;

public class AbrirOrdemServicoUseCase {

	private final OrdemServicoGateway ordemServicoGateway;
	private final OrdemServicoValidator ordemServicoValidator;
	private final ClienteFinder clienteFinder;
	private final VeiculoFinder veiculoFinder;
	private final Relogio relogio;

	public AbrirOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway, OrdemServicoValidator validator,
			ClienteFinder clienteFinder, VeiculoFinder veiculoFinder, Relogio relogio) {

		this.ordemServicoGateway = ordemServicoGateway;
		this.ordemServicoValidator = validator;
		this.clienteFinder = clienteFinder;
		this.veiculoFinder = veiculoFinder;
		this.relogio = relogio;
	}

	public OrdemServicoRespCommand abrirOrdemServico(OrdemServicoReqCommand command) {

		ordemServicoValidator.validarNaoExisteOrdemServicoAtiva(command.placaVeiculo());

		Cliente cliente = clienteFinder.buscarPorDocumentoOuFalhar(command.documentoCliente());
		Veiculo veiculo = veiculoFinder.buscarPorPlacaOuFalhar(command.placaVeiculo());

		ordemServicoValidator.validarVeiculoPertenceAoCliente(veiculo, cliente);

		OrdemServico ordemServico = OrdemServico.criar(command.documentoCliente(), command.placaVeiculo(),
				command.descricaoProblema(),relogio.agora());

		return domainToCommand(ordemServicoGateway.save(ordemServico));
	}

}
