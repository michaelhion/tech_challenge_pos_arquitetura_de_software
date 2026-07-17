package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.EstoqueApi;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.usecases.estoque.BuscarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.CriarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.EditarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ExcluirItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ListarItensEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ReservarEstoqueUseCase;
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

    private final CriarItemEstoqueUseCase criarItemEstoqueUseCase;
    private final EditarItemEstoqueUseCase editarItemEstoqueUseCase;
    private final BuscarItemEstoqueUseCase buscarItemEstoqueUseCase;
    private final ListarItensEstoqueUseCase listarItensEstoqueUseCase;
    private final ExcluirItemEstoqueUseCase excluirItemEstoqueUseCase;
    private final ReservarEstoqueUseCase reservarEstoqueUseCase;

    @Override
    @GetMapping("/listar/{codigo}")
    public ResponseEntity<EstoqueDTO> buscarPorId(
            @PathVariable String codigo
    ) {
        return buscarItemEstoqueUseCase.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<EstoqueDTO> salvar(
            @Valid @RequestBody EstoqueDTO estoqueDTO
    ) {
        EstoqueDTO estoqueSalvo = criarItemEstoqueUseCase.save(estoqueDTO);
        return ResponseEntity.status(201).body(estoqueSalvo);
    }

    @Override
    @PutMapping("/editar/{codigo}")
    public ResponseEntity<EstoqueDTO> editar(
            @PathVariable String codigo,
            @Valid @RequestBody EstoqueDTO estoqueDTO
    ) {
        EstoqueDTO estoqueAtualizado = editarItemEstoqueUseCase.edit(codigo, estoqueDTO);
        return ResponseEntity.ok(estoqueAtualizado);
    }

    @Override
    @DeleteMapping("/deletar/{codigo}")
    public ResponseEntity<Void> deletar(
            @PathVariable String codigo
    ) {
        excluirItemEstoqueUseCase.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<EstoqueDTO>> listarEstoques() {
        List<EstoqueDTO> estoques = listarItensEstoqueUseCase.findAll();
        return ResponseEntity.ok(estoques);
    }

    @Override
    @PatchMapping("/reservar/{codigo}")
    public ResponseEntity<EstoqueDTO> reservar(
            @PathVariable String codigo,
            @Valid @RequestBody ReservaEstoqueDTO reservaDTO
    ) {
        EstoqueDTO estoqueAtualizado = reservarEstoqueUseCase.reservar(codigo, reservaDTO);
        return ResponseEntity.ok(estoqueAtualizado);
    }
}