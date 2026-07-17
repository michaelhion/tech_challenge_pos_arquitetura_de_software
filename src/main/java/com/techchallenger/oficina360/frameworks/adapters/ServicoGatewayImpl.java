package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.frameworks.mappers.servico.ServicoMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ServicoRepository;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ServicoGatewayImpl implements ServicoGateway {

    private final ServicoMapper mapper;
    private final ServicoRepository repository;

    @Override
    public Optional<Servico> findByCodigo(String codigo){
        return repository
                .findByCodigo(codigo)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteByCodigo(String codigo){
        repository.deleteByCodigo(codigo);
    }

    @Override
    public List<Servico> findByCodigoIn(List<String> codigos){
        return repository.findByCodigoIn(codigos)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Servico save(Servico servico){
        ServicoEntity entity = mapper.toEntity(servico);
        ServicoEntity persisted = repository.save(entity);
        return mapper.toDomain(persisted);
    }

    @Override
    public List<Servico> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Servico> findById(UUID id){
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
