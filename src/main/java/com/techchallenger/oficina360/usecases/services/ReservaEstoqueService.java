package com.techchallenger.oficina360.usecases.services;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.loaders.DiagnosticoDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReservaEstoqueService {

	private final EstoqueGateway estoqueGateway;

	public ReservaEstoqueService(EstoqueGateway estoqueGateway) {
		this.estoqueGateway = estoqueGateway;
	}

	public void reservar(DiagnosticoDados dados) {

		List<Estoque> estoques = new ArrayList<>();

		for (Map.Entry<String, Integer> entry :
				dados.estoquesPorCodigoSolicitado().entrySet()) {

			Estoque estoque =
					dados.estoquesPorCodigoBanco().get(entry.getKey());

			estoque.reservar(entry.getValue());

			estoques.add(estoque);
		}

		estoqueGateway.saveAll(estoques);
	}
}
