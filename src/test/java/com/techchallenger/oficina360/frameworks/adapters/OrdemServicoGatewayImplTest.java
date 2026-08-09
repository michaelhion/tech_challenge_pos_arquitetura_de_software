package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoDTOMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.OrdemServicosRepository;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import com.techchallenger.oficina360.usecases.ordemservico.query.OrdemServicoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.DirecaoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.ResultadoPaginado;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoGatewayImplTest {

	private static final UUID ORDEM_SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID OUTRA_ORDEM_SERVICO_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final String PLACA = "ABC1D23";

	@Mock
	private OrdemServicosRepository repository;

	@Mock
	private OrdemServicoDTOMapper mapper;

	@Mock
	private OrdemServico ordemServico;

	@Mock
	private OrdemServico outraOrdemServico;

	@Mock
	private OrdemServicoEntity entity;

	@Mock
	private OrdemServicoEntity outraEntity;

	@Mock
	private OrdemServicoEntity entityPersistida;

	@InjectMocks
	private OrdemServicoGatewayImpl gateway;

	private static Stream<Arguments> cenariosDeOrdenacao() {
		return Stream.of(Arguments.of(OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC, "dtHoraAbertura",
						Sort.Direction.ASC),
				Arguments.of(OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.DESC, "dtHoraAbertura",
						Sort.Direction.DESC),
				Arguments.of(OrdemServicoOrdenacao.VALOR_TOTAL, DirecaoOrdenacao.ASC, "valorOs", Sort.Direction.ASC),
				Arguments.of(OrdemServicoOrdenacao.STATUS, DirecaoOrdenacao.DESC, "ordemDeServicoStatus",
						Sort.Direction.DESC),
				Arguments.of(OrdemServicoOrdenacao.PLACA, DirecaoOrdenacao.ASC, "placaVeiculo", Sort.Direction.ASC));
	}

	@Test
	void deveBuscarPrimeiraOrdemAtivaPorPlacaEStatus() {
		Collection<OrdemDeServicoStatus> status = List.of(OrdemDeServicoStatus.RECEBIDA,
				OrdemDeServicoStatus.EM_DIAGNOSTICO);

		when(repository.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(PLACA, status)).thenReturn(
				Optional.of(entity));

		when(mapper.toDomain(entity)).thenReturn(ordemServico);

		Optional<OrdemServico> resultado = gateway.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(PLACA, status);

		assertTrue(resultado.isPresent());
		assertSame(ordemServico, resultado.orElseThrow());

		verify(repository).findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(PLACA, status);

		verify(mapper).toDomain(entity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarVazioQuandoNaoExistirOrdemAtivaParaPlaca() {
		Collection<OrdemDeServicoStatus> status = List.of(OrdemDeServicoStatus.RECEBIDA);

		when(repository.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(PLACA, status)).thenReturn(Optional.empty());

		Optional<OrdemServico> resultado = gateway.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(PLACA, status);

		assertTrue(resultado.isEmpty());

		verify(repository).findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(PLACA, status);

		verify(mapper, never()).toDomain(any(OrdemServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveSalvarOrdemServicoComSucesso() {
		when(mapper.toEntity(ordemServico)).thenReturn(entity);

		when(repository.save(entity)).thenReturn(entityPersistida);

		when(mapper.toDomain(entityPersistida)).thenReturn(ordemServico);

		OrdemServico resultado = gateway.save(ordemServico);

		assertSame(ordemServico, resultado);

		verify(mapper).toEntity(ordemServico);
		verify(repository).save(entity);
		verify(mapper).toDomain(entityPersistida);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveListarTodasAsOrdensServico() {
		when(repository.findAll()).thenReturn(List.of(entity, outraEntity));

		when(mapper.toDomain(entity)).thenReturn(ordemServico);

		when(mapper.toDomain(outraEntity)).thenReturn(outraOrdemServico);

		List<OrdemServico> resultado = gateway.findAll();

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(ordemServico, resultado.get(0)),
				() -> assertSame(outraOrdemServico, resultado.get(1)));

		verify(repository).findAll();
		verify(mapper).toDomain(entity);
		verify(mapper).toDomain(outraEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremOrdensServico() {
		when(repository.findAll()).thenReturn(List.of());

		List<OrdemServico> resultado = gateway.findAll();

		assertTrue(resultado.isEmpty());

		verify(repository).findAll();

		verify(mapper, never()).toDomain(any(OrdemServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	@SuppressWarnings("unchecked")
	void deveFiltrarOrdensServicoComPaginacao() {
		ListarOrdensServicoQuery query = criarQuery(1, 5, OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);

		Pageable pageableRetornado = PageRequest.of(1, 5);

		Page<OrdemServicoEntity> paginaEntidades = new PageImpl<>(List.of(entity, outraEntity), pageableRetornado, 12);

		when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(paginaEntidades);

		when(mapper.toDomain(entity)).thenReturn(ordemServico);

		when(mapper.toDomain(outraEntity)).thenReturn(outraOrdemServico);

		ResultadoPaginado<OrdemServico> resultado = gateway.filtrar(query);

		assertAll(() -> assertEquals(2, resultado.conteudo().size()),
				() -> assertSame(ordemServico, resultado.conteudo().get(0)),
				() -> assertSame(outraOrdemServico, resultado.conteudo().get(1)),
				() -> assertEquals(1, resultado.pagina()), () -> assertEquals(5, resultado.tamanho()),
				() -> assertEquals(12, resultado.totalElementos()), () -> assertEquals(3, resultado.totalPaginas()),
				() -> assertFalse(resultado.primeiraPagina()), () -> assertFalse(resultado.ultimaPagina()),
				() -> assertTrue(resultado.possuiProximaPagina()));

		verify(repository).findAll(any(Specification.class), any(Pageable.class));

		verify(mapper).toDomain(entity);
		verify(mapper).toDomain(outraEntity);
	}

	@ParameterizedTest
	@MethodSource("cenariosDeOrdenacao")
	@SuppressWarnings("unchecked")
	void deveCriarOrdenacaoCorreta(OrdemServicoOrdenacao ordenacao, DirecaoOrdenacao direcao,
			String propriedadeEsperada, Sort.Direction direcaoEsperada) {
		ListarOrdensServicoQuery query = criarQuery(0, 10, ordenacao, direcao);

		Page<OrdemServicoEntity> paginaVazia = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

		when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(paginaVazia);

		gateway.filtrar(query);

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

		verify(repository).findAll(any(Specification.class), pageableCaptor.capture());

		Pageable pageable = pageableCaptor.getValue();

		Sort.Order ordenacaoPrincipal = pageable.getSort().getOrderFor(propriedadeEsperada);

		Sort.Order desempatePorId = pageable.getSort().getOrderFor("id");

		assertAll(() -> assertEquals(0, pageable.getPageNumber()), () -> assertEquals(10, pageable.getPageSize()),
				() -> assertEquals(direcaoEsperada, ordenacaoPrincipal.getDirection()),
				() -> assertEquals(Sort.Direction.ASC, desempatePorId.getDirection()));
	}

	@Test
	@SuppressWarnings("unchecked")
	void deveUsarOrdenacaoAscendenteQuandoDirecaoNaoForDescendente() {
		ListarOrdensServicoQuery query = criarQuery(0, 10, OrdemServicoOrdenacao.PLACA, null);

		Page<OrdemServicoEntity> paginaVazia = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

		when(repository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(paginaVazia);

		gateway.filtrar(query);

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

		verify(repository).findAll(any(Specification.class), pageableCaptor.capture());

		Sort.Order ordem = pageableCaptor.getValue().getSort().getOrderFor("placaVeiculo");

		assertEquals(Sort.Direction.ASC, ordem.getDirection());
	}

	@Test
	void deveBuscarOrdemServicoPorIdComSucesso() {
		when(repository.findById(ORDEM_SERVICO_ID)).thenReturn(Optional.of(entity));

		when(mapper.toDomain(entity)).thenReturn(ordemServico);

		Optional<OrdemServico> resultado = gateway.findById(ORDEM_SERVICO_ID);

		assertTrue(resultado.isPresent());
		assertSame(ordemServico, resultado.orElseThrow());

		verify(repository).findById(ORDEM_SERVICO_ID);
		verify(mapper).toDomain(entity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarVazioQuandoOrdemServicoNaoExistirPorId() {
		when(repository.findById(OUTRA_ORDEM_SERVICO_ID)).thenReturn(Optional.empty());

		Optional<OrdemServico> resultado = gateway.findById(OUTRA_ORDEM_SERVICO_ID);

		assertTrue(resultado.isEmpty());

		verify(repository).findById(OUTRA_ORDEM_SERVICO_ID);

		verify(mapper, never()).toDomain(any(OrdemServicoEntity.class));

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveExcluirOrdemServicoPorId() {
		gateway.deleteById(ORDEM_SERVICO_ID);

		verify(repository).deleteById(ORDEM_SERVICO_ID);

		verifyNoMoreInteractions(repository, mapper);
	}

	private ListarOrdensServicoQuery criarQuery(int pagina, int tamanho, OrdemServicoOrdenacao ordenacao,
			DirecaoOrdenacao direcao) {
		return new ListarOrdensServicoQuery(null, null, null, null, null, null, null, pagina, tamanho, ordenacao,
				direcao);
	}
}