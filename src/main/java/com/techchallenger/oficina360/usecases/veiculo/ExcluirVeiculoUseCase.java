package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import org.springframework.transaction.annotation.Transactional;

import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class ExcluirVeiculoUseCase {

	private final VeiculoGateway veiculoGateway;
	private final VeiculoFinder veiculoFinder;

	public ExcluirVeiculoUseCase(VeiculoGateway veiculoGateway, VeiculoFinder veiculoFinder) {
		this.veiculoGateway = veiculoGateway;
		this.veiculoFinder = veiculoFinder;
	}

	@Transactional
	public void delete(String placa) {
		String placaNormalizada = normalizarPlaca(placa);

		veiculoFinder.buscarPorPlacaOuFalhar(placaNormalizada);

		veiculoGateway.deleteByPlaca(placaNormalizada);
	}
}
