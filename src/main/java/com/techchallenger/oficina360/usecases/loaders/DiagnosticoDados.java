package com.techchallenger.oficina360.usecases.loaders;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.Servico;

import java.util.List;
import java.util.Map;

public record DiagnosticoDados(
		List<String> codigosServicos,
		List<String> codigosEstoque,
		Map<String, Servico> servicosPorCodigoBanco,
		Map<String, Integer> estoquesPorCodigoSolicitado,
		Map<String, Estoque> estoquesPorCodigoBanco
) {}
