package com.techchallenger.oficina360.usecases.factories;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.gateways.Relogio;
import com.techchallenger.oficina360.usecases.ordemservico.command.CriarOrdemServicoCommand;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class OrdemServicoFactory {

    private final Relogio relogio;

	public OrdemServicoFactory(Relogio relogio) {
		this.relogio = relogio;
	}

	public OrdemServico criar(CriarOrdemServicoCommand command, String documento, String placa) {
        return new OrdemServico(
                documento,
                placa,
                command.descricaoProblema(),
                relogio.agora(),
                OrdemDeServicoStatus.RECEBIDA);
    }
}