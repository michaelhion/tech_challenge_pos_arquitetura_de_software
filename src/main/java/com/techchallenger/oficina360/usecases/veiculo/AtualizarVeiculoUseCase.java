package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;
import com.techchallenger.oficina360.usecases.veiculo.exception.PlacaJaExisteException;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_VEICULO_CADASTRADO;
import static com.techchallenger.oficina360.mappers.VeiculoCommandMapper.domainToCommand;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class AtualizarVeiculoUseCase {

	private final VeiculoGateway veiculoGateway;
	private final VeiculoFinder veiculoFinder;
	private final ClienteFinder clienteFinder;

	public AtualizarVeiculoUseCase(VeiculoGateway veiculoGateway, VeiculoFinder veiculoFinder,
			ClienteFinder clienteFinder) {
		this.veiculoGateway = veiculoGateway;
		this.veiculoFinder = veiculoFinder;
		this.clienteFinder = clienteFinder;
	}

	public VeiculoCommand edit(String placaAtual, VeiculoCommand command) {
		String placaAtualNormalizada = normalizarPlaca(placaAtual);
		String novaPlacaNormalizada = normalizarPlaca(command.placa());
		String documentoClienteNormalizado = normalizarDocumento(command.clienteDocumento());

		Veiculo veiculo = veiculoFinder.buscarPorPlacaOuFalhar(placaAtualNormalizada);

		clienteFinder.buscarPorDocumentoOuFalhar(documentoClienteNormalizado);
		validarPlacaDisponivelParaEdicao(novaPlacaNormalizada, veiculo.getId());

		veiculo.editar(
				command.placa(),
				command.marca(),
				command.modelo(),
				command.ano(),
				command.clienteDocumento()
		);

		Veiculo veiculoAtualizado = veiculoGateway.save(veiculo);

		return domainToCommand(veiculoAtualizado);
	}

	private void validarPlacaDisponivelParaEdicao(
			String novaPlaca,
			UUID id
	) {
		if (veiculoGateway.existsByPlacaAndIdNot(novaPlaca, id)) {
			throw new PlacaJaExisteException(VEICULO_SERV_VEICULO_CADASTRADO);
		}
	}
}
