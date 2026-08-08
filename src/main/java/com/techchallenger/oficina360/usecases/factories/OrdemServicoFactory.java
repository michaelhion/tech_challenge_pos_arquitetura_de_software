package com.techchallenger.oficina360.usecases.factories;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.usecases.ordemservico.command.CriarOrdemServicoCommand;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class OrdemServicoFactory {

    public OrdemServico criar(CriarOrdemServicoCommand command, String documento, String placa) {
        return new OrdemServico(
                documento,
                placa,
                command.descricaoProblema(),
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),
                OrdemDeServicoStatus.RECEBIDA);
    }
}