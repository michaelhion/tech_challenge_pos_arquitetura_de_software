package com.techchallenger.oficina360.services.validators;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.frameworks.web.exceptions.RegraDeNegocioException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.DIAGNOSTICO_INVALIDO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.INFORME_AO_MENOS_UM_SERVICO_PARA_O_DIAGNOSTICO;

public class OrdemServicoServicoValidator {

	public void validar(List<String> codigosServicos, Map<String, Servico> servicosPorCodigo) {
		List<String> erros = new ArrayList<>();

		if (servicosNaoInformados(codigosServicos)) {
			throw new RegraDeNegocioException(DIAGNOSTICO_INVALIDO,
					List.of(INFORME_AO_MENOS_UM_SERVICO_PARA_O_DIAGNOSTICO));
		}

		validarSeTodosServicosExistem(codigosServicos, servicosPorCodigo, erros);

		if (!erros.isEmpty()) {
			throw new RegraDeNegocioException(DIAGNOSTICO_INVALIDO, erros);
		}
	}

	private static boolean servicosNaoInformados(List<String> codigosServicos) {
		return codigosServicos == null || codigosServicos.isEmpty();
	}

	private void validarSeTodosServicosExistem(List<String> codigosServicosSolicitados,
			Map<String, Servico> servicosPorCodigoEncontrados, List<String> erros) {

		List<String> codigosNaoEncontrados = buscaCodigosNaoEncontrados(codigosServicosSolicitados,
				servicosPorCodigoEncontrados);

		if (!codigosNaoEncontrados.isEmpty()) {
			erros.add("Serviços não encontrados: " + String.join(", ", codigosNaoEncontrados));
		}
	}

	private List<String> buscaCodigosNaoEncontrados(List<String> codigosServicosSolicitados,
			Map<String, Servico> servicosPorCodigo) {

		return codigosServicosSolicitados.stream().distinct().filter(codigo -> !servicosPorCodigo.containsKey(codigo))
				.toList();
	}

}