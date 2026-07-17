package com.techchallenger.oficina360.usecases.validators;

import com.techchallenger.oficina360.services.validators.OrdemServicoEstoqueValidator;
import com.techchallenger.oficina360.services.validators.OrdemServicoServicoValidator;
import com.techchallenger.oficina360.usecases.loaders.DiagnosticoDados;

public class DiagnosticoValidator {

	private final OrdemServicoServicoValidator ordemServicoServicoValidator;
	private final OrdemServicoEstoqueValidator ordemServicoEstoqueValidator;

	public DiagnosticoValidator(OrdemServicoServicoValidator ordemServicoServicoValidator,
			OrdemServicoEstoqueValidator ordemServicoEstoqueValidator) {
		this.ordemServicoServicoValidator = ordemServicoServicoValidator;
		this.ordemServicoEstoqueValidator = ordemServicoEstoqueValidator;
	}

	public void validar(DiagnosticoDados diagnosticoDados){

		ordemServicoServicoValidator.validar(diagnosticoDados.codigosServicos(), diagnosticoDados.servicosPorCodigoBanco());
		ordemServicoEstoqueValidator.validar(diagnosticoDados.estoquesPorCodigoSolicitado(),diagnosticoDados.estoquesPorCodigoBanco());
	}


}
