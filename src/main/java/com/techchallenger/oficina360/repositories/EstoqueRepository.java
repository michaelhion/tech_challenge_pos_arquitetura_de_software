package com.techchallenger.oficina360.repositories;

import com.techchallenger.oficina360.entities.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, UUID> {
    Optional<Estoque> findByCodigo(String codigo);
    List<Estoque> findByCodigoIn(List<String> codigos);
}
