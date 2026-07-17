package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.ServicosApi;
import com.techchallenger.oficina360.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.usecases.cliente.AtualizarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.BuscarClientePorDocumentoUseCase;
import com.techchallenger.oficina360.usecases.cliente.CadastrarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ExcluirClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ListarClientesUseCase;
import com.techchallenger.oficina360.usecases.servicos.AtualizarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.BuscarServicoPorCodigoUseCase;
import com.techchallenger.oficina360.usecases.servicos.CadastrarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ExcluirServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ListarServicosUseCase;
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

    private final CadastrarServicoUseCase cadastrarServicoUseCase;
    private final BuscarServicoPorCodigoUseCase buscarServicoPorCodigoUseCase;
    private final ListarServicosUseCase listarServicosUseCase;
    private final AtualizarServicoUseCase atualizarServicoUseCase;
    private final ExcluirServicoUseCase excluirServicoUseCase;

    @Override
    @GetMapping("/listar/{codigo}")
    public ResponseEntity<ServicoDTO> buscarPorId(
            @PathVariable String codigo
    ) {
        return buscarServicoPorCodigoUseCase.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<ServicoDTO> salvar(
            @Valid @RequestBody ServicoDTO servico
    ) {
        ServicoDTO servicoSalvo = cadastrarServicoUseCase.save(servico);
        return ResponseEntity.status(201).body(servicoSalvo);
    }

    @Override
    @PutMapping("/editar/{codigo}")
    public ResponseEntity<ServicoDTO> editar(
            @PathVariable String codigo,
            @Valid @RequestBody ServicoDTO servico
    ) {
        ServicoDTO servicoAtualizado = atualizarServicoUseCase.edit(codigo, servico);
        return ResponseEntity.ok(servicoAtualizado);
    }

    @Override
    @DeleteMapping("/deletar/{codigo}")
    public ResponseEntity<Void> deletar(
            @PathVariable String codigo
    ) {
        excluirServicoUseCase.delete(codigo);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<ServicoDTO>> listarServicos() {
        List<ServicoDTO> servicos = listarServicosUseCase.findAll();
        return ResponseEntity.ok(servicos);
    }
}