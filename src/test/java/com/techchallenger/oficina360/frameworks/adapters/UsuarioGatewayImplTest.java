package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Usuario;
import com.techchallenger.oficina360.frameworks.mappers.usuario.UsuarioDTOMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.UsuarioEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioGatewayImplTest {

	private static final UUID USUARIO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID OUTRO_USUARIO_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final String EMAIL = "cliente@oficina360.com";

	@Mock
	private UsuarioDTOMapper mapper;

	@Mock
	private UsuarioRepository repository;

	@Mock
	private Usuario usuario;

	@Mock
	private Usuario outroUsuario;

	@Mock
	private UsuarioEntity usuarioEntity;

	@Mock
	private UsuarioEntity outroUsuarioEntity;

	@Mock
	private UsuarioEntity usuarioEntityPersistido;

	private UsuarioGatewayImpl gateway;

	@BeforeEach
	void setUp() {
		gateway = new UsuarioGatewayImpl(mapper, repository, mapper);
	}

	@Test
	void deveBuscarUsuarioPorEmailComSucesso() {
		when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuarioEntity));

		when(mapper.toDomain(usuarioEntity)).thenReturn(usuario);

		Optional<Usuario> resultado = gateway.findByEmail(EMAIL);

		assertTrue(resultado.isPresent());

		assertSame(usuario, resultado.orElseThrow());

		verify(repository).findByEmail(EMAIL);

		verify(mapper).toDomain(usuarioEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarOptionalVazioQuandoEmailNaoExistir() {
		when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());

		Optional<Usuario> resultado = gateway.findByEmail(EMAIL);

		assertTrue(resultado.isEmpty());

		verify(repository).findByEmail(EMAIL);

		verify(mapper, never()).toDomain(usuarioEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarTrueQuandoEmailExistir() {
		when(repository.existsByEmail(EMAIL)).thenReturn(true);

		boolean resultado = gateway.existsByEmail(EMAIL);

		assertTrue(resultado);

		verify(repository).existsByEmail(EMAIL);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void deveRetornarFalseQuandoEmailNaoExistir() {
		when(repository.existsByEmail(EMAIL)).thenReturn(false);

		boolean resultado = gateway.existsByEmail(EMAIL);

		assertFalse(resultado);

		verify(repository).existsByEmail(EMAIL);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void deveRetornarListaVaziaAoSalvarListaVazia() {
		when(repository.saveAll(List.of())).thenReturn(List.of());

		List<Usuario> resultado = gateway.saveAll(List.of());

		assertTrue(resultado.isEmpty());

		verify(repository).saveAll(List.of());

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveListarTodosOsUsuarios() {
		when(repository.findAll()).thenReturn(List.of(usuarioEntity, outroUsuarioEntity));

		when(mapper.toDomain(usuarioEntity)).thenReturn(usuario);

		when(mapper.toDomain(outroUsuarioEntity)).thenReturn(outroUsuario);

		List<Usuario> resultado = gateway.findAll();

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(usuario, resultado.get(0)),
				() -> assertSame(outroUsuario, resultado.get(1)));

		verify(repository).findAll();

		verify(mapper).toDomain(usuarioEntity);

		verify(mapper).toDomain(outroUsuarioEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremUsuarios() {
		when(repository.findAll()).thenReturn(List.of());

		List<Usuario> resultado = gateway.findAll();

		assertTrue(resultado.isEmpty());

		verify(repository).findAll();

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarUsuarioPorIdComSucesso() {
		when(repository.findById(USUARIO_ID)).thenReturn(Optional.of(usuarioEntity));

		when(mapper.toDomain(usuarioEntity)).thenReturn(usuario);

		Optional<Usuario> resultado = gateway.findById(USUARIO_ID);

		assertTrue(resultado.isPresent());

		assertSame(usuario, resultado.orElseThrow());

		verify(repository).findById(USUARIO_ID);

		verify(mapper).toDomain(usuarioEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarOptionalVazioQuandoUsuarioNaoExistirPorId() {
		when(repository.findById(OUTRO_USUARIO_ID)).thenReturn(Optional.empty());

		Optional<Usuario> resultado = gateway.findById(OUTRO_USUARIO_ID);

		assertTrue(resultado.isEmpty());

		verify(repository).findById(OUTRO_USUARIO_ID);

		verify(mapper, never()).toDomain(usuarioEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveExcluirTodosOsUsuarios() {
		gateway.excluirTodos();

		verify(repository).deleteAll();

		verifyNoMoreInteractions(repository);
	}
}