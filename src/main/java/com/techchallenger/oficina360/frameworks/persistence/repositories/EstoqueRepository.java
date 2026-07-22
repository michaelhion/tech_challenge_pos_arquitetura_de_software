package com.techchallenger.oficina360.frameworks.persistence.repositories;

import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstoqueRepository extends JpaRepository<EstoqueEntity, UUID> {
    Optional<EstoqueEntity> findByCodigo(String codigo);
    List<EstoqueEntity> findByCodigoIn(List<String> codigos);
    List<EstoqueEntity> findByIdIn(List<UUID> id);
    void deleteByCodigo(String codigo);
}
