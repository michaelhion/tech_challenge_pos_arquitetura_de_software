package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.docs.api.OrdemServicoApi;
import com.techchallenger.oficina360.dtos.ordemservico.AprovacaoOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.services.OrdemServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ordens-servico")
@RequiredArgsConstructor
public class OrdemServicoController implements OrdemServicoApi {

    private final OrdemServicoService ordemServicoService;

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<OrdemServicoDTO>> listarOrdensServico() {
        List<OrdemServicoDTO> ordensServico = ordemServicoService.findAll();
        return ResponseEntity.ok(ordensServico);
    }

    @Override
    @GetMapping("/listar/{id}")
    public ResponseEntity<OrdemServicoDTO> buscarPorId(
            @PathVariable UUID id
    ) {
        return ordemServicoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<OrdemServicoDTO> salvar(
            @Valid @RequestBody OrdemServicoDTO ordemServicoDTO
    ) {
        OrdemServicoDTO ordemServicoSalva = ordemServicoService.save(ordemServicoDTO);
        return ResponseEntity.status(201).body(ordemServicoSalva);
    }

    @Override
    @PutMapping("/editar/{id}")
    public ResponseEntity<OrdemServicoDTO> editar(
            @PathVariable UUID id,
            @Valid @RequestBody OrdemServicoDTO ordemServicoDTO
    ) {
        OrdemServicoDTO ordemServicoAtualizada = ordemServicoService.edit(id, ordemServicoDTO);
        return ResponseEntity.ok(ordemServicoAtualizada);
    }

    @Override
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id
    ) {
        ordemServicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{id}/aprovacao")
    public ResponseEntity<OrdemServicoDTO> aprovar(
            @PathVariable UUID id,
            @Valid @RequestBody AprovacaoOrdemServicoDTO aprovacaoDTO
    ) {
        OrdemServicoDTO ordemServicoAtualizada =
                ordemServicoService.aprovar(id, aprovacaoDTO);

        return ResponseEntity.ok(ordemServicoAtualizada);
    }

    @Override
    @PatchMapping("/{id}/diagnostico")
    public ResponseEntity<OrdemServicoDTO> diagnosticar(
            @PathVariable UUID id,
            @Valid @RequestBody DiagnosticoDTO diagnosticoDTO
    ) {
        OrdemServicoDTO ordemServicoDiagnosticada =
                ordemServicoService.diagnosticar(id, diagnosticoDTO);

        return ResponseEntity.ok(ordemServicoDiagnosticada);
    }

    @Override
    @PatchMapping("/execucao/iniciar/{id}")
    public ResponseEntity<OrdemServicoDTO> iniciarExecucao(
            @PathVariable UUID id
    ) {
        ordemServicoService.iniciarExecucao(id);

        return ResponseEntity.accepted().build();
    }

    @Override
    @PatchMapping("/execucao/finalizar/{id}")
    public ResponseEntity<OrdemServicoDTO> finalizarExecucao(
            @PathVariable UUID id
    ) {
        ordemServicoService.finalizarExecucao(id);

        return ResponseEntity.accepted().build();
    }
}