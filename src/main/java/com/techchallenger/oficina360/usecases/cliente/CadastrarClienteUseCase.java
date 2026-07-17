package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.mappers.ClienteMapper;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_JA_CADASTRADO;
import static com.techchallenger.oficina360.mappers.ClienteMapper.domainToDTO;
import static com.techchallenger.oficina360.utils.FormataDadosUtils.normalizarDocumento;

public class CadastrarClienteUseCase {

	private final ClienteGateway clienteGateway;

	public CadastrarClienteUseCase(ClienteGateway clienteGateway) {
		this.clienteGateway = clienteGateway;
	}

	public ClienteDTO save(ClienteDTO clienteDTO) {
		if (clienteGateway.existsByDocumento(normalizarDocumento(clienteDTO.documento()))) {
			throw new ConflitoException(CLIENTE_JA_CADASTRADO);
		}
		return domainToDTO(clienteGateway.save(ClienteMapper.toDomain(clienteDTO)));
	}
}
