package com.techchallenger.oficina360.frameworks.persistence.repositories;

import com.techchallenger.oficina360.frameworks.persistence.entities.TempoExecucaoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TempoExecucaoServicoRepository extends JpaRepository<TempoExecucaoServicoEntity, UUID> {

    @Query("""
        SELECT AVG(t.tempoExecucaoMinutos)
        FROM TempoExecucaoServicoEntity t
        WHERE t.servicoID = :servicoId
    """)
    Double calcularTempoMedio(@Param("servicoId") UUID servicoId);
}
