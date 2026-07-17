package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.Cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteGateway {
    Optional<Cliente> findByDocumento(String documento);
    void deleteByDocumento(String documento);
    boolean existsByDocumento(String documento);

    Cliente save(Cliente cliente);

    List<Cliente> findAll();

    Optional<Cliente> findById(UUID id);
    void deleteById(UUID id);
}
