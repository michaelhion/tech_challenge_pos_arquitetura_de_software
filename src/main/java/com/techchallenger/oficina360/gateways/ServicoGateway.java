package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.Servico;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServicoGateway {
    Optional<Servico> findByCodigo(String codigo);
    void deleteByCodigo(String codigo);
    List<Servico> findByCodigoIn(List<String> codigos);

    Servico save(Servico servico);

    List<Servico> findAll();

    Optional<Servico> findById(UUID id);
}
