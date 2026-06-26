package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.docs.api.EstoqueApi;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.services.EstoqueService;
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

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController implements EstoqueApi {

    private final EstoqueService estoqueService;

    @Override
    @GetMapping("/listar/{codigo}")
    public ResponseEntity<EstoqueDTO> buscarPorId(
            @PathVariable String codigo
    ) {
        return estoqueService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<EstoqueDTO> salvar(
            @Valid @RequestBody EstoqueDTO estoqueDTO
    ) {
        EstoqueDTO estoqueSalvo = estoqueService.save(estoqueDTO);
        return ResponseEntity.status(201).body(estoqueSalvo);
    }

    @Override
    @PutMapping("/editar/{codigo}")
    public ResponseEntity<EstoqueDTO> editar(
            @PathVariable String codigo,
            @Valid @RequestBody EstoqueDTO estoqueDTO
    ) {
        EstoqueDTO estoqueAtualizado = estoqueService.edit(codigo, estoqueDTO);
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @Override
    @DeleteMapping("/deletar/{codigo}")
    public ResponseEntity<Void> deletar(
            @PathVariable String codigo
    ) {
        estoqueService.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<EstoqueDTO>> listarEstoques() {
        List<EstoqueDTO> estoques = estoqueService.findAll();
        return ResponseEntity.ok(estoques);
    }

    @Override
    @PatchMapping("/reservar/{codigo}")
    public ResponseEntity<EstoqueDTO> reservar(
            @PathVariable String codigo,
            @Valid @RequestBody ReservaEstoqueDTO reservaDTO
    ) {
        EstoqueDTO estoqueAtualizado = estoqueService.reservar(codigo, reservaDTO);
        return ResponseEntity.ok(estoqueAtualizado);
    }
}