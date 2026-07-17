package com.techchallenger.oficina360.frameworks.persistence.repositories;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdemServicosRepository extends JpaRepository<OrdemServicoEntity, UUID> {

    Optional<OrdemServicoEntity> findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(
            String placaVeiculo,
            Collection<OrdemDeServicoStatus> status
    );

}
