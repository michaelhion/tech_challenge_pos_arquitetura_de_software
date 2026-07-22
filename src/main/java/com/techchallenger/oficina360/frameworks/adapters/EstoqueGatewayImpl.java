package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.frameworks.mappers.estoque.EstoqueMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.EstoqueRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoConcorrenciaEstoqueException;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EstoqueGatewayImpl implements EstoqueGateway {

	private final EstoqueMapper mapper;
	private final EstoqueRepository repository;

	@Override
	public Optional<Estoque> findByCodigo(String codigo) {
		return repository.findByCodigo(codigo).map(mapper::toDomain);
	}

	@Override
	public List<Estoque> findByCodigoIn(List<String> codigos) {
		return repository.findByCodigoIn(codigos).stream().map(mapper::toDomain).toList();
	}

	@Override
	public List<Estoque> findByIdIn(List<UUID> ids) {
		return repository.findByIdIn(ids).stream().map(mapper::toDomain).toList();
	}

	@Override
	public void deleteByCodigo(String codigo) {
		repository.deleteByCodigo(codigo);
	}

	@Override
	@Transactional
	public Estoque save(Estoque estoque) {
		try {
			EstoqueEntity entity;

			if (estoque.getId() == null) {
				entity = mapper.toEntity(estoque);
				entity = repository.saveAndFlush(entity);
			} else {
				entity = buscarEntidadeOuFalhar(estoque.getId());

				mapper.updateEntityFromDomain(estoque, entity);

				repository.flush();
			}

			return mapper.toDomain(entity);

		} catch (ObjectOptimisticLockingFailureException exception) {
			throw new ConflitoConcorrenciaEstoqueException(estoque.getId(), exception);
		}
	}

	@Override
	@Transactional
	public List<Estoque> saveAll(List<Estoque> estoques) {
		try {
			List<UUID> ids = estoques.stream().map(Estoque::getId).toList();

			Map<UUID, EstoqueEntity> entitiesPorId = repository.findAllById(ids).stream()
					.collect(Collectors.toMap(EstoqueEntity::getId, Function.identity()));

			List<EstoqueEntity> entitiesAtualizadas = estoques.stream().map(estoque -> {
				EstoqueEntity entity = Optional.ofNullable(entitiesPorId.get(estoque.getId())).orElseThrow(
						() -> new IllegalStateException("Item de estoque não encontrado: " + estoque.getId()));

				mapper.updateEntityFromDomain(estoque, entity);

				return entity;
			}).toList();

			repository.flush();

			return entitiesAtualizadas.stream().map(mapper::toDomain).toList();

		} catch (ObjectOptimisticLockingFailureException exception) {
			throw new ConflitoConcorrenciaEstoqueException(null, exception);
		}
	}

	@Override
	public List<Estoque> findAll() {
		return repository.findAll().stream().map(mapper::toDomain).toList();
	}

	@Override
	public Optional<Estoque> findById(UUID id) {
		return repository.findById(id).map(mapper::toDomain);
	}

	private EstoqueEntity buscarEntidadeOuFalhar(UUID estoqueId) {
		return repository.findById(estoqueId)
				.orElseThrow(() -> new IllegalStateException("Item de estoque não encontrado: " + estoqueId));
	}
}