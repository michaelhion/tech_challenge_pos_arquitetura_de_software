package com.techchallenger.oficina360.frameworks.persistence.specifications;

import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public final class OrdemServicoSpecification {

	private OrdemServicoSpecification() {
	}

	public static Specification<OrdemServicoEntity> filtrar(ListarOrdensServicoQuery filtro) {
		return (root, query, cb) -> {
			var predicates = new ArrayList<Predicate>();

			addStatus(filtro, root, cb, predicates);

			addDocumentoCliente(filtro, root, cb, predicates);

			addPlaca(filtro, root, cb, predicates);

			addDTAberturaInicio(filtro, root, cb, predicates);

			addDtAberturaFim(filtro, root, cb, predicates);

			addValorMin(filtro, root, cb, predicates);

			addValorMax(filtro, root, cb, predicates);

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}

	private static void addValorMax(ListarOrdensServicoQuery filtro, Root<OrdemServicoEntity> root, CriteriaBuilder cb,
			ArrayList<Predicate> predicates) {
		if (filtro.valorMaximo() != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("valorOs"), filtro.valorMaximo()));
		}
	}

	private static void addValorMin(ListarOrdensServicoQuery filtro, Root<OrdemServicoEntity> root, CriteriaBuilder cb,
			ArrayList<Predicate> predicates) {
		if (filtro.valorMinimo() != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("valorOs"), filtro.valorMinimo()));
		}
	}

	private static void addDtAberturaFim(ListarOrdensServicoQuery filtro, Root<OrdemServicoEntity> root, CriteriaBuilder cb,
			ArrayList<Predicate> predicates) {
		if (filtro.aberturaFinal() != null) {
			predicates.add(cb.lessThanOrEqualTo(root.get("dtHoraAbertura"), filtro.aberturaFinal()));
		}
	}

	private static void addDTAberturaInicio(ListarOrdensServicoQuery filtro, Root<OrdemServicoEntity> root, CriteriaBuilder cb,
			ArrayList<Predicate> predicates) {
		if (filtro.aberturaInicial() != null) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("dtHoraAbertura"), filtro.aberturaInicial()));
		}
	}

	private static void addPlaca(ListarOrdensServicoQuery filtro, Root<OrdemServicoEntity> root, CriteriaBuilder cb,
			ArrayList<Predicate> predicates) {
		if (filtro.placa() != null && !filtro.placa().isBlank()) {
			predicates.add(cb.equal(cb.upper(root.get("placaVeiculo")), filtro.placa().trim().toUpperCase()));
		}
	}

	private static void addDocumentoCliente(ListarOrdensServicoQuery filtro, Root<OrdemServicoEntity> root, CriteriaBuilder cb,
			ArrayList<Predicate> predicates) {
		if (filtro.documentoCliente() != null && !filtro.documentoCliente().isBlank()) {
			predicates.add(cb.equal(root.get("documentoCliente"), filtro.documentoCliente()));
		}
	}

	private static void addStatus(ListarOrdensServicoQuery filtro, Root<OrdemServicoEntity> root, CriteriaBuilder cb,
			ArrayList<Predicate> predicates) {
		if (filtro.status() != null) {
			predicates.add(cb.equal(root.get("ordemDeServicoStatus"), filtro.status()));
		}
	}
}
