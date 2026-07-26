package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.frameworks.persistence.specifications.OrdemServicoSpecification;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import com.techchallenger.oficina360.usecases.ordemservico.query.OrdemServicoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.DirecaoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.ResultadoPaginado;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public ResultadoPaginado<OrdemServico> filtrar(ListarOrdensServicoQuery query) {
        Sort sort = criarOrdenacao(query);
        Pageable pageable = PageRequest.of(
                query.pagina(),
                query.tamanho(),
                sort
        );

        Specification<OrdemServicoEntity> specification = OrdemServicoSpecification.filtrar(query);

        Page<OrdemServico> dominios = repository.findAll(specification, pageable)
                        .map(mapper::toDomain);

        return new ResultadoPaginado<>(
                dominios.getContent(),
                dominios.getNumber(),
                dominios.getSize(),
                dominios.getTotalElements(),
                dominios.getTotalPages(),
                dominios.isFirst(),
                dominios.isLast(),
                dominios.hasNext()
        );
    }

    private Sort criarOrdenacao(ListarOrdensServicoQuery query) {
        String propriedade = obterPropriedade(query.ordenarPor());

        Sort.Direction direcao = query.direcao() == DirecaoOrdenacao.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        return Sort.by(new Sort.Order(direcao, propriedade),Sort.Order.asc("id"));
    }

    private String obterPropriedade(OrdemServicoOrdenacao ordenarPor) {
        return switch (ordenarPor) {
            case DATA_ABERTURA -> "dtHoraAbertura";
            case VALOR_TOTAL -> "valorOs";
            case STATUS -> "ordemDeServicoStatus";
            case PLACA -> "placaVeiculo";
        };
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
