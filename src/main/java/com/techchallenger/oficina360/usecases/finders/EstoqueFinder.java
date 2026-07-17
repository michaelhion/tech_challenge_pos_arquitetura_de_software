package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.frameworks.web.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.gateways.EstoqueGateway;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA;

public class EstoqueFinder {

	private final EstoqueGateway estoqueGateway;

	public EstoqueFinder(EstoqueGateway estoqueGateway) {
		this.estoqueGateway = estoqueGateway;
	}

	public Estoque obterOuFalhar(String codigo) {
		return estoqueGateway.findByCodigo(codigo)
				.orElseThrow(() -> new RegraDeNegocioException(ESTOQUE_CODIGO_JA_EXISTE_NO_SISTEMA));
	}
}
