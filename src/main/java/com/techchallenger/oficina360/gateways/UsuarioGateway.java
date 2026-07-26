package com.techchallenger.oficina360.gateways;

import com.techchallenger.oficina360.dominio.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioGateway {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    Usuario save(Usuario usuario);
    List<Usuario> saveAll(List<Usuario> usuarios);

    List<Usuario> findAll();

    Optional<Usuario> findById(UUID id);


    void excluirTodos();
}
