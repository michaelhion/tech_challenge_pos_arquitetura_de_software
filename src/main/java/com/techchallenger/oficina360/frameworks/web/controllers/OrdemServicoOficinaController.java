package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.OrdemServicoOficinaApi;
import com.techchallenger.oficina360.dtos.ordemservico.CriarOrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.OrdemServicoDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.DiagnosticoDTO;
import com.techchallenger.oficina360.usecases.ordemservico.AbrirOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.BuscarOrdemServicoPorIdUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DeletarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.DiagnosticarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.EditarOrdemServicoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.FinalizarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.IniciarExecucaoUseCase;
import com.techchallenger.oficina360.usecases.ordemservico.ListarOrdensServicoUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<OrdemServicoDTO>> listarOrdensServico() {
        List<OrdemServicoDTO> ordensServico = listarOrdensServicoUseCase.findAll();
        return ResponseEntity.ok(ordensServico);
    }

    @Override
    @GetMapping("/listar/{id}")
    public ResponseEntity<OrdemServicoDTO> buscarPorId(
            @PathVariable UUID id
    ) {
        return buscarOrdemServicoPorIdUseCase.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<CriarOrdemServicoDTO> salvar(
            @Valid @RequestBody CriarOrdemServicoDTO criarOrdemServicoDTO
    ) {
        CriarOrdemServicoDTO ordemServicoSalva = abrirOrdemServicoUseCase.abrirOrdemServico(criarOrdemServicoDTO);
        return ResponseEntity.status(201).body(ordemServicoSalva);
    }

    @Override
    @PutMapping("/editar/{id}")
    public ResponseEntity<OrdemServicoDTO> editar(
            @PathVariable UUID id,
            @Valid @RequestBody OrdemServicoDTO ordemServicoDTO
    ) {
        OrdemServicoDTO ordemServicoAtualizada = editarOrdemServicoUseCase.edit(id, ordemServicoDTO);
        return ResponseEntity.ok(ordemServicoAtualizada);
    }

    @Override
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable UUID id
    ) {
        deletarOrdemServicoUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @Override
    @PatchMapping("/{id}/diagnostico")
    public ResponseEntity<OrdemServicoDTO> diagnosticar(
            @PathVariable UUID id,
            @Valid @RequestBody DiagnosticoDTO diagnosticoDTO
    ) {
        OrdemServicoDTO ordemServicoDiagnosticada =
                diagnosticar.diagnosticar(id, diagnosticoDTO);

        return ResponseEntity.ok(ordemServicoDiagnosticada);
    }

    @Override
    @PatchMapping("/execucao/iniciar/{id}")
    public ResponseEntity<OrdemServicoDTO> iniciarExecucao(
            @PathVariable UUID id
    ) {
        iniciar.iniciarExecucao(id);

        return ResponseEntity.accepted().build();
    }

    @Override
    @PatchMapping("/execucao/finalizar/{id}")
    public ResponseEntity<OrdemServicoDTO> finalizarExecucao(
            @PathVariable UUID id
    ) {
        finalizar.finalizarExecucao(id);

        return ResponseEntity.accepted().build();
    }
}