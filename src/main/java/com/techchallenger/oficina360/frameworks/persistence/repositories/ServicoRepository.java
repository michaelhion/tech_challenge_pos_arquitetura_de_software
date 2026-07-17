package com.techchallenger.oficina360.frameworks.persistence.repositories;

import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<ServicoEntity, UUID> {
    Optional<ServicoEntity> findByCodigo(String codigo);
    void deleteByCodigo(String codigo);
    List<ServicoEntity> findByCodigoIn(List<String> codigos);
}
