package com.techchallenger.oficina360.usecases.validators;

import com.techchallenger.oficina360.constants.MensagensDeErroConstant;
import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.web.exceptions.RegraDeNegocioException;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.OS_ORDEM_DE_SERVICO_ATIVA_PARA_O_VEICULO;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class OrdemServicoValidator {

	private final OrdemServicoGateway ordemServicoGateway;


	public OrdemServicoValidator(OrdemServicoGateway ordemServicoGateway) {
		this.ordemServicoGateway = ordemServicoGateway;
	}

	public void validarVeiculoPertenceAoCliente(
			Veiculo veiculo,
			Cliente cliente) {

		if (!veiculo.getClienteDocumento().equals(cliente.getDocumento())) {
			throw new RegraDeNegocioException(
					MensagensDeErroConstant.OS_VEICULO_NAO_PERTENCE_AO_CLIENTE
			);
		}
	}

	public void validarNaoExisteOrdemServicoAtiva(String placaVeiculo) {

		ordemServicoGateway
				.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(
						normalizarPlaca(placaVeiculo),
						OrdemDeServicoStatus.ativos())
				.ifPresent(os -> {
					throw new RegraDeNegocioException(
							String.format(
									OS_ORDEM_DE_SERVICO_ATIVA_PARA_O_VEICULO,
									os.getPlacaVeiculo(),
									os.getId(),
									os.getOrdemDeServicoStatus()
							)
					);
				});
	}

}
