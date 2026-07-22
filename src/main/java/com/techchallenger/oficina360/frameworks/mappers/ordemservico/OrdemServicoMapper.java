package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class OrdemServicoMapper {

    private final OrdemServicoItemEstoqueMapper ordemServicoItemEstoqueMapper;
    private final OrdemServicoServicoMapper ordemServicoServicoMapper;

    public OrdemServico toDomain(OrdemServicoEntity entity) {

        if (entity == null) {
            return null;
        }

        return new OrdemServico(
                entity.getId(),
                entity.getDocumentoCliente(),
                entity.getPlacaVeiculo(),
                entity.getDtHoraAbertura(),
                entity.getDtHoraFechamento(),
                entity.getDescricaoProblema(),
                entity.getOrdemDeServicoStatus(),
                entity.getObservacaoCliente(),
                ordemServicoServicoMapper.toDomainList(entity.getServicos()),
                ordemServicoItemEstoqueMapper.toDomainList(entity.getItensEstoque()),
                entity.getDtHoraInicioExecucao(),
                entity.getDtHoraFimExecucao());
    }

    public OrdemServicoEntity toEntity(
            OrdemServico domain
    ) {
        if (domain == null) {
            return null;
        }

        OrdemServicoEntity entity = OrdemServicoEntity.builder()
                        .id(domain.getId())
                        .documentoCliente(domain.getDocumentoCliente())
                        .placaVeiculo(domain.getPlacaVeiculo())
                        .dtHoraAbertura(domain.getDtHoraAbertura())
                        .dtHoraFechamento(domain.getDtHoraFechamento())
                        .descricaoProblema(domain.getDescricaoProblema())
                        .ordemDeServicoStatus(domain.getOrdemDeServicoStatus())
                        .observacaoCliente(domain.getObservacaoCliente())
                        .valorServicos(domain.getValorServicos())
                        .valorPecasInsumos(domain.getValorPecasInsumos())
                        .valorOs(domain.getValorOs())
                        .dtHoraInicioExecucao(domain.getDtHoraInicioExecucao())
                        .dtHoraFimExecucao(domain.getDtHoraFimExecucao())
                        .servicos(new ArrayList<>())
                        .itensEstoque(new ArrayList<>())
                        .build();

        domain.getServicos()
                .stream()
                .map(ordemServicoServicoMapper::toEntity)
                .forEach(entity::adicionarServico);

        domain.getItensEstoque()
                .stream()
                .map(ordemServicoItemEstoqueMapper::toEntity)
                .forEach(entity::adicionarItemEstoque);

        return entity;
    }


}