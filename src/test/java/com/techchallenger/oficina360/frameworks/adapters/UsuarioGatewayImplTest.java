package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper.toDomain;
import static com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper.toEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioGatewayImplTest {

	@Mock
	private UsuarioRepository repository;

	@InjectMocks
	private UsuarioGatewayImpl gateway;

	private Usuario usuario;
	private UsuarioEntity usuarioEntity;
	private UUID id;

	@BeforeEach
	void setup() {
		id = UUID.randomUUID();

		usuario = new Usuario("teste@email.com","TESTE","CLIENTE","123456789");

		usuarioEntity = new UsuarioEntity();
		usuarioEntity.setId(id);
		usuarioEntity.setEmail("teste@email.com");
	}

	@Test
	void deveBuscarUsuarioPorEmail() {
		try (MockedStatic<com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper> mapperMock = mockStatic(
				com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper.class)) {

			when(repository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuarioEntity));

			mapperMock.when(() -> toDomain(usuarioEntity)).thenReturn(usuario);

			Optional<Usuario> resultado = gateway.findByEmail("teste@email.com");

			assertTrue(resultado.isPresent());
			assertEquals(usuario, resultado.get());

			verify(repository).findByEmail("teste@email.com");
		}
	}

	@Test
	void deveRetornarVazioQuandoEmailNaoEncontrado() {
		when(repository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

		Optional<Usuario> resultado = gateway.findByEmail("naoexiste@email.com");

		assertTrue(resultado.isEmpty());

		verify(repository).findByEmail("naoexiste@email.com");
	}

	@Test
	void deveVerificarSeEmailExiste() {
		when(repository.existsByEmail("teste@email.com")).thenReturn(true);

		boolean resultado = gateway.existsByEmail("teste@email.com");

		assertTrue(resultado);
		verify(repository).existsByEmail("teste@email.com");
	}

	@Test
	void deveSalvarUsuario() {
		try (MockedStatic<com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper> mapperMock = mockStatic(
				com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper.class)) {

			mapperMock.when(() -> toEntity(usuario)).thenReturn(usuarioEntity);

			when(repository.save(usuarioEntity)).thenReturn(usuarioEntity);

			mapperMock.when(() -> toDomain(usuarioEntity)).thenReturn(usuario);

			Usuario resultado = gateway.save(usuario);

			assertNotNull(resultado);
			assertEquals(usuario, resultado);

			verify(repository).save(usuarioEntity);
		}
	}

	@Test
	void deveSalvarListaDeUsuarios() {
		try (MockedStatic<com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper> mapperMock = mockStatic(
				com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper.class)) {

			List<Usuario> usuarios = List.of(usuario);
			List<UsuarioEntity> entities = List.of(usuarioEntity);

			mapperMock.when(() -> toEntity(usuario)).thenReturn(usuarioEntity);

			when(repository.saveAll(entities)).thenReturn(entities);

			mapperMock.when(() -> toDomain(usuarioEntity)).thenReturn(usuario);

			List<Usuario> resultado = gateway.saveAll(usuarios);

			assertEquals(1, resultado.size());
			assertEquals(usuario, resultado.get(0));

			verify(repository).saveAll(entities);
		}
	}

	@Test
	void deveRetornarTodosUsuarios() {
		try (MockedStatic<com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper> mapperMock = mockStatic(
				com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper.class)) {

			when(repository.findAll()).thenReturn(List.of(usuarioEntity));

			mapperMock.when(() -> toDomain(usuarioEntity)).thenReturn(usuario);

			List<Usuario> resultado = gateway.findAll();

			assertEquals(1, resultado.size());
			assertEquals(usuario, resultado.get(0));

			verify(repository).findAll();
		}
	}

	@Test
	void deveBuscarUsuarioPorId() {
		try (MockedStatic<com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper> mapperMock = mockStatic(
				com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper.class)) {

			when(repository.findById(id)).thenReturn(Optional.of(usuarioEntity));

			mapperMock.when(() -> toDomain(usuarioEntity)).thenReturn(usuario);

			Optional<Usuario> resultado = gateway.findById(id);

			assertTrue(resultado.isPresent());
			assertEquals(usuario, resultado.get());

			verify(repository).findById(id);
		}
	}

	@Test
	void deveRetornarVazioQuandoIdNaoEncontrado() {
		UUID idInexistente = UUID.randomUUID();

		when(repository.findById(idInexistente)).thenReturn(Optional.empty());

		Optional<Usuario> resultado = gateway.findById(idInexistente);

		assertTrue(resultado.isEmpty());

		verify(repository).findById(idInexistente);
	}

	@Test
	void deveExcluirTodosUsuarios() {
		doNothing().when(repository).deleteAll();

		gateway.excluirTodos();

		verify(repository, times(1)).deleteAll();
	}
}