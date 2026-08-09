package com.techchallenger.oficina360.frameworks.persistence.specifications;

import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import com.techchallenger.oficina360.usecases.ordemservico.query.OrdemServicoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.DirecaoOrdenacao;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoSpecificationTest {

	@Mock
	private Root<OrdemServicoEntity> root;

	@Mock
	private CriteriaQuery<?> criteriaQuery;

	@Mock
	private CriteriaBuilder criteriaBuilder;

	@Mock
	private Path<OrdemDeServicoStatus> statusPath;

	@Mock
	private Path<String> documentoClientePath;

	@Mock
	private Path<String> placaVeiculoPath;

	@Mock
	private Expression<String> placaVeiculoUpperExpression;

	@Mock
	private Path<LocalDateTime> dataHoraAberturaPath;

	@Mock
	private Path<BigDecimal> valorOsPath;

	@Mock
	private Predicate statusPredicate;

	@Mock
	private Predicate documentoPredicate;

	@Mock
	private Predicate placaPredicate;

	@Mock
	private Predicate aberturaInicialPredicate;

	@Mock
	private Predicate aberturaFinalPredicate;

	@Mock
	private Predicate valorMinimoPredicate;

	@Mock
	private Predicate valorMaximoPredicate;

	@Mock
	private Predicate resultadoPredicate;

	@BeforeEach
	void setUp() {
		when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(resultadoPredicate);
	}

	@Test
	void deveCriarSpecificationSemFiltros() {
		ListarOrdensServicoQuery filtro = querySemFiltros();

		Specification<OrdemServicoEntity> specification = OrdemServicoSpecification.filtrar(filtro);

		Predicate resultado = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

		assertSame(resultadoPredicate, resultado);

		ArgumentCaptor<Predicate[]> predicatesCaptor = ArgumentCaptor.forClass(Predicate[].class);

		verify(criteriaBuilder).and(predicatesCaptor.capture());

		assertEquals(0, predicatesCaptor.getValue().length);

		verifyNoMoreInteractions(root);
	}

	@Test
	void deveAdicionarTodosOsFiltrosInformados() {
		LocalDateTime aberturaInicial = LocalDateTime.of(2026, 7, 1, 0, 0);

		LocalDateTime aberturaFinal = LocalDateTime.of(2026, 7, 31, 23, 59);

		BigDecimal valorMinimo = new BigDecimal("100.00");

		BigDecimal valorMaximo = new BigDecimal("1000.00");

		ListarOrdensServicoQuery filtro = new ListarOrdensServicoQuery(OrdemDeServicoStatus.RECEBIDA, "12345678901",
				" abc1d23 ", aberturaInicial, aberturaFinal, valorMinimo, valorMaximo, 0, 10,
				OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);

		configurarCaminhosDaEntidade();

		when(criteriaBuilder.equal(statusPath, OrdemDeServicoStatus.RECEBIDA)).thenReturn(statusPredicate);

		when(criteriaBuilder.equal(documentoClientePath, "12345678901")).thenReturn(documentoPredicate);

		when(criteriaBuilder.upper(placaVeiculoPath)).thenReturn(placaVeiculoUpperExpression);

		when(criteriaBuilder.equal(placaVeiculoUpperExpression, "ABC1D23")).thenReturn(placaPredicate);

		when(criteriaBuilder.greaterThanOrEqualTo(dataHoraAberturaPath, aberturaInicial)).thenReturn(
				aberturaInicialPredicate);

		when(criteriaBuilder.lessThanOrEqualTo(dataHoraAberturaPath, aberturaFinal)).thenReturn(aberturaFinalPredicate);

		when(criteriaBuilder.greaterThanOrEqualTo(valorOsPath, valorMinimo)).thenReturn(valorMinimoPredicate);

		when(criteriaBuilder.lessThanOrEqualTo(valorOsPath, valorMaximo)).thenReturn(valorMaximoPredicate);

		Specification<OrdemServicoEntity> specification = OrdemServicoSpecification.filtrar(filtro);

		Predicate resultado = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

		assertSame(resultadoPredicate, resultado);

		verify(root).get("ordemDeServicoStatus");

		verify(root).get("documentoCliente");

		verify(root).get("placaVeiculo");

		verify(root, times(2)).get("dtHoraAbertura");

		verify(root, times(2)).get("valorOs");

		verify(criteriaBuilder).equal(statusPath, OrdemDeServicoStatus.RECEBIDA);

		verify(criteriaBuilder).equal(documentoClientePath, "12345678901");

		verify(criteriaBuilder).upper(placaVeiculoPath);

		verify(criteriaBuilder).equal(placaVeiculoUpperExpression, "ABC1D23");

		verify(criteriaBuilder).greaterThanOrEqualTo(dataHoraAberturaPath, aberturaInicial);

		verify(criteriaBuilder).lessThanOrEqualTo(dataHoraAberturaPath, aberturaFinal);

		verify(criteriaBuilder).greaterThanOrEqualTo(valorOsPath, valorMinimo);

		verify(criteriaBuilder).lessThanOrEqualTo(valorOsPath, valorMaximo);

		verify(criteriaBuilder).equal(statusPath, OrdemDeServicoStatus.RECEBIDA);

		verify(criteriaBuilder).equal(documentoClientePath, "12345678901");

		verify(criteriaBuilder).upper(placaVeiculoPath);

		verify(criteriaBuilder).equal(placaVeiculoUpperExpression, "ABC1D23");

		verify(criteriaBuilder).greaterThanOrEqualTo(dataHoraAberturaPath, aberturaInicial);

		verify(criteriaBuilder).lessThanOrEqualTo(dataHoraAberturaPath, aberturaFinal);

		verify(criteriaBuilder).greaterThanOrEqualTo(valorOsPath, valorMinimo);

		verify(criteriaBuilder).lessThanOrEqualTo(valorOsPath, valorMaximo);

		ArgumentCaptor<Predicate[]> predicatesCaptor = ArgumentCaptor.forClass(Predicate[].class);

		verify(criteriaBuilder).and(predicatesCaptor.capture());

		Predicate[] predicates = predicatesCaptor.getValue();

		assertEquals(7, predicates.length);

		assertSame(statusPredicate, predicates[0]);

		assertSame(documentoPredicate, predicates[1]);

		assertSame(placaPredicate, predicates[2]);

		assertSame(aberturaInicialPredicate, predicates[3]);

		assertSame(aberturaFinalPredicate, predicates[4]);

		assertSame(valorMinimoPredicate, predicates[5]);

		assertSame(valorMaximoPredicate, predicates[6]);
	}

	@Test
	void deveIgnorarDocumentoEPlacaEmBranco() {
		ListarOrdensServicoQuery filtro = new ListarOrdensServicoQuery(null, "   ", "  ", null, null, null, null, 0, 10,
				OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);

		Specification<OrdemServicoEntity> specification = OrdemServicoSpecification.filtrar(filtro);

		Predicate resultado = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

		assertSame(resultadoPredicate, resultado);

		verify(root, never()).get("documentoCliente");

		verify(root, never()).get("placaVeiculo");

		ArgumentCaptor<Predicate[]> predicatesCaptor = ArgumentCaptor.forClass(Predicate[].class);

		verify(criteriaBuilder).and(predicatesCaptor.capture());

		assertEquals(0, predicatesCaptor.getValue().length);
	}

	@Test
	void deveNormalizarPlacaAntesDeFiltrar() {
		ListarOrdensServicoQuery filtro = new ListarOrdensServicoQuery(null, null, " abc1d23 ", null, null, null, null,
				0, 10, OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);

		when(root.<String>get("placaVeiculo")).thenReturn(placaVeiculoPath);

		when(criteriaBuilder.upper(placaVeiculoPath)).thenReturn(placaVeiculoUpperExpression);

		when(criteriaBuilder.equal(placaVeiculoUpperExpression, "ABC1D23")).thenReturn(placaPredicate);

		Specification<OrdemServicoEntity> specification = OrdemServicoSpecification.filtrar(filtro);

		specification.toPredicate(root, criteriaQuery, criteriaBuilder);

		verify(criteriaBuilder).equal(placaVeiculoUpperExpression, "ABC1D23");

		ArgumentCaptor<Predicate[]> predicatesCaptor = ArgumentCaptor.forClass(Predicate[].class);

		verify(criteriaBuilder).and(predicatesCaptor.capture());

		assertEquals(1, predicatesCaptor.getValue().length);

		assertSame(placaPredicate, predicatesCaptor.getValue()[0]);
	}

	private void configurarCaminhosDaEntidade() {
		when(root.<OrdemDeServicoStatus>get("ordemDeServicoStatus")).thenReturn(statusPath);

		when(root.<String>get("documentoCliente")).thenReturn(documentoClientePath);

		when(root.<String>get("placaVeiculo")).thenReturn(placaVeiculoPath);

		when(root.<LocalDateTime>get("dtHoraAbertura")).thenReturn(dataHoraAberturaPath);

		when(root.<BigDecimal>get("valorOs")).thenReturn(valorOsPath);
	}

	private ListarOrdensServicoQuery querySemFiltros() {
		return new ListarOrdensServicoQuery(null, null, null, null, null, null, null, 0, 10,
				OrdemServicoOrdenacao.DATA_ABERTURA, DirecaoOrdenacao.ASC);
	}
}