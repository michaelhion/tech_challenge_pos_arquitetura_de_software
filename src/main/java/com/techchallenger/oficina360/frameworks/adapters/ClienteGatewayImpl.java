package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.frameworks.mappers.cliente.ClienteMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ClienteRepository;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ClienteGatewayImpl implements ClienteGateway {

    private final ClienteMapper mapper;
    private final ClienteRepository repository;

    @Override
    public Optional<Cliente> findByDocumento(String documento){
        return repository
                .findByDocumento(documento)
                .map(mapper::toDomain);
    }
    @Override
    public void deleteByDocumento(String documento){
        repository.deleteByDocumento(documento);
    }

    @Override
    public boolean existsByDocumento(String documento){
        return repository.existsByDocumento(documento);
    }

    @Override
    public Cliente save(Cliente cliente){
        ClienteEntity entity = mapper.toEntity(cliente);
        ClienteEntity persisted = repository.save(entity);
        return mapper.toDomain(persisted);
    }

    @Override
    public List<Cliente> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Cliente> findById(UUID id){
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}
