package com.techchallenger.oficina360.repositories;

import com.techchallenger.oficina360.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByDocumento(String documento);
    void deleteByDocumento(String documento);
    boolean existsByDocumento(String documento);
}
