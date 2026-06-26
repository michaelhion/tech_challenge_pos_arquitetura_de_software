package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.docs.api.ServicosApi;
import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.services.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicosController implements ServicosApi {

    private final ServicoService servicoService;

    @Override
    @GetMapping("/listar/{codigo}")
    public ResponseEntity<ServicoDTO> buscarPorId(
            @PathVariable String codigo
    ) {
        return servicoService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<ServicoDTO> salvar(
            @Valid @RequestBody ServicoDTO servico
    ) {
        ServicoDTO servicoSalvo = servicoService.save(servico);
        return ResponseEntity.status(201).body(servicoSalvo);
    }

    @Override
    @PutMapping("/editar/{codigo}")
    public ResponseEntity<ServicoDTO> editar(
            @PathVariable String codigo,
            @Valid @RequestBody ServicoDTO servico
    ) {
        ServicoDTO servicoAtualizado = servicoService.edit(codigo, servico);
        return ResponseEntity.ok(servicoAtualizado);
    }

    @Override
    @DeleteMapping("/deletar/{codigo}")
    public ResponseEntity<Void> deletar(
            @PathVariable String codigo
    ) {
        servicoService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        List<ServicoDTO> servicos = servicoService.findAll();
        return ResponseEntity.ok(servicos);
    }
}