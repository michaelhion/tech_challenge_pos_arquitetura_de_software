package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.mappers.VeiculoMapper;

import java.util.Optional;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class BuscarVeiculoPorPlacaUseCase {

	private final VeiculoGateway veiculoGateway;

	public BuscarVeiculoPorPlacaUseCase(VeiculoGateway veiculoGateway) {
		this.veiculoGateway = veiculoGateway;
	}

	public Optional<VeiculoDTO> findByPlaca(String placa) {
		String placaNormalizada = normalizarPlaca(placa);

		return veiculoGateway.findByPlaca(placaNormalizada)
				.map(VeiculoMapper::domainToDTO);
	}
}
