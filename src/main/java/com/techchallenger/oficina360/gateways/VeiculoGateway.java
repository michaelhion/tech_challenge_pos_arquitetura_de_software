package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.Veiculo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoGateway {
    Optional<Veiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

    boolean existsByPlacaAndIdNot(String placa, UUID id);

    void deleteByPlaca(String placa);

    Veiculo save(Veiculo veiculo);

    List<Veiculo> findAll(Veiculo veiculo);

    Optional<Veiculo> findById(UUID id);
}
