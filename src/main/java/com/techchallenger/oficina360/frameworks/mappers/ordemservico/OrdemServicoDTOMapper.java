package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoRequestDTO;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoResponseDTO;
import com.techchallenger.oficina360.dtos.ordemservico.DadosFinanceirosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDetailDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.PecasInsumosAdicionadosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.detalhes.ServicosAdicionadosDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoEstoqueDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoEntity;
import com.techchallenger.oficina360.usecases.ordemservico.command.AprovacaoOrdemServicoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.DadosFinanceirosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.DiagnosticoCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.DiagnosticoEstoqueCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoDiagnosticoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoReqCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.OrdemServicoRespCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.PecasInsumosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ServicosAdicionadosCommand;
import com.techchallenger.oficina360.usecases.ordemservico.output.OrdemServicoResumoOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrdemServicoDTOMapper {

	private final OrdemServicoItemEstoqueDTOMapper ordemServicoItemEstoqueDTOMapper;

	private final OrdemServicoServicoMapper ordemServicoServicoMapper;

	public static OrdemServicoDTO domainToDTO(OrdemServico domain){
		return new OrdemServicoDTO(
				domain.getDocumentoCliente(),
				domain.getPlacaVeiculo(),
				domain.getDescricaoProblema(),
				domain.getOrdemDeServicoStatus()
		);
	}

	public static OrdemServicoReqCommand toCommand(OrdemServicoDTO dto){
		return new OrdemServicoReqCommand(
				dto.documentoCliente(),
				dto.placaVeiculo(),
				dto.descricaoProblema()
		);
	}

	public static OrdemServicoReqCommand criarOsToCommand(CriarOrdemServicoRequestDTO dto){
		return new OrdemServicoReqCommand(
				dto.documentoCliente(),
				dto.placaVeiculo(),
				dto.descricaoProblema()
		);
	}

	public static List<OrdemServicoDTO> outputListToDTOList(List<OrdemServicoResumoOutput> outputs) {
		if (outputs == null || outputs.isEmpty()) {
			return List.of();
		}

		return outputs.stream().map(OrdemServicoDTOMapper::outputToDTO).toList();
	}

	private static OrdemServicoDTO outputToDTO(OrdemServicoResumoOutput output) {
		if (output == null) {
			return null;
		}

		return new OrdemServicoDTO(
				output.documentoCliente(),
				output.placaVeiculo(),
				output.descricaoProblema(),
				output.status()
		);
	}

	private static PecasInsumosAdicionadosDTO toPecaInsumoDTO(OrdemServicoItemEstoque item) {
		if (item == null) {
			return null;
		}

		return new PecasInsumosAdicionadosDTO(item.getNome(), item.getValorUnitario(), item.getQuantidade(),
				item.getValorTotal());
	}



	private static ServicosAdicionadosDTO toServicoAdicionadoDTO(OrdemServicoServico servico) {
		if (servico == null) {
			return null;
		}

		return new ServicosAdicionadosDTO(servico.getDescricao(), servico.getValor());
	}

	public static AprovacaoOrdemServicoCommand aprovacaoDTOToCommand(AprovacaoOrdemServicoDTO aprovacaoDTO) {
		return new AprovacaoOrdemServicoCommand(
				aprovacaoDTO.aprovado(),
				aprovacaoDTO.observacao()
		);
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
				ordemServicoItemEstoqueDTOMapper.toDomainList(entity.getItensEstoque()),
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

		domain.getItensEstoque().stream().map(ordemServicoItemEstoqueDTOMapper::toEntity)
				.forEach(entity::adicionarItemEstoque);

		return entity;
	}


	public static OrdemServicoDTO commandToDTO(OrdemServicoRespCommand command) {
		return new OrdemServicoDTO(
				command.documentoCliente(),
				command.placaVeiculo(),
				command.descricaoProblema(),
				command.status()

		);
	}

	public static OrdemServicoDetailDTO commandDadosFinanceirosToDTO(OrdemServicoDiagnosticoRespCommand command) {
		return new OrdemServicoDetailDTO(
				command.documentoCliente(),
				command.placaVeiculo(),
				command.descricaoProblema(),
				command.status(),
				toDadosFinanceirosDTO(command.dadosFinanceirosCommand())

		);
	}

	public static CriarOrdemServicoResponseDTO criarOsRespCommandToDTO(OrdemServicoRespCommand command){
		return new CriarOrdemServicoResponseDTO(
				command.id(),
				command.documentoCliente(),
				command.placaVeiculo(),
				command.descricaoProblema(),
				command.status()
		);
	}

	private static List<ServicosAdicionadosDTO> toServicosAdicionadosDTO(List<OrdemServicoServico> servicos) {
		if (servicos == null || servicos.isEmpty()) {
			return List.of();
		}

		return servicos.stream().map(OrdemServicoDTOMapper::toServicoAdicionadoDTO).toList();
	}

	private static List<PecasInsumosAdicionadosDTO> toPecasInsumosAdicionadosDTO(
			List<OrdemServicoItemEstoque> itensEstoque) {
		if (itensEstoque == null || itensEstoque.isEmpty()) {
			return List.of();
		}

		return itensEstoque.stream().map(OrdemServicoDTOMapper::toPecaInsumoDTO).toList();
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

	private static DadosFinanceirosDTO toDadosFinanceirosDTO(DadosFinanceirosCommand command) {
		return new DadosFinanceirosDTO(
				servicosAdicionadosCommandTODTO(command.servicos()),
				pecasInsumosAdicionadosCommandToDTO(command.pecasInsumos()),
				command.valorServicos(),
				command.valorPecasInsumos(),
				command.valorTotal()
		);
	}

	private static List<ServicosAdicionadosDTO> servicosAdicionadosCommandTODTO(List<ServicosAdicionadosCommand> command){
		List<ServicosAdicionadosDTO> servicosAdicionadosDTOList = new ArrayList<>();
		for (ServicosAdicionadosCommand commands : command){
			servicosAdicionadosDTOList.add(new ServicosAdicionadosDTO(
					commands.nome(),
					commands.valor()
			));

		}
		return servicosAdicionadosDTOList;
	}

	private static List<PecasInsumosAdicionadosDTO> pecasInsumosAdicionadosCommandToDTO(List<PecasInsumosAdicionadosCommand> commands){
		List<PecasInsumosAdicionadosDTO> pecasInsumosAdicionadosDTOList = new ArrayList<>();
		for (PecasInsumosAdicionadosCommand command : commands){
			pecasInsumosAdicionadosDTOList.add(new PecasInsumosAdicionadosDTO(
					command.nome(),
					command.valorUnitario(),
					command.quantidade(),
					command.valorTotal()
			));
		}
		return pecasInsumosAdicionadosDTOList;
	}

	public static DiagnosticoEstoqueCommand itenEstoqueDTOToCommand(DiagnosticoEstoqueDTO dto){
		return new DiagnosticoEstoqueCommand(
				dto.codigo(),
				dto.quantidade()
		);
	}

	public static DiagnosticoCommand diagnosticoDTOTOCommand(DiagnosticoDTO dto){
		return new DiagnosticoCommand(
				dto.codigosServicos(),
				dto.itensEstoque().stream().map(OrdemServicoDTOMapper::itenEstoqueDTOToCommand).toList()
		);
	}

}