package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.frameworks.mappers.servico.ServicoDTOMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ServicoRepository;
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
class ServicoGatewayImplTest {

	private static final UUID SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID OUTRO_SERVICO_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final String CODIGO = "TROCA-DE-OLEO";

	private static final String OUTRO_CODIGO = "ALINHAMENTO";

	@Mock
	private ServicoDTOMapper mapper;

	@Mock
	private ServicoRepository repository;

	@Mock
	private Servico servico;

	@Mock
	private Servico outroServico;

	@Mock
	private ServicoEntity servicoEntity;

	@Mock
	private ServicoEntity outroServicoEntity;

	@Mock
	private ServicoEntity servicoEntityPersistido;

	@InjectMocks
	private ServicoGatewayImpl gateway;

	@Test
	void deveBuscarServicoPorCodigoComSucesso() {
		when(repository.findByCodigo(CODIGO)).thenReturn(Optional.of(servicoEntity));

		when(mapper.toDomain(servicoEntity)).thenReturn(servico);

		Optional<Servico> resultado = gateway.findByCodigo(CODIGO);

		assertTrue(resultado.isPresent());

		assertSame(servico, resultado.orElseThrow());

		verify(repository).findByCodigo(CODIGO);

		verify(mapper).toDomain(servicoEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarOptionalVazioQuandoServicoNaoExistirPorCodigo() {
		when(repository.findByCodigo(CODIGO)).thenReturn(Optional.empty());

		Optional<Servico> resultado = gateway.findByCodigo(CODIGO);

		assertTrue(resultado.isEmpty());

		verify(repository).findByCodigo(CODIGO);

		verify(mapper, never()).toDomain(any(ServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveExcluirServicoPorCodigo() {
		gateway.deleteByCodigo(CODIGO);

		verify(repository).deleteByCodigo(CODIGO);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarServicosPorListaDeCodigos() {
		List<String> codigos = List.of(CODIGO, OUTRO_CODIGO);

		when(repository.findByCodigoIn(codigos)).thenReturn(List.of(servicoEntity, outroServicoEntity));

		when(mapper.toDomain(servicoEntity)).thenReturn(servico);

		when(mapper.toDomain(outroServicoEntity)).thenReturn(outroServico);

		List<Servico> resultado = gateway.findByCodigoIn(codigos);

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(servico, resultado.get(0)),
				() -> assertSame(outroServico, resultado.get(1)));

		verify(repository).findByCodigoIn(codigos);

		verify(mapper).toDomain(servicoEntity);

		verify(mapper).toDomain(outroServicoEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarListaVaziaQuandoNenhumCodigoForEncontrado() {
		List<String> codigos = List.of(CODIGO, OUTRO_CODIGO);

		when(repository.findByCodigoIn(codigos)).thenReturn(List.of());

		List<Servico> resultado = gateway.findByCodigoIn(codigos);

		assertTrue(resultado.isEmpty());

		verify(repository).findByCodigoIn(codigos);

		verify(mapper, never()).toDomain(any(ServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveSalvarServicoComSucesso() {
		when(mapper.toEntity(servico)).thenReturn(servicoEntity);

		when(repository.save(servicoEntity)).thenReturn(servicoEntityPersistido);

		when(mapper.toDomain(servicoEntityPersistido)).thenReturn(servico);

		Servico resultado = gateway.save(servico);

		assertSame(servico, resultado);

		verify(mapper).toEntity(servico);

		verify(repository).save(servicoEntity);

		verify(mapper).toDomain(servicoEntityPersistido);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveConverterPersistirEConverterServicoNestaOrdem() {
		when(mapper.toEntity(servico)).thenReturn(servicoEntity);

		when(repository.save(servicoEntity)).thenReturn(servicoEntityPersistido);

		when(mapper.toDomain(servicoEntityPersistido)).thenReturn(servico);

		gateway.save(servico);

		InOrder ordemDasChamadas = inOrder(mapper, repository);

		ordemDasChamadas.verify(mapper).toEntity(servico);

		ordemDasChamadas.verify(repository).save(servicoEntity);

		ordemDasChamadas.verify(mapper).toDomain(servicoEntityPersistido);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void deveListarTodosOsServicos() {
		when(repository.findAll()).thenReturn(List.of(servicoEntity, outroServicoEntity));

		when(mapper.toDomain(servicoEntity)).thenReturn(servico);

		when(mapper.toDomain(outroServicoEntity)).thenReturn(outroServico);

		List<Servico> resultado = gateway.findAll();

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(servico, resultado.get(0)),
				() -> assertSame(outroServico, resultado.get(1)));

		verify(repository).findAll();

		verify(mapper).toDomain(servicoEntity);

		verify(mapper).toDomain(outroServicoEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void devePreservarOrdemRetornadaPeloRepository() {
		when(repository.findAll()).thenReturn(List.of(outroServicoEntity, servicoEntity));

		when(mapper.toDomain(outroServicoEntity)).thenReturn(outroServico);

		when(mapper.toDomain(servicoEntity)).thenReturn(servico);

		List<Servico> resultado = gateway.findAll();

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(outroServico, resultado.get(0)),
				() -> assertSame(servico, resultado.get(1)));

		verify(repository).findAll();

		verify(mapper).toDomain(outroServicoEntity);

		verify(mapper).toDomain(servicoEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremServicos() {
		when(repository.findAll()).thenReturn(List.of());

		List<Servico> resultado = gateway.findAll();

		assertTrue(resultado.isEmpty());

		verify(repository).findAll();

		verify(mapper, never()).toDomain(any(ServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarServicoPorIdComSucesso() {
		when(repository.findById(SERVICO_ID)).thenReturn(Optional.of(servicoEntity));

		when(mapper.toDomain(servicoEntity)).thenReturn(servico);

		Optional<Servico> resultado = gateway.findById(SERVICO_ID);

		assertTrue(resultado.isPresent());

		assertSame(servico, resultado.orElseThrow());

		verify(repository).findById(SERVICO_ID);

		verify(mapper).toDomain(servicoEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarOptionalVazioQuandoServicoNaoExistirPorId() {
		when(repository.findById(OUTRO_SERVICO_ID)).thenReturn(Optional.empty());

		Optional<Servico> resultado = gateway.findById(OUTRO_SERVICO_ID);

		assertTrue(resultado.isEmpty());

		verify(repository).findById(OUTRO_SERVICO_ID);

		verify(mapper, never()).toDomain(any(ServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}
}