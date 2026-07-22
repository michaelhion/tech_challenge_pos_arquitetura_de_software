package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.shared.exception.VeiculosJaCadastradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_VEICULO_CADASTRADO;
import static com.techchallenger.oficina360.mappers.VeiculoMapper.domainToDTO;
import static com.techchallenger.oficina360.mappers.VeiculoMapper.toDomain;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class CadastrarVeiculoUseCase {

	private final VeiculoGateway veiculoGateway;
	private final ClienteFinder clienteFinder;

	public CadastrarVeiculoUseCase(VeiculoGateway veiculoGateway, ClienteFinder clienteFinder) {
		this.veiculoGateway = veiculoGateway;
		this.clienteFinder = clienteFinder;
	}

	public VeiculoDTO save(VeiculoDTO veiculoDTO) {
		String placaNormalizada = normalizarPlaca(veiculoDTO.placa());
		String documentoClienteNormalizado = normalizarDocumento(veiculoDTO.clienteDocumento());

		clienteFinder.buscarPorDocumentoOuFalhar(documentoClienteNormalizado);
		validarPlacaDisponivelParaCadastro(placaNormalizada);

		Veiculo veiculo = toDomain(veiculoDTO);

		Veiculo veiculoSalvo = veiculoGateway.save(veiculo);

		return domainToDTO(veiculoSalvo);
	}

	private void validarPlacaDisponivelParaCadastro(String placa) {
		if (veiculoGateway.existsByPlaca(placa)) {
			throw new VeiculosJaCadastradoException(VEICULO_SERV_VEICULO_CADASTRADO);
		}
	}

}
