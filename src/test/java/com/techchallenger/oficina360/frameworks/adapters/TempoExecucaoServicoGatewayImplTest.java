package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.TempoExecucaoServico;
import com.techchallenger.oficina360.frameworks.mappers.tempoexecucaoservico.TempoExecucaoServicoMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.TempoExecucaoServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.TempoExecucaoServicoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TempoExecucaoServicoGatewayImplTest {

	private static final UUID TEMPO_EXECUCAO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID OUTRO_TEMPO_EXECUCAO_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final UUID SERVICO_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	@Mock
	private TempoExecucaoServicoRepository repository;

	@Mock
	private TempoExecucaoServicoMapper mapper;

	@Mock
	private TempoExecucaoServico tempoExecucaoServico;

	@Mock
	private TempoExecucaoServico outroTempoExecucaoServico;

	@Mock
	private TempoExecucaoServicoEntity entity;

	@Mock
	private TempoExecucaoServicoEntity outraEntity;

	@Mock
	private TempoExecucaoServicoEntity entityPersistida;

	@InjectMocks
	private TempoExecucaoServicoGatewayImpl gateway;

	@Test
	void deveCalcularTempoMedioDoServico() {
		Double mediaEsperada = 45.5;

		when(repository.calcularTempoMedio(SERVICO_ID)).thenReturn(mediaEsperada);

		Double resultado = gateway.calcularTempoMedio(SERVICO_ID);

		assertEquals(mediaEsperada, resultado);

		verify(repository).calcularTempoMedio(SERVICO_ID);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarNullQuandoNaoExistirTempoMedio() {
		when(repository.calcularTempoMedio(SERVICO_ID)).thenReturn(null);

		Double resultado = gateway.calcularTempoMedio(SERVICO_ID);

		assertNull(resultado);

		verify(repository).calcularTempoMedio(SERVICO_ID);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveSalvarTempoExecucaoServicoComSucesso() {
		when(mapper.toEntity(tempoExecucaoServico)).thenReturn(entity);

		when(repository.save(entity)).thenReturn(entityPersistida);

		when(mapper.toDomain(entityPersistida)).thenReturn(tempoExecucaoServico);

		TempoExecucaoServico resultado = gateway.save(tempoExecucaoServico);

		assertSame(tempoExecucaoServico, resultado);

		verify(mapper).toEntity(tempoExecucaoServico);

		verify(repository).save(entity);

		verify(mapper).toDomain(entityPersistida);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveConverterPersistirEConverterTempoNestaOrdem() {
		when(mapper.toEntity(tempoExecucaoServico)).thenReturn(entity);

		when(repository.save(entity)).thenReturn(entityPersistida);

		when(mapper.toDomain(entityPersistida)).thenReturn(tempoExecucaoServico);

		gateway.save(tempoExecucaoServico);

		InOrder ordemDasChamadas = inOrder(mapper, repository);

		ordemDasChamadas.verify(mapper).toEntity(tempoExecucaoServico);

		ordemDasChamadas.verify(repository).save(entity);

		ordemDasChamadas.verify(mapper).toDomain(entityPersistida);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void deveListarTodosOsTemposDeExecucao() {
		when(repository.findAll()).thenReturn(List.of(entity, outraEntity));

		when(mapper.toDomain(entity)).thenReturn(tempoExecucaoServico);

		when(mapper.toDomain(outraEntity)).thenReturn(outroTempoExecucaoServico);

		List<TempoExecucaoServico> resultado = gateway.findAll(tempoExecucaoServico);

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(tempoExecucaoServico, resultado.get(0)),
				() -> assertSame(outroTempoExecucaoServico, resultado.get(1)));

		verify(repository).findAll();

		verify(mapper).toDomain(entity);

		verify(mapper).toDomain(outraEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void devePreservarOrdemRetornadaPeloRepository() {
		when(repository.findAll()).thenReturn(List.of(outraEntity, entity));

		when(mapper.toDomain(outraEntity)).thenReturn(outroTempoExecucaoServico);

		when(mapper.toDomain(entity)).thenReturn(tempoExecucaoServico);

		List<TempoExecucaoServico> resultado = gateway.findAll(tempoExecucaoServico);

		assertAll(() -> assertEquals(2, resultado.size()),
				() -> assertSame(outroTempoExecucaoServico, resultado.get(0)),
				() -> assertSame(tempoExecucaoServico, resultado.get(1)));

		verify(repository).findAll();

		verify(mapper).toDomain(outraEntity);

		verify(mapper).toDomain(entity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremTemposDeExecucao() {
		when(repository.findAll()).thenReturn(List.of());

		List<TempoExecucaoServico> resultado = gateway.findAll(tempoExecucaoServico);

		assertTrue(resultado.isEmpty());

		verify(repository).findAll();

		verify(mapper, never()).toDomain(any(TempoExecucaoServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveIgnorarParametroAoListarTemposDeExecucao() {
		when(repository.findAll()).thenReturn(List.of(entity));

		when(mapper.toDomain(entity)).thenReturn(tempoExecucaoServico);

		List<TempoExecucaoServico> resultado = gateway.findAll(outroTempoExecucaoServico);

		assertEquals(1, resultado.size());

		assertSame(tempoExecucaoServico, resultado.get(0));

		verify(repository).findAll();

		verify(mapper).toDomain(entity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarTempoExecucaoPorIdComSucesso() {
		when(repository.findById(TEMPO_EXECUCAO_ID)).thenReturn(Optional.of(entity));

		when(mapper.toDomain(entity)).thenReturn(tempoExecucaoServico);

		Optional<TempoExecucaoServico> resultado = gateway.findById(TEMPO_EXECUCAO_ID);

		assertTrue(resultado.isPresent());

		assertSame(tempoExecucaoServico, resultado.orElseThrow());

		verify(repository).findById(TEMPO_EXECUCAO_ID);

		verify(mapper).toDomain(entity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarVazioQuandoTempoExecucaoNaoExistirPorId() {
		when(repository.findById(OUTRO_TEMPO_EXECUCAO_ID)).thenReturn(Optional.empty());

		Optional<TempoExecucaoServico> resultado = gateway.findById(OUTRO_TEMPO_EXECUCAO_ID);

		assertTrue(resultado.isEmpty());

		verify(repository).findById(OUTRO_TEMPO_EXECUCAO_ID);

		verify(mapper, never()).toDomain(any(TempoExecucaoServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}
}