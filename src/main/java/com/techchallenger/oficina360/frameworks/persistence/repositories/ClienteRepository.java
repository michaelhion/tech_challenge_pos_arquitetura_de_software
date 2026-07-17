package com.techchallenger.oficina360.frameworks.persistence.repositories;

import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<ClienteEntity, UUID> {
    Optional<ClienteEntity> findByDocumento(String documento);
    void deleteByDocumento(String documento);
    boolean existsByDocumento(String documento);
}
