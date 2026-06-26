package com.techchallenger.oficina360.repositories;

import com.techchallenger.oficina360.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, UUID> {
    Optional<Servico> findByCodigo(String codigo);
    void deleteByCodigo(String codigo);
    List<Servico> findByCodigoIn(List<String> codigos);
}
