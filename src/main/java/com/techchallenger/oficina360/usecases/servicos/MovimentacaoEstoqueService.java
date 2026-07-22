package com.techchallenger.oficina360.usecases.services;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.shared.exception.ItemEstoqueInvalidoException;
import com.techchallenger.oficina360.gateways.EstoqueGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class MovimentacaoEstoqueService {

	private final EstoqueGateway estoqueGateway;

	public MovimentacaoEstoqueService(EstoqueGateway estoqueGateway) {
		this.estoqueGateway = estoqueGateway;
	}

	public void liberarReservas(List<OrdemServicoItemEstoque> itens) {
		movimentar(itens, Estoque::liberarReserva);
	}

	public void consumirReservas(List<OrdemServicoItemEstoque> itens) {
		movimentar(itens, Estoque::consumirReserva);
	}

	private void movimentar(List<OrdemServicoItemEstoque> itens, BiConsumer<Estoque, Integer> operacao) {
		if (itens == null || itens.isEmpty()) {
			return;
		}

		Map<UUID, Integer> quantidadePorEstoque = agruparQuantidades(itens);

		List<UUID> ids = new ArrayList<>(quantidadePorEstoque.keySet());

		List<Estoque> estoques = estoqueGateway.findByIdIn(ids);

		validarEstoquesEncontrados(quantidadePorEstoque, estoques);

		estoques.forEach(estoque -> {
			Integer quantidade = quantidadePorEstoque.get(estoque.getId());

			operacao.accept(estoque, quantidade);
		});

		estoqueGateway.saveAll(estoques);
	}

	private Map<UUID, Integer> agruparQuantidades(List<OrdemServicoItemEstoque> itens) {
		return itens.stream().collect(
				Collectors.toMap(OrdemServicoItemEstoque::getEstoqueId, OrdemServicoItemEstoque::getQuantidade,
						Integer::sum));
	}

	private void validarEstoquesEncontrados(Map<UUID, Integer> quantidadePorEstoque, List<Estoque> estoques) {
		List<UUID> idsEncontrados = estoques.stream().map(Estoque::getId).toList();

		List<UUID> idsAusentes = quantidadePorEstoque.keySet().stream().filter(id -> !idsEncontrados.contains(id))
				.toList();

		if (!idsAusentes.isEmpty()) {
			throw new ItemEstoqueInvalidoException("Itens de estoque não encontrados: " + idsAusentes);
		}
	}
}