package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.gateways.ServicoGateway;

import static com.techchallenger.oficina360.mappers.ServicoMapper.domainToDTO;
import static com.techchallenger.oficina360.mappers.ServicoMapper.toDomain;

public class CadastrarServicoUseCase {

	private final ServicoGateway servicoGateway;

	public CadastrarServicoUseCase(ServicoGateway servicoGateway) {
		this.servicoGateway = servicoGateway;
	}

	public ServicoDTO save(ServicoDTO servicoDTO) {
		Servico servico = toDomain(servicoDTO);
		servico.inicializaTempoDeExecucao(0);
		Servico servicoSalvo = servicoGateway.save(servico);

		return domainToDTO(servicoSalvo);
	}
}
