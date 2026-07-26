package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dtos.ordemservico.DadosFinanceirosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.PecasInsumosAdicionadosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.ServicosAdicionadosDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.usecases.ordemservico.output.OrdemServicoResumoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrdemServicoMapper {

	private final OrdemServicoItemEstoqueMapper ordemServicoItemEstoqueMapper;

	private final OrdemServicoServicoMapper ordemServicoServicoMapper;

	public static OrdemServicoDTO toDTO(OrdemServico domain) {
		if (domain == null) {
			return null;
		}

		return new OrdemServicoDTO(
				domain.getId(),
				domain.getDocumentoCliente(),
				domain.getPlacaVeiculo(),
				domain.getDescricaoProblema(),
				domain.getOrdemDeServicoStatus(),
				toDadosFinanceirosDTO(domain)
		);
	}

	public static List<OrdemServicoDTO> outputListToDTOList(List<OrdemServicoResumoOutput> outputs) {
		if (outputs == null || outputs.isEmpty()) {
			return List.of();
		}

		return outputs.stream().map(OrdemServicoMapper::outputToDTO).toList();
	}

	private static OrdemServicoDTO outputToDTO(OrdemServicoResumoOutput output) {
		if (output == null) {
			return null;
		}

		return new OrdemServicoDTO(
				output.id(),
				output.documentoCliente(),
				output.placaVeiculo(),
				output.descricaoProblema(),
				output.status(),
				new DadosFinanceirosDTO(
						List.of(),
						List.of(),
						output.valorServicos(),
						output.valorPecasInsumos(),
						output.valorTotal())
		);
	}

	private static DadosFinanceirosDTO toDadosFinanceirosDTO(OrdemServico ordemServico) {
		if (ordemServico == null) {
			return null;
		}

		return new DadosFinanceirosDTO(
				toServicosAdicionadosDTO(ordemServico.getServicos()),
				toPecasInsumosAdicionadosDTO(ordemServico.getItensEstoque()),
				ordemServico.getValorServicos(),
				ordemServico.getValorPecasInsumos(),
				ordemServico.getValorOs()
		);
	}

	private static List<PecasInsumosAdicionadosDTO> toPecasInsumosAdicionadosDTO(
			List<OrdemServicoItemEstoque> itensEstoque) {
		if (itensEstoque == null || itensEstoque.isEmpty()) {
			return List.of();
		}

		return itensEstoque.stream().map(OrdemServicoMapper::toPecaInsumoDTO).toList();
	}

	private static PecasInsumosAdicionadosDTO toPecaInsumoDTO(OrdemServicoItemEstoque item) {
		if (item == null) {
			return null;
		}

		return new PecasInsumosAdicionadosDTO(item.getNome(), item.getValorUnitario(), item.getQuantidade(),
				item.getValorTotal());
	}

	private static List<ServicosAdicionadosDTO> toServicosAdicionadosDTO(List<OrdemServicoServico> servicos) {
		if (servicos == null || servicos.isEmpty()) {
			return List.of();
		}

		return servicos.stream().map(OrdemServicoMapper::toServicoAdicionadoDTO).toList();
	}

	private static ServicosAdicionadosDTO toServicoAdicionadoDTO(OrdemServicoServico servico) {
		if (servico == null) {
			return null;
		}

		return new ServicosAdicionadosDTO(servico.getDescricao(), servico.getValor());
	}

	public OrdemServico toDomain(OrdemServicoEntity entity) {
		if (entity == null) {
			return null;
		}

		return new OrdemServico(
				entity.getId(),
				entity.getDocumentoCliente(),
				entity.getPlacaVeiculo(),
				entity.getDtHoraAbertura(),
				entity.getDtHoraFechamento(),
				entity.getDescricaoProblema(),
				entity.getOrdemDeServicoStatus(),
				entity.getObservacaoCliente(),
				ordemServicoServicoMapper.toDomainList(entity.getServicos()),
				ordemServicoItemEstoqueMapper.toDomainList(entity.getItensEstoque()),
				entity.getDtHoraInicioExecucao(),
				entity.getDtHoraFimExecucao()
		);
	}

	public OrdemServicoEntity toEntity(OrdemServico domain) {
		if (domain == null) {
			return null;
		}

		OrdemServicoEntity entity = OrdemServicoEntity.builder()
				.id(domain.getId())
				.documentoCliente(domain.getDocumentoCliente())
				.placaVeiculo(domain.getPlacaVeiculo())
				.dtHoraAbertura(domain.getDtHoraAbertura())
				.dtHoraFechamento(domain.getDtHoraFechamento())
				.descricaoProblema(domain.getDescricaoProblema())
				.ordemDeServicoStatus(domain.getOrdemDeServicoStatus())
				.observacaoCliente(domain.getObservacaoCliente())
				.valorServicos(domain.getValorServicos())
				.valorPecasInsumos(domain.getValorPecasInsumos())
				.valorOs(domain.getValorOs())
				.dtHoraInicioExecucao(domain.getDtHoraInicioExecucao())
				.dtHoraFimExecucao(domain.getDtHoraFimExecucao())
				.servicos(new ArrayList<>())
				.itensEstoque(new ArrayList<>())
				.build();

		domain.getServicos().stream().map(ordemServicoServicoMapper::toEntity).forEach(entity::adicionarServico);

		domain.getItensEstoque().stream().map(ordemServicoItemEstoqueMapper::toEntity)
				.forEach(entity::adicionarItemEstoque);

		return entity;
	}
}