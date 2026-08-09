package com.techchallenger.oficina360.usecases.services;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dominio.TempoExecucaoServico;
import com.techchallenger.oficina360.gateways.Relogio;
import com.techchallenger.oficina360.gateways.TempoExecucaoServicoGateway;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.AMERICA_SAO_PAULO;

public class TempoExecucaoService {

	private final TempoExecucaoServicoGateway tempoExecucaoServicoGateway;
	private final Relogio relogio;

	public TempoExecucaoService(TempoExecucaoServicoGateway tempoExecucaoServicoGateway, Relogio relogio) {
		this.tempoExecucaoServicoGateway = tempoExecucaoServicoGateway;
		this.relogio = relogio;
	}

	public void registrar(OrdemServico ordemServico) {

		ZoneId zoneId = ZoneId.of("America/Sao_Paulo");

		int tempoTotal = Math.toIntExact(Duration.between(ordemServico.getDtHoraInicioExecucao().atZone(zoneId),
				ordemServico.getDtHoraFimExecucao().atZone(zoneId)).toMinutes());

		int quantidadeServicos = ordemServico.getServicos().size();

		int tempoPorServico = quantidadeServicos > 0 ? tempoTotal / quantidadeServicos : tempoTotal;

		for (OrdemServicoServico oss : ordemServico.getServicos()) {
			tempoExecucaoServicoGateway.save(buildTempoExecucaoServico(oss, tempoPorServico));
		}
	}

	private TempoExecucaoServico buildTempoExecucaoServico(OrdemServicoServico oss, int tempoPorServico) {
		return new TempoExecucaoServico(oss.getServicoId(), tempoPorServico,
				relogio.agora());
	}
}
