package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.frameworks.mappers.estoque.EstoqueDTOMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.EstoqueRepository;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoConcorrenciaEstoqueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueGatewayImplTest {

	private static final UUID PRIMEIRO_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final UUID SEGUNDO_ID = UUID.fromString("3c4efe7e-3f54-5a3f-9fb4-37825c2409f9");

	private static final String CODIGO = "FILTRO-DE-OLEO";

	private static final String OUTRO_CODIGO = "PASTILHA-DE-FREIO";

	@Mock
	private EstoqueDTOMapper mapper;

	@Mock
	private EstoqueRepository repository;

	@Mock
	private Estoque estoque;

	@Mock
	private Estoque outroEstoque;

	@Mock
	private Estoque novoEstoque;

	@Mock
	private EstoqueEntity entity;

	@Mock
	private EstoqueEntity outraEntity;

	@Mock
	private EstoqueEntity entityPersistida;

	@InjectMocks
	private EstoqueGatewayImpl gateway;

	@Test
	void deveBuscarEstoquePorCodigoComSucesso() {
		when(repository.findByCodigo(CODIGO)).thenReturn(Optional.of(entity));

		when(mapper.toDomain(entity)).thenReturn(estoque);

		Optional<Estoque> resultado = gateway.findByCodigo(CODIGO);

		assertTrue(resultado.isPresent());
		assertSame(estoque, resultado.orElseThrow());

		verify(repository).findByCodigo(CODIGO);
		verify(mapper).toDomain(entity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarVazioQuandoCodigoNaoExistir() {
		when(repository.findByCodigo(CODIGO)).thenReturn(Optional.empty());

		Optional<Estoque> resultado = gateway.findByCodigo(CODIGO);

		assertTrue(resultado.isEmpty());

		verify(repository).findByCodigo(CODIGO);
		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarEstoquesPorListaDeCodigos() {
		List<String> codigos = List.of(CODIGO, OUTRO_CODIGO);

		when(repository.findByCodigoIn(codigos)).thenReturn(List.of(entity, outraEntity));

		when(mapper.toDomain(entity)).thenReturn(estoque);

		when(mapper.toDomain(outraEntity)).thenReturn(outroEstoque);

		List<Estoque> resultado = gateway.findByCodigoIn(codigos);

		assertEquals(2, resultado.size());
		assertSame(estoque, resultado.get(0));
		assertSame(outroEstoque, resultado.get(1));

		verify(repository).findByCodigoIn(codigos);
		verify(mapper).toDomain(entity);
		verify(mapper).toDomain(outraEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarEstoquesPorListaDeIds() {
		List<UUID> ids = List.of(PRIMEIRO_ID, SEGUNDO_ID);

		when(repository.findByIdIn(ids)).thenReturn(List.of(entity, outraEntity));

		when(mapper.toDomain(entity)).thenReturn(estoque);

		when(mapper.toDomain(outraEntity)).thenReturn(outroEstoque);

		List<Estoque> resultado = gateway.findByIdIn(ids);

		assertEquals(2, resultado.size());
		assertSame(estoque, resultado.get(0));
		assertSame(outroEstoque, resultado.get(1));

		verify(repository).findByIdIn(ids);
		verify(mapper).toDomain(entity);
		verify(mapper).toDomain(outraEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveExcluirEstoquePorCodigo() {
		gateway.deleteByCodigo(CODIGO);

		verify(repository).deleteByCodigo(CODIGO);
		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveCriarNovoItemDeEstoque() {
		when(novoEstoque.getId()).thenReturn(null);

		when(mapper.toEntity(novoEstoque)).thenReturn(entity);

		when(repository.saveAndFlush(entity)).thenReturn(entityPersistida);

		when(mapper.toDomain(entityPersistida)).thenReturn(estoque);

		Estoque resultado = gateway.save(novoEstoque);

		assertSame(estoque, resultado);

		verify(novoEstoque).getId();
		verify(mapper).toEntity(novoEstoque);
		verify(repository).saveAndFlush(entity);
		verify(mapper).toDomain(entityPersistida);

		verify(repository, never()).findById(any());
		verify(repository, never()).flush();
	}

	@Test
	void deveAtualizarItemDeEstoqueExistente() {
		when(estoque.getId()).thenReturn(PRIMEIRO_ID);

		when(repository.findById(PRIMEIRO_ID)).thenReturn(Optional.of(entity));

		when(mapper.toDomain(entity)).thenReturn(estoque);

		Estoque resultado = gateway.save(estoque);

		assertSame(estoque, resultado);

		verify(estoque,times(2)).getId();
		verify(repository).findById(PRIMEIRO_ID);
		verify(mapper).updateEntityFromDomain(estoque, entity);
		verify(repository).flush();
		verify(mapper).toDomain(entity);

		verify(mapper, never()).toEntity(estoque);
		verify(repository, never()).saveAndFlush(any(EstoqueEntity.class));
	}

	@Test
	void deveLancarExcecaoQuandoItemParaAtualizacaoNaoExistir() {
		when(estoque.getId()).thenReturn(PRIMEIRO_ID);

		when(repository.findById(PRIMEIRO_ID)).thenReturn(Optional.empty());

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> gateway.save(estoque));

		assertEquals("Item de estoque não encontrado: " + PRIMEIRO_ID, exception.getMessage());

		verify(repository).findById(PRIMEIRO_ID);

		verify(mapper, never()).updateEntityFromDomain(any(Estoque.class), any(EstoqueEntity.class));

		verify(repository, never()).flush();
	}

	@Test
	void deveConverterConcorrenciaOtimistaAoAtualizarEstoque() {
		ObjectOptimisticLockingFailureException causa = new ObjectOptimisticLockingFailureException(EstoqueEntity.class,
				PRIMEIRO_ID);

		when(estoque.getId()).thenReturn(PRIMEIRO_ID);

		when(repository.findById(PRIMEIRO_ID)).thenReturn(Optional.of(entity));

		doThrow(causa).when(repository).flush();

		ConflitoConcorrenciaEstoqueException exception = assertThrows(ConflitoConcorrenciaEstoqueException.class,
				() -> gateway.save(estoque));

		assertSame(causa, exception.getCause());

		verify(repository).findById(PRIMEIRO_ID);
		verify(mapper).updateEntityFromDomain(estoque, entity);
		verify(repository).flush();
		verify(mapper, never()).toDomain(entity);
	}

	@Test
	void deveSalvarTodosOsItensDeEstoque() {
		List<Estoque> estoques = List.of(estoque, outroEstoque);

		List<UUID> ids = List.of(PRIMEIRO_ID, SEGUNDO_ID);

		when(estoque.getId()).thenReturn(PRIMEIRO_ID);

		when(outroEstoque.getId()).thenReturn(SEGUNDO_ID);

		when(entity.getId()).thenReturn(PRIMEIRO_ID);

		when(outraEntity.getId()).thenReturn(SEGUNDO_ID);

		when(repository.findAllById(ids)).thenReturn(List.of(entity, outraEntity));

		when(mapper.toDomain(entity)).thenReturn(estoque);

		when(mapper.toDomain(outraEntity)).thenReturn(outroEstoque);

		List<Estoque> resultado = gateway.saveAll(estoques);

		assertEquals(2, resultado.size());
		assertSame(estoque, resultado.get(0));
		assertSame(outroEstoque, resultado.get(1));

		verify(repository).findAllById(ids);
		verify(mapper).updateEntityFromDomain(estoque, entity);
		verify(mapper).updateEntityFromDomain(outroEstoque, outraEntity);
		verify(repository).flush();
		verify(mapper).toDomain(entity);
		verify(mapper).toDomain(outraEntity);
	}

	@Test
	void deveLancarExcecaoQuandoItemDoLoteNaoExistir() {
		List<Estoque> estoques = List.of(estoque, outroEstoque);

		List<UUID> ids = List.of(PRIMEIRO_ID, SEGUNDO_ID);

		when(estoque.getId()).thenReturn(PRIMEIRO_ID);

		when(outroEstoque.getId()).thenReturn(SEGUNDO_ID);

		when(entity.getId()).thenReturn(PRIMEIRO_ID);

		when(repository.findAllById(ids)).thenReturn(List.of(entity));

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> gateway.saveAll(estoques));

		assertEquals("Item de estoque não encontrado: " + SEGUNDO_ID, exception.getMessage());

		verify(repository).findAllById(ids);
		verify(mapper).updateEntityFromDomain(estoque, entity);

		verify(mapper, never()).updateEntityFromDomain(outroEstoque, outraEntity);

		verify(repository, never()).flush();
	}

	@Test
	void deveConverterConcorrenciaOtimistaAoSalvarLote() {
		ObjectOptimisticLockingFailureException causa = new ObjectOptimisticLockingFailureException(EstoqueEntity.class,
				PRIMEIRO_ID);

		List<Estoque> estoques = List.of(estoque);

		List<UUID> ids = List.of(PRIMEIRO_ID);

		when(estoque.getId()).thenReturn(PRIMEIRO_ID);

		when(entity.getId()).thenReturn(PRIMEIRO_ID);

		when(repository.findAllById(ids)).thenReturn(List.of(entity));

		doThrow(causa).when(repository).flush();

		ConflitoConcorrenciaEstoqueException exception = assertThrows(ConflitoConcorrenciaEstoqueException.class,
				() -> gateway.saveAll(estoques));

		assertSame(causa, exception.getCause());

		verify(repository).findAllById(ids);
		verify(mapper).updateEntityFromDomain(estoque, entity);
		verify(repository).flush();
		verify(mapper, never()).toDomain(entity);
	}

	@Test
	void deveListarTodosOsItensDeEstoque() {
		when(repository.findAll()).thenReturn(List.of(entity, outraEntity));

		when(mapper.toDomain(entity)).thenReturn(estoque);

		when(mapper.toDomain(outraEntity)).thenReturn(outroEstoque);

		List<Estoque> resultado = gateway.findAll();

		assertEquals(2, resultado.size());
		assertSame(estoque, resultado.get(0));
		assertSame(outroEstoque, resultado.get(1));

		verify(repository).findAll();
		verify(mapper).toDomain(entity);
		verify(mapper).toDomain(outraEntity);
	}

	@Test
	void deveRetornarListaVaziaQuandoEstoqueEstiverVazio() {
		when(repository.findAll()).thenReturn(List.of());

		List<Estoque> resultado = gateway.findAll();

		assertTrue(resultado.isEmpty());

		verify(repository).findAll();
		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarEstoquePorIdComSucesso() {
		when(repository.findById(PRIMEIRO_ID)).thenReturn(Optional.of(entity));

		when(mapper.toDomain(entity)).thenReturn(estoque);

		Optional<Estoque> resultado = gateway.findById(PRIMEIRO_ID);

		assertTrue(resultado.isPresent());
		assertSame(estoque, resultado.orElseThrow());

		verify(repository).findById(PRIMEIRO_ID);
		verify(mapper).toDomain(entity);
	}

	@Test
	void deveRetornarVazioQuandoEstoqueNaoExistirPorId() {
		when(repository.findById(PRIMEIRO_ID)).thenReturn(Optional.empty());

		Optional<Estoque> resultado = gateway.findById(PRIMEIRO_ID);

		assertFalse(resultado.isPresent());

		verify(repository).findById(PRIMEIRO_ID);
		verifyNoMoreInteractions(repository, mapper);
	}
}