package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoServicoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrdemServicoServicoMapper {

	public OrdemServicoServico toDomain(OrdemServicoServicoEntity entity) {

		return new OrdemServicoServico(
				entity.getId(),
				entity.getServicoId(),
				entity.getDescricao(),
				entity.getValor()
		);
	}

	public OrdemServicoServicoEntity toEntity(OrdemServicoServico domain) {
		if (domain == null) {
			return null;
		}

		return OrdemServicoServicoEntity.builder()
				.id(domain.getId())
				.servicoId(domain.getServicoId())
				.descricao(domain.getDescricao())
				.valor(domain.getValor())
				.build();
	}

	public List<OrdemServicoServico> toDomainList(List<OrdemServicoServicoEntity> entities) {

		if (entities == null) {
			return List.of();
		}

		return entities.stream().map(this::toDomain).toList();
	}

	public List<OrdemServicoServicoEntity> toEntityList(List<OrdemServicoServico> entities) {

		if (entities == null) {
			return List.of();
		}

		return entities.stream().map(this::toEntity).toList();
	}
}