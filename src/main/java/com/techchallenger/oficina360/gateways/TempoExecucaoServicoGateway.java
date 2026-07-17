package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.TempoExecucaoServico;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TempoExecucaoServicoGateway {
    Double calcularTempoMedio(@Param("servicoId") UUID servicoId);

    TempoExecucaoServico save(TempoExecucaoServico tempoExecucaoServico);

    List<TempoExecucaoServico> findAll(TempoExecucaoServico tempoExecucaoServico);

    Optional<TempoExecucaoServico> findById(UUID id);
}
