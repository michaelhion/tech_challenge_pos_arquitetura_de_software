package com.techchallenger.oficina360.frameworks.persistence.repositories;

import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepository extends JpaRepository<VeiculoEntity, UUID> {

    Optional<VeiculoEntity> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, UUID id);

    void deleteByPlaca(String placa);
}