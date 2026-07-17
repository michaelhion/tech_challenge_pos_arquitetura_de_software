package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.Estoque;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EstoqueGateway {
    Optional<Estoque> findByCodigo(String codigo);
    List<Estoque> findByCodigoIn(List<String> codigos);
    void deleteByCodigo(String codigo);

    Estoque save(Estoque estoque);
    List<Estoque> saveAll(List<Estoque> estoques);

    List<Estoque> findAll();

    Optional<Estoque> findById(UUID id);
}
