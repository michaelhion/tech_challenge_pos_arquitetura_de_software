package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.frameworks.web.exceptions.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.gateways.ServicoGateway;
import com.techchallenger.oficina360.mappers.ServicoMapper;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.SERVICO_NAO_ENCONTRADO;
import static com.techchallenger.oficina360.mappers.ServicoMapper.domainToDTO;

public class AtualizarServicoUseCase {

	private final ServicoGateway servicoGateway;

	public AtualizarServicoUseCase(ServicoGateway servicoGateway) {
		this.servicoGateway = servicoGateway;
	}

	public ServicoDTO edit(String codigo, ServicoDTO servicoDTO) {
		Servico servico = servicoGateway.findByCodigo(codigo)
				.orElseThrow(() -> new RecursoNaoEncontradoException(SERVICO_NAO_ENCONTRADO));

		ServicoMapper.updatedomainFromDto(servicoDTO, servico);

		Servico servicoAtualizado = servicoGateway.save(servico);

		return domainToDTO(servicoAtualizado);
	}
}
