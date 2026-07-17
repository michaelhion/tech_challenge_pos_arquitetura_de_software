package com.techchallenger.oficina360.usecases.factories;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class OrdemServicoFactory {

    public OrdemServico criar(CriarOrdemServicoDTO dto, String documento, String placa) {
        return new OrdemServico(
                documento,
                placa,
                dto.descricaoProblema(),
                LocalDateTime.now(ZoneId.of("America/Sao_Paulo")),
                OrdemDeServicoStatus.RECEBIDA);
    }
}