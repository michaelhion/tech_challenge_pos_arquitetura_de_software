package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.factories.OrdemServicoFactory;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.validators.OrdemServicoValidator;

import static com.techchallenger.oficina360.mappers.CriarOrdemServicoMapper.toDTO;

public class AbrirOrdemServicoUseCase {

	private final OrdemServicoGateway ordemServicoGateway;
	private final OrdemServicoFactory ordemServicoFactory;
	private final OrdemServicoValidator ordemServicoValidator;
	private final ClienteFinder clienteFinder;
	private final VeiculoFinder veiculoFinder;

	public AbrirOrdemServicoUseCase(OrdemServicoGateway ordemServicoGateway, OrdemServicoFactory ordemServicoFactory,
			OrdemServicoValidator validator,
			ClienteFinder clienteFinder, VeiculoFinder veiculoFinder) {

		this.ordemServicoGateway = ordemServicoGateway;
		this.ordemServicoFactory = ordemServicoFactory;
		this.ordemServicoValidator = validator;
		this.clienteFinder = clienteFinder;
		this.veiculoFinder = veiculoFinder;
	}

	public CriarOrdemServicoDTO abrirOrdemServico(CriarOrdemServicoDTO dto) {

		ordemServicoValidator.validarNaoExisteOrdemServicoAtiva(dto.placaVeiculo());

		Cliente cliente = clienteFinder.buscarPorDocumentoOuFalhar(dto.documentoCliente());
		Veiculo veiculo = veiculoFinder.buscarPorPlacaOuFalhar(dto.placaVeiculo());

		ordemServicoValidator.validarVeiculoPertenceAoCliente(veiculo, cliente);

		OrdemServico ordemServico = ordemServicoFactory.criar(dto, cliente.getDocumento(), veiculo.getPlaca());

		return toDTO(ordemServicoGateway.save(ordemServico));
	}

}
