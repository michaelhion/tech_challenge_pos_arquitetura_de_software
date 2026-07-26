package com.techchallenger.oficina360.usecases.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.ordemservico.output.OrdemServicoResumoOutput;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import com.techchallenger.oficina360.usecases.shared.paginacao.ResultadoPaginado;

public class ListarOrdensServicoUseCase {

	private final OrdemServicoGateway ordemServicoGateway;

	public ListarOrdensServicoUseCase(OrdemServicoGateway ordemServicoGateway) {
		this.ordemServicoGateway = ordemServicoGateway;
	}

	public ResultadoPaginado<OrdemServicoResumoOutput> executar(ListarOrdensServicoQuery query) {
		ResultadoPaginado<OrdemServico> resultado = ordemServicoGateway.filtrar(query);

		return resultado.map(this::toOutput);
	}

	private OrdemServicoResumoOutput toOutput(
			OrdemServico ordemServico
	) {
		return new OrdemServicoResumoOutput(
				ordemServico.getId(),
				ordemServico.getDocumentoCliente(),
				ordemServico.getPlacaVeiculo(),
				ordemServico.getDescricaoProblema(),
				ordemServico.getOrdemDeServicoStatus(),
				ordemServico.getDtHoraAbertura(),
				ordemServico.getValorServicos(),
				ordemServico.getValorPecasInsumos(),
				ordemServico.getValorOs()
		);
	}
}