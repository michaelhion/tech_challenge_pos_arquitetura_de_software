package com.techchallenger.oficina360.services.factories;

import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.entities.Cliente;
import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.entities.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class OrdemServicoFactory {

    public OrdemServico criar(CriarOrdemServicoDTO dto, Cliente cliente, Veiculo veiculo) {
        return OrdemServico.builder()
                .documentoCliente(cliente.getDocumento())
                .placaVeiculo(veiculo.getPlaca())
                .descricaoProblema(dto.descricaoProblema())
                .dtHoraAbertura(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                .ordemDeServicoStatus(OrdemDeServicoStatus.RECEBIDA)
                .build();
    }
}