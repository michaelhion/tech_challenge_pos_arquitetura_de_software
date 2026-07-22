package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.SERVICO_NAO_ENCONTRADO;

public class ExcluirServicoUseCase {

	private final ServicoGateway servicoGateway;

	public ExcluirServicoUseCase(ServicoGateway servicoGateway) {
		this.servicoGateway = servicoGateway;
	}

	public void delete(String codigo) {
		servicoGateway.findByCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));

		servicoGateway.deleteByCodigo(codigo);
	}
}
