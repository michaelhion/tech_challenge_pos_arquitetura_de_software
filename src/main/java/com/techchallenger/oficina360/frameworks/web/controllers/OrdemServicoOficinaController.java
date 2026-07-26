package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.OrdemServicoOficinaApi;
import com.techchallenger.oficina360.dtos.consultarstatus.ConsultarStatusDTO;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.listagem.OrdemServicoFiltroDTO;
import com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoMapper;
import com.techchallenger.oficina360.usecases.ordemservico.AbrirOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.ConsultarStatusOsUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DeletarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DiagnosticarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.EditarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.FinalizarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.IniciarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.ListarOrdensServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.output.OrdemServicoResumoOutput;
import com.techchallenger.oficina360.usecases.ordemservico.query.ListarOrdensServicoQuery;
import com.techchallenger.oficina360.usecases.ordemservico.query.OrdemServicoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.DirecaoOrdenacao;
import com.techchallenger.oficina360.usecases.shared.paginacao.ResultadoPaginado;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/ordem-servico")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('MECANICO','ADMIN')")
public class OrdemServicoOficinaController implements OrdemServicoOficinaApi {

	private final AbrirOrdemServicoUseCase abrirOrdemServicoUseCase;
	private final DiagnosticarOrdemServicoUseCase diagnosticar;
	private final IniciarExecucaoUseCase iniciar;
	private final FinalizarExecucaoUseCase finalizar;
	private final ListarOrdensServicoUseCase listarOrdensServicoUseCase;
	private final BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;
	private final EditarOrdemServicoUseCase editarOrdemServicoUseCase;
	private final DeletarOrdemServicoUseCase deletarOrdemServicoUseCase;
	private final ConsultarStatusOsUseCase consultarStatusOsUseCase;

	@GetMapping("/listar")
	public ResponseEntity<Page<OrdemServicoDTO>> listarOrdensServico(@ModelAttribute OrdemServicoFiltroDTO filtro,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "DATA_ABERTURA") OrdemServicoOrdenacao ordenarPor,
			@RequestParam(defaultValue = "ASC") DirecaoOrdenacao direcao) {
		ListarOrdensServicoQuery query = new ListarOrdensServicoQuery(filtro.status(), filtro.documentoCliente(),
				filtro.placa(), filtro.aberturaInicial(), filtro.aberturaFinal(), filtro.valorMinimo(),
				filtro.valorMaximo(), page, size, ordenarPor, direcao);

		ResultadoPaginado<OrdemServicoResumoOutput> output = listarOrdensServicoUseCase.executar(query);

		return ResponseEntity.ok(toPage(output));
	}

	private Page<OrdemServicoDTO> toPage(ResultadoPaginado<OrdemServicoResumoOutput> output) {
		Pageable pageable = PageRequest.of(output.pagina(), output.tamanho());
		return new PageImpl<>(OrdemServicoMapper.outputListToDTOList(output.conteudo()), pageable,
				output.totalElementos());
	}

	@Override
	@GetMapping("/listar/{id}")
	public ResponseEntity<OrdemServicoDTO> buscarPorId(@PathVariable UUID id) {
		return buscarOrdemServicoPorIdUseCase.findById(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@Override
	@PostMapping("/salvar")
	public ResponseEntity<CriarOrdemServicoDTO> salvar(@Valid @RequestBody CriarOrdemServicoDTO criarOrdemServicoDTO) {
		CriarOrdemServicoDTO ordemServicoSalva = abrirOrdemServicoUseCase.abrirOrdemServico(criarOrdemServicoDTO);
		return ResponseEntity.status(201).body(ordemServicoSalva);
	}

	@Override
	@PutMapping("/editar/{id}")
	public ResponseEntity<OrdemServicoDTO> editar(@PathVariable UUID id,
			@Valid @RequestBody OrdemServicoDTO ordemServicoDTO) {
		OrdemServicoDTO ordemServicoAtualizada = editarOrdemServicoUseCase.edit(id, ordemServicoDTO);
		return ResponseEntity.ok(ordemServicoAtualizada);
	}

	@Override
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletar(@PathVariable UUID id) {
		deletarOrdemServicoUseCase.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PatchMapping("/{id}/diagnostico")
	public ResponseEntity<OrdemServicoDTO> diagnosticar(@PathVariable UUID id,
			@Valid @RequestBody DiagnosticoDTO diagnosticoDTO) {
		OrdemServicoDTO ordemServicoDiagnosticada = diagnosticar.diagnosticar(id, diagnosticoDTO);

		return ResponseEntity.ok(ordemServicoDiagnosticada);
	}

	@Override
	@PatchMapping("/execucao/iniciar/{id}")
	public ResponseEntity<OrdemServicoDTO> iniciarExecucao(@PathVariable UUID id) {
		iniciar.iniciarExecucao(id);

		return ResponseEntity.accepted().build();
	}

	@Override
	@PatchMapping("/execucao/finalizar/{id}")
	public ResponseEntity<OrdemServicoDTO> finalizarExecucao(@PathVariable UUID id) {
		finalizar.finalizarExecucao(id);

		return ResponseEntity.accepted().build();
	}

	@Override
	@GetMapping("/status/{id}")
	public ResponseEntity<ConsultarStatusDTO> consultarStatus(@PathVariable UUID id) {
		ConsultarStatusDTO dto = new ConsultarStatusDTO(consultarStatusOsUseCase.executar(id));
		return ResponseEntity.ok(dto);
	}
}