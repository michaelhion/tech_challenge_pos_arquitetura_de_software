package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.frameworks.mappers.veiculo.VeiculoMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.VeiculoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.VeiculoRepository;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class VeiculoGatewayImpl implements VeiculoGateway {

    private final VeiculoMapper veiculoMapper;
    private final VeiculoRepository repository;

    @Override
    public Optional<Veiculo> findByPlaca(String placa){
        return repository
                .findByPlaca(placa)
                .map(veiculoMapper::toDomain);
    }

    @Override
    public boolean existsByPlaca(String placa){
        return repository.existsByPlaca(placa);
    }

    @Override
    public boolean existsByPlacaAndIdNot(String placa, UUID id){
        return repository.existsByPlacaAndIdNot(placa,id);
    }

    @Override
    public void deleteByPlaca(String placa){
        repository.deleteByPlaca(placa);
    }

    @Override
    public Veiculo save(Veiculo veiculo){
        VeiculoEntity entity = veiculoMapper.toEntity(veiculo);
        VeiculoEntity persisted = repository.save(entity);
        return veiculoMapper.toDomain(persisted);
    }

    @Override
    public List<Veiculo> findAll(Veiculo veiculo){
        return repository.findAll()
                .stream()
                .map(veiculoMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Veiculo> findById(UUID id){
        return repository.findById(id)
                .map(veiculoMapper::toDomain);
    }
}
