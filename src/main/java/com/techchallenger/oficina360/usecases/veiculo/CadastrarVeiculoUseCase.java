package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.shared.exception.VeiculosJaCadastradoException;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_VEICULO_CADASTRADO;
import static com.techchallenger.oficina360.mappers.VeiculoCommandMapper.commandToDomain;
import static com.techchallenger.oficina360.mappers.VeiculoCommandMapper.domainToCommand;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarPlaca;

public class CadastrarVeiculoUseCase {

	private final VeiculoGateway veiculoGateway;
	private final ClienteFinder clienteFinder;

	public CadastrarVeiculoUseCase(VeiculoGateway veiculoGateway, ClienteFinder clienteFinder) {
		this.veiculoGateway = veiculoGateway;
		this.clienteFinder = clienteFinder;
	}

	public VeiculoCommand save(VeiculoCommand command) {
		String placaNormalizada = normalizarPlaca(command.placa());
		String documentoClienteNormalizado = normalizarDocumento(command.clienteDocumento());

		clienteFinder.buscarPorDocumentoOuFalhar(documentoClienteNormalizado);
		validarPlacaDisponivelParaCadastro(placaNormalizada);

		Veiculo veiculo = commandToDomain(command);

		return domainToCommand(veiculoGateway.save(veiculo));
	}

	private void validarPlacaDisponivelParaCadastro(String placa) {
		if (veiculoGateway.existsByPlaca(placa)) {
			throw new VeiculosJaCadastradoException(VEICULO_SERV_VEICULO_CADASTRADO);
		}
	}

}
