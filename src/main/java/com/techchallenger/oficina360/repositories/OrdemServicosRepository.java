package com.techchallenger.oficina360.repositories;

import com.techchallenger.oficina360.entities.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdemServicosRepository extends JpaRepository<OrdemServico, UUID> {

    Optional<OrdemServico> findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(
            String placaVeiculo,
            Collection<OrdemDeServicoStatus> status
    );

}
