package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;

import static com.techchallenger.oficina360.mappers.VeiculoCommandMapper.domainToCommand;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class BuscarVeiculoPorPlacaUseCase {

	private final VeiculoGateway veiculoGateway;

	public BuscarVeiculoPorPlacaUseCase(VeiculoGateway veiculoGateway) {
		this.veiculoGateway = veiculoGateway;
	}

	public VeiculoCommand findByPlaca(String placa) {
		String placaNormalizada = normalizarPlaca(placa);

		Veiculo veiculo = veiculoGateway.findByPlaca(placaNormalizada)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Veiculo não encontrado"));
		return domainToCommand(veiculo);
	}
}
