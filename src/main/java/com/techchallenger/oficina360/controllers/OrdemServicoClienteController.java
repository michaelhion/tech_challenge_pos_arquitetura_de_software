package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.docs.api.OrdemServicoClienteApi;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.services.OrdemServicoClienteService;
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

@RestController
@RequestMapping("/ordem-servico/clientes")
@RequiredArgsConstructor
public class OrdemServicoClienteController implements OrdemServicoClienteApi {

    private final OrdemServicoClienteService ordemServicoClienteService;

    @PreAuthorize("hasRole('" + CLIENTE + "') and @autorizacaoCliente.podeAcessarOrdemServico(#id, authentication)")
    @GetMapping("/listar/{id}")
    @Override
    public ResponseEntity<OrdemServicoDTO> buscarPorId(
            @PathVariable UUID id
    ) {
        return ordemServicoClienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('" + CLIENTE + "') and @autorizacaoCliente.podeAcessarOrdemServico(#id, authentication)")
    @PatchMapping("/aprovacao/{id}")
    @Override
    public ResponseEntity<OrdemServicoDTO> aprovar(
            @PathVariable UUID id,
            @Valid @RequestBody AprovacaoOrdemServicoDTO aprovacaoDTO

    ) {
        OrdemServicoDTO ordemServicoAtualizada =
                ordemServicoClienteService.aprovar(id, aprovacaoDTO);

        return ResponseEntity.ok(ordemServicoAtualizada);
    }

}