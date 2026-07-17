package com.techchallenger.oficina360.frameworks.web.controllers;

import com.techchallenger.oficina360.docs.api.ClientesApi;
import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.usecases.cliente.AtualizarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.BuscarClientePorDocumentoUseCase;
import com.techchallenger.oficina360.usecases.cliente.CadastrarClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ExcluirClienteUseCase;
import com.techchallenger.oficina360.usecases.cliente.ListarClientesUseCase;
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
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClientesController implements ClientesApi {

    private final BuscarClientePorDocumentoUseCase buscarClientePorDocumentoUseCase;
    private final CadastrarClienteUseCase cadastrarClienteUseCase;
    private final ExcluirClienteUseCase excluirClienteUseCase;
    private final ListarClientesUseCase listarClientesUseCase;
    private final AtualizarClienteUseCase atualizarClienteUseCase;

    @Override
    @GetMapping("/listar/{documento}")
    public ResponseEntity<ClienteDTO> buscarPorDocumento(
            @PathVariable String documento
    ) {
        return buscarClientePorDocumentoUseCase.findByDocumento(documento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<ClienteDTO> salvar(
            @Valid @RequestBody ClienteDTO cliente
    ) {
        ClienteDTO clienteSalvo = cadastrarClienteUseCase.save(cliente);
        return ResponseEntity.status(201).body(clienteSalvo);
    }

    @Override
    @PutMapping("/editar/{documento}")
    public ResponseEntity<ClienteDTO> editar(
            @PathVariable String documento,
            @Valid @RequestBody ClienteDTO cliente
    ) {
        ClienteDTO clienteAtualizado = atualizarClienteUseCase.edit(documento, cliente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Override
    @DeleteMapping("/deletar/{documento}")
    public ResponseEntity<Void> deletar(
            @PathVariable String documento
    ) {
        excluirClienteUseCase.delete(documento);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        return ResponseEntity.ok(listarClientesUseCase.findAll());
    }
}