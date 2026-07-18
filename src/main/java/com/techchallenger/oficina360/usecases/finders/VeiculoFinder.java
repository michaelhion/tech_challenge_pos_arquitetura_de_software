package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.gateways.VeiculoGateway;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class VeiculoFinder {

	private final VeiculoGateway gateway;

	public VeiculoFinder(VeiculoGateway gateway) {
		this.gateway = gateway;
	}

	public Veiculo buscarPorPlacaOuFalhar(String placa) {
		return gateway.findByPlaca(normalizarPlaca(placa))
				.orElseThrow(() ->
						new RecursoNaoEncontradoException(VEICULO_NAO_ENCONTRADO));
	}

}
