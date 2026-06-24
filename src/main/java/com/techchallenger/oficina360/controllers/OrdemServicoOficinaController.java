package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.docs.api.OrdemServicoOficinaApi;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.services.OrdemServicoOficinaService;
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
@RequestMapping("/ordem-servico")
@RequiredArgsConstructor
public class OrdemServicoOficinaController implements OrdemServicoOficinaApi {

    private final OrdemServicoOficinaService ordemServicoOficinaService;

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<OrdemServicoDTO>> listarOrdensServico() {
        List<OrdemServicoDTO> ordensServico = ordemServicoOficinaService.findAll();
        return ResponseEntity.ok(ordensServico);
    }

    @Override
    @GetMapping("/listar/{id}")
    public ResponseEntity<OrdemServicoDTO> buscarPorId(
            @PathVariable UUID id
    ) {
        return ordemServicoOficinaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<CriarOrdemServicoDTO> salvar(
            @Valid @RequestBody CriarOrdemServicoDTO criarOrdemServicoDTO
    ) {
        CriarOrdemServicoDTO ordemServicoSalva = ordemServicoOficinaService.save(criarOrdemServicoDTO);
        return ResponseEntity.status(201).body(ordemServicoSalva);
    }

    @Override
    @PutMapping("/editar/{id}")
    public ResponseEntity<OrdemServicoDTO> editar(
            @PathVariable UUID id,
            @Valid @RequestBody OrdemServicoDTO ordemServicoDTO
    ) {
        OrdemServicoDTO ordemServicoAtualizada = ordemServicoOficinaService.edit(id, ordemServicoDTO);
        return ResponseEntity.ok(ordemServicoAtualizada);
    }

    @Override
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id
    ) {
        ordemServicoOficinaService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @Override
    @PatchMapping("/{id}/diagnostico")
    public ResponseEntity<OrdemServicoDTO> diagnosticar(
            @PathVariable UUID id,
            @Valid @RequestBody DiagnosticoDTO diagnosticoDTO
    ) {
        OrdemServicoDTO ordemServicoDiagnosticada =
                ordemServicoOficinaService.diagnosticar(id, diagnosticoDTO);

        return ResponseEntity.ok(ordemServicoDiagnosticada);
    }

    @Override
    @PatchMapping("/execucao/iniciar/{id}")
    public ResponseEntity<OrdemServicoDTO> iniciarExecucao(
            @PathVariable UUID id
    ) {
        ordemServicoOficinaService.iniciarExecucao(id);

        return ResponseEntity.accepted().build();
    }

    @Override
    @PatchMapping("/execucao/finalizar/{id}")
    public ResponseEntity<OrdemServicoDTO> finalizarExecucao(
            @PathVariable UUID id
    ) {
        ordemServicoOficinaService.finalizarExecucao(id);

        return ResponseEntity.accepted().build();
    }
}