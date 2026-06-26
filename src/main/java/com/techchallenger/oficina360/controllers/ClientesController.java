package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.docs.api.ClientesApi;
import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.services.ClienteService;
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
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClientesController implements ClientesApi {

    private final ClienteService clienteService;

    @Override
    @GetMapping("/listar/{documento}")
    public ResponseEntity<ClienteDTO> buscarPorDocumento(
            @PathVariable String documento
    ) {
        return clienteService.findByDocumento(documento)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping("/salvar")
    public ResponseEntity<ClienteDTO> salvar(
            @Valid @RequestBody ClienteDTO cliente
    ) {
        ClienteDTO clienteSalvo = clienteService.save(cliente);
        return ResponseEntity.status(201).body(clienteSalvo);
    }

    @Override
    @PutMapping("/editar/{documento}")
    public ResponseEntity<ClienteDTO> editar(
            @PathVariable String documento,
            @Valid @RequestBody ClienteDTO cliente
    ) {
        ClienteDTO clienteAtualizado = clienteService.edit(documento, cliente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Override
    @DeleteMapping("/deletar/{documento}")
    public ResponseEntity<Void> deletar(
            @PathVariable String documento
    ) {
        clienteService.delete(documento);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/listar")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        return ResponseEntity.ok(clienteService.findAll());
    }
}