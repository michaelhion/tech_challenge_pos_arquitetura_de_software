package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrdemServicoGateway {

    Optional<OrdemServico> findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(String placaVeiculo, Collection<OrdemDeServicoStatus> status);

    OrdemServico save(OrdemServico ordemServico);

    List<OrdemServico> findAll();

    Optional<OrdemServico> findById(UUID id);

    void deleteById(UUID id);
}
