package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.mappers.VeiculoMapper;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.veiculo.exception.PlacaJaExisteException;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_VEICULO_CADASTRADO;
import static com.techchallenger.oficina360.mappers.VeiculoMapper.domainToDTO;
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

	public VeiculoDTO edit(String placaAtual, VeiculoDTO veiculoDTO) {
		String placaAtualNormalizada = normalizarPlaca(placaAtual);
		String novaPlacaNormalizada = normalizarPlaca(veiculoDTO.placa());
		String documentoClienteNormalizado = normalizarDocumento(veiculoDTO.clienteDocumento());

		Veiculo veiculo = veiculoFinder.buscarPorPlacaOuFalhar(placaAtualNormalizada);

		clienteFinder.buscarPorDocumentoOuFalhar(documentoClienteNormalizado);
		validarPlacaDisponivelParaEdicao(novaPlacaNormalizada, veiculo.getId());

		VeiculoMapper.updateDomainFromDto(veiculoDTO, veiculo);

		Veiculo veiculoAtualizado = veiculoGateway.save(veiculo);

		return domainToDTO(veiculoAtualizado);
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
