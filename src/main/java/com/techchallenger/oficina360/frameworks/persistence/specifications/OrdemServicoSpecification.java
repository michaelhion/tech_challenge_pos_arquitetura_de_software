package com.techchallenger.oficina360.frameworks.persistence.specifications;

import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public final class OrdemServicoSpecification {

	private OrdemServicoSpecification() {
	}

	public static Specification<OrdemServicoEntity> filtrar(ListarOrdensServicoQuery filtro) {
		return (root, query, cb) -> {
			var predicates = new ArrayList<Predicate>();

			if (filtro.status() != null) {
				predicates.add(cb.equal(root.get("ordemDeServicoStatus"), filtro.status()));
			}

			if (filtro.documentoCliente() != null && !filtro.documentoCliente().isBlank()) {
				predicates.add(cb.equal(root.get("documentoCliente"), filtro.documentoCliente()));
			}

			if (filtro.placa() != null && !filtro.placa().isBlank()) {
				predicates.add(cb.equal(cb.upper(root.get("placaVeiculo")), filtro.placa().trim().toUpperCase()));
			}

			if (filtro.aberturaInicial() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("dtHoraAbertura"), filtro.aberturaInicial()));
			}

			if (filtro.aberturaFinal() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("dtHoraAbertura"), filtro.aberturaFinal()));
			}

			if (filtro.valorMinimo() != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("valorOs"), filtro.valorMinimo()));
			}

			if (filtro.valorMaximo() != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("valorOs"), filtro.valorMaximo()));
			}

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}
