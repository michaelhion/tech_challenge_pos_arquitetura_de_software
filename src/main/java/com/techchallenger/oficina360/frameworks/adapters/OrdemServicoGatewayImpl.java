package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;

import com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrdemServicoGatewayImpl implements OrdemServicoGateway {

    private final OrdemServicosRepository repository;
    private final OrdemServicoMapper mapper;

    @Override
    public Optional<OrdemServico> findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(String placaVeiculo, Collection<OrdemDeServicoStatus> status) {
        return repository
                .findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(
                        placaVeiculo,
                        status)
                .map(mapper::toDomain);
    }

    @Override
    public OrdemServico save(OrdemServico ordemServico){
        OrdemServicoEntity entity = mapper.toEntity(ordemServico);
        OrdemServicoEntity persisted = repository.save(entity);

        return mapper.toDomain(persisted);
    }

    @Override
    public List<OrdemServico> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<OrdemServico> findById(UUID id){
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

}
