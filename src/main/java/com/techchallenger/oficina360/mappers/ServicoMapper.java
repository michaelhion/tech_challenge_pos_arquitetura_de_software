package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ServicoEntity;

public class ServicoMapper {

	private ServicoMapper() {
	}

	public static ServicoDTO toDTO(ServicoEntity servicoEntity) {
		return new ServicoDTO(
				servicoEntity.getCodigo(),
				servicoEntity.getDescricao(),
				servicoEntity.getValor(),
				servicoEntity.getTempoMedioExecucaoMinutos()
		);
	}

	public static ServicoDTO domainToDTO(Servico servico) {
		return new ServicoDTO(
				servico.getCodigo(),
				servico.getDescricao(),
				servico.getValor(),
				servico.getTempoMedioExecucaoMinutos()
		);
	}

	public static ServicoEntity toEntity(ServicoDTO servicoDTO) {
		return ServicoEntity.builder()
				.codigo(servicoDTO.codigo())
				.descricao(servicoDTO.descricao())
				.valor(servicoDTO.valor())
				.tempoMedioExecucaoMinutos(servicoDTO.tempoDeExecucaoMedio())
				.build();
	}

	public static Servico toDomain(ServicoDTO servicoDTO) {
		return new Servico(
				null,
				servicoDTO.descricao(),
				servicoDTO.valor(),
				servicoDTO.codigo(),
				servicoDTO.tempoDeExecucaoMedio()
		);
	}

	public static void updateEntityFromDto(ServicoDTO dto, ServicoEntity entity) {
		if (dto == null || entity == null) {
			return;
		}

		entity.setDescricao(dto.descricao());
		entity.setValor(dto.valor());
	}

	public static void updatedomainFromDto(ServicoDTO dto, Servico domain) {
		if (dto == null || domain == null) {
			return;
		}
		Servico newServico = new Servico(
				domain.getId(),
				domain.getDescricao(),
				domain.getValor(),
				domain.getCodigo()
		);
		domain = newServico;
	}

}
