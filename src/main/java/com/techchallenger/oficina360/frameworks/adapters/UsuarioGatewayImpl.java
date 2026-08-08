package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.UsuarioRepository;
import com.techchallenger.oficina360.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UsuarioGatewayImpl implements UsuarioGateway {

	private final UsuarioDTOMapper mapper;
	private final UsuarioRepository repository;
	private final UsuarioDTOMapper usuarioDTOMapper;

	@Override
	public Optional<Usuario> findByEmail(String email) {
		return repository
				.findByEmail(email)
				.map(mapper::toDomain);
	}

	@Override
	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	@Override
	public Usuario save(Usuario usuario) {
		UsuarioEntity entity = mapper.toEntity(usuario);
		UsuarioEntity persisted = repository.save(entity);
		return mapper.toDomain(persisted);
	}

	@Override
	public List<Usuario> saveAll(List<Usuario> usuarios) {
		List<UsuarioEntity> usuarioEntityList = usuarios.stream().map(UsuarioDTOMapper::toEntity).toList();
		List<UsuarioEntity> usuarioEntitiesSaved = repository.saveAll(usuarioEntityList);
		return usuarioEntitiesSaved.stream().map(usuarioDTOMapper::toDomain).toList();
	}

	@Override
	public List<Usuario> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::toDomain)
				.toList();
	}

	@Override
	public Optional<Usuario> findById(UUID id) {
		return repository.findById(id)
				.map(mapper::toDomain);
	}


	@Override
	public void excluirTodos() {
		repository.deleteAll();
	}
}
