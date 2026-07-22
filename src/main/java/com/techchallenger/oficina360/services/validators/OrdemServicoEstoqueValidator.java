package com.techchallenger.oficina360.services.validators;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.DIAGNOSTICO_INVALIDO;

@Component
public class OrdemServicoEstoqueValidator {

	public void validar(Map<String, Integer> estoquesPorCodigoSolicitado, Map<String, Estoque> estoquesPorCodigoBanco) {
		if (estoqueNaoInformado(estoquesPorCodigoSolicitado)) {
			return;
		}

		validarSeExisteTudoQueFoiSolicitado(estoquesPorCodigoSolicitado, estoquesPorCodigoBanco);
		validarDisponibilidadeEstoque(estoquesPorCodigoSolicitado, estoquesPorCodigoBanco);
	}

	private boolean estoqueNaoInformado(Map<String, Integer> estoquesPorCodigoSolicitado) {
		return estoquesPorCodigoSolicitado == null || estoquesPorCodigoSolicitado.isEmpty();
	}

	private void validarSeExisteTudoQueFoiSolicitado(Map<String, Integer> estoquesPorCodigoSolicitado,
			Map<String, Estoque> estoquesPorCodigoBanco) {
		List<String> erros = new ArrayList<>();
		Set<String> codigosEncontrados = estoquesPorCodigoBanco.keySet();

		List<String> codigosNaoEncontrados = estoquesPorCodigoSolicitado.keySet().stream()
				.filter(codigo -> !codigosEncontrados.contains(codigo)).toList();

		if (!codigosNaoEncontrados.isEmpty()) {
			erros.add("Itens de estoque não encontrados: " + String.join(", ", codigosNaoEncontrados));
			throw new RegraDeNegocioException(DIAGNOSTICO_INVALIDO,erros);
		}
	}

	private void validarDisponibilidadeEstoque(Map<String, Integer> estoquesPorCodigoSolicitado,
			Map<String, Estoque> estoquesPorCodigoBanco) {
		List<String> erros = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : estoquesPorCodigoSolicitado.entrySet()) {

			String codigo = entry.getKey();
			Integer quantidadeSolicitada = entry.getValue();

			Estoque estoque = estoquesPorCodigoBanco.get(codigo);

			if (quantidadeSolicitada > estoque.getDisponiveis()) {
				erros.add(String.format("Estoque insuficiente para %s. Solicitado: %d, disponível: %d", codigo,
						quantidadeSolicitada, estoque.getDisponiveis()));
				throw new RegraDeNegocioException(DIAGNOSTICO_INVALIDO,erros);
			}

		}
	}
}