package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.frameworks.mappers.estoque.EstoqueMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.EstoqueRepository;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EstoqueGatewayImpl implements EstoqueGateway {

    private final EstoqueMapper mapper;
    private final EstoqueRepository repository;

    @Override
    public Optional<Estoque> findByCodigo(String codigo){
        return repository
                .findByCodigo(codigo)
                .map(mapper::toDomain);
    }

    @Override
    public List<Estoque> findByCodigoIn(List<String> codigos){
        return repository.findByCodigoIn(codigos)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByCodigo(String codigo){
        repository.deleteByCodigo(codigo);
    }

    @Override
    public Estoque save(Estoque estoque){
        EstoqueEntity entity = mapper.toEntity(estoque);
        EstoqueEntity persisted = repository.save(entity);
        return mapper.toDomain(persisted);
    }

    @Override
    public List<Estoque> saveAll(List<Estoque> estoques) {
        List<EstoqueEntity> estoqueEntities = mapper.domainListToEntityList(estoques);
        List<EstoqueEntity> estoqueEntitiesSaved = repository.saveAll(estoqueEntities);
        return mapper.entityListToDomainList(estoqueEntitiesSaved);
    }

    @Override
    public List<Estoque> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Estoque> findById(UUID id){
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
