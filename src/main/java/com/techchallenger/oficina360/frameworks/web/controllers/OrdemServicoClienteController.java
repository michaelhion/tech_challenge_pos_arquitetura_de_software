package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.OrdemServicoClienteApi;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.usecases.ordemservico.AprovarOrcamentoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.BuscarOrdemServicoPorIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.Roles.CLIENTE;
import static com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoDTOMapper.aprovacaoDTOToCommand;
import static com.techchallenger.oficina360.frameworks.mappers.ordemservico.OrdemServicoDTOMapper.commandToDTO;

@RestController
@RequestMapping("/ordem-servico/clientes")
@RequiredArgsConstructor
public class OrdemServicoClienteController implements OrdemServicoClienteApi {

	private final AprovarOrcamentoUseCase aprovar;
	private final BuscarOrdemServicoPorIdUseCase buscarOrdemServicoPorIdUseCase;

	@PreAuthorize("hasRole('" + CLIENTE + "') and @autorizacaoClienteUseCase.podeAcessarOrdemServico(#id)")
	@GetMapping("/listar/{id}")
	@Override
	public ResponseEntity<OrdemServicoDTO> buscarPorId(@PathVariable UUID id) {
		OrdemServicoDTO dto =  commandToDTO(buscarOrdemServicoPorIdUseCase.findById(id));
		return ResponseEntity.ok(dto);
	}

	@PreAuthorize("hasRole('" + CLIENTE + "') and @autorizacaoClienteUseCase.podeAcessarOrdemServico(#id)")
	@PatchMapping("/aprovacao/{id}")
	@Override
	public ResponseEntity<Void> aprovar(@PathVariable UUID id,@Valid @RequestBody AprovacaoOrdemServicoDTO aprovacaoDTO

	) {
		aprovar.aprovar(id, aprovacaoDTOToCommand(aprovacaoDTO));

		return ResponseEntity.accepted().build();
	}

}