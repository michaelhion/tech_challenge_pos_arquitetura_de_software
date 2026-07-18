package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.VeiculosApi;
import com.techchallenger.oficina360.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.usecases.veiculo.AtualizarVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.BuscarVeiculoPorPlacaUseCase;
import com.techchallenger.oficina360.usecases.veiculo.CadastrarVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.ExcluirVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.ListarVeiculosUseCase;
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
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculosController implements VeiculosApi {

    private final CadastrarVeiculoUseCase cadastrarVeiculoUseCase;
    private final AtualizarVeiculoUseCase atualizarVeiculoUseCase;
    private final BuscarVeiculoPorPlacaUseCase buscarVeiculoPorPlacaUseCase;
    private final ListarVeiculosUseCase listarVeiculosUseCase;
    private final ExcluirVeiculoUseCase excluirVeiculoUseCase;

    @Override
    @GetMapping("/listar/{placa}")
    public ResponseEntity<VeiculoDTO> buscarPorPlaca(
            @PathVariable String placa
    ) {
        return buscarVeiculoPorPlacaUseCase.findByPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<VeiculoDTO> salvar(
            @Valid @RequestBody VeiculoDTO veiculoDTO
    ) {
        VeiculoDTO veiculoSalvo = cadastrarVeiculoUseCase.save(veiculoDTO);
        return ResponseEntity.status(201).body(veiculoSalvo);
    }

    @Override
    @PutMapping("/editar/{placa}")
    public ResponseEntity<VeiculoDTO> editar(
            @PathVariable String placa,
            @Valid @RequestBody VeiculoDTO veiculoDTO
    ) {
        VeiculoDTO veiculoAtualizado = atualizarVeiculoUseCase.edit(placa, veiculoDTO);
        return ResponseEntity.ok(veiculoAtualizado);
    }

    @Override
    @DeleteMapping("/deletar/{placa}")
    public ResponseEntity<Void> deletar(
            @PathVariable String placa
    ) {
        excluirVeiculoUseCase.delete(placa);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<VeiculoDTO>> listarVeiculos() {
        List<VeiculoDTO> veiculos = listarVeiculosUseCase.findAll();
        return ResponseEntity.ok(veiculos);
    }
}