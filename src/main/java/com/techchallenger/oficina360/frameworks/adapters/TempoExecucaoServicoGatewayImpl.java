package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.TempoExecucaoServico;
import com.techchallenger.oficina360.frameworks.mappers.tempoexecucaoservico.TempoExecucaoServicoMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.TempoExecucaoServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.TempoExecucaoServicoRepository;
import com.techchallenger.oficina360.gateways.TempoExecucaoServicoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TempoExecucaoServicoGatewayImpl implements TempoExecucaoServicoGateway {
    
    private final TempoExecucaoServicoRepository repository;
    private final TempoExecucaoServicoMapper mapper;

    @Override
    public Double calcularTempoMedio(@Param("servicoId") UUID servicoId){
        return repository.calcularTempoMedio(servicoId);
    }
    @Override
    public TempoExecucaoServico save(TempoExecucaoServico tempoExecucaoServico){
        TempoExecucaoServicoEntity entity = mapper.toEntity(tempoExecucaoServico);
        TempoExecucaoServicoEntity persisted = repository.save(entity);
        return mapper.toDomain(persisted);
    }

    @Override
    public List<TempoExecucaoServico> findAll(TempoExecucaoServico tempoExecucaoServico){
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<TempoExecucaoServico> findById(UUID id){
        return repository.findById(id)
                .map(mapper::toDomain);
    }
}
