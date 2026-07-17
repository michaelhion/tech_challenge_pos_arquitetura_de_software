package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.gateways.TempoExecucaoServicoGateway;

import java.util.List;

public class ListarServicosUseCase {

	private final ServicoGateway servicoGateway;
	private final TempoExecucaoServicoGateway tempoExecucaoServicoGateway;

	public ListarServicosUseCase(ServicoGateway servicoGateway,
			TempoExecucaoServicoGateway tempoExecucaoServicoGateway) {
		this.servicoGateway = servicoGateway;
		this.tempoExecucaoServicoGateway = tempoExecucaoServicoGateway;
	}

	public List<ServicoDTO> findAll() {

		return servicoGateway.findAll()
				.stream()
				.map(this::toDTOComTempoMedio)
				.toList();
	}

	private ServicoDTO toDTOComTempoMedio(Servico servico) {

		Double media = tempoExecucaoServicoGateway
				.calcularTempoMedio(servico.getId());

		Integer tempoMedio = media != null
				? (int) Math.round(media)
				: 0;

		return new ServicoDTO(
				servico.getCodigo(),
				servico.getDescricao(),
				servico.getValor(),
				tempoMedio
		);
	}
}
