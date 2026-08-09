package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.frameworks.mappers.cliente.ClienteDTOMapper;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import com.techchallenger.oficina360.frameworks.persistence.repositories.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteGatewayImplTest {

	private static final UUID CLIENTE_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID OUTRO_CLIENTE_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final String DOCUMENTO = "12345678901";

	@Mock
	private ClienteDTOMapper mapper;

	@Mock
	private ClienteRepository repository;

	@Mock
	private Cliente cliente;

	@Mock
	private Cliente outroCliente;

	@Mock
	private ClienteEntity clienteEntity;

	@Mock
	private ClienteEntity outroClienteEntity;

	@Mock
	private ClienteEntity clienteEntityPersistido;

	@InjectMocks
	private ClienteGatewayImpl gateway;

	@Test
	void deveBuscarClientePorDocumentoComSucesso() {
		when(repository.findByDocumento(DOCUMENTO)).thenReturn(Optional.of(clienteEntity));

		when(mapper.toDomain(clienteEntity)).thenReturn(cliente);

		Optional<Cliente> resultado = gateway.findByDocumento(DOCUMENTO);

		assertTrue(resultado.isPresent());

		assertSame(cliente, resultado.orElseThrow());

		verify(repository).findByDocumento(DOCUMENTO);

		verify(mapper).toDomain(clienteEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarOptionalVazioQuandoClienteNaoExistirPorDocumento() {
		when(repository.findByDocumento(DOCUMENTO)).thenReturn(Optional.empty());

		Optional<Cliente> resultado = gateway.findByDocumento(DOCUMENTO);

		assertTrue(resultado.isEmpty());

		verify(repository).findByDocumento(DOCUMENTO);

		verify(mapper, never()).toDomain(clienteEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveExcluirClientePorDocumento() {
		gateway.deleteByDocumento(DOCUMENTO);

		verify(repository).deleteByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void deveRetornarTrueQuandoClienteExistirPorDocumento() {
		when(repository.existsByDocumento(DOCUMENTO)).thenReturn(true);

		boolean resultado = gateway.existsByDocumento(DOCUMENTO);

		assertTrue(resultado);

		verify(repository).existsByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void deveRetornarFalseQuandoClienteNaoExistirPorDocumento() {
		when(repository.existsByDocumento(DOCUMENTO)).thenReturn(false);

		boolean resultado = gateway.existsByDocumento(DOCUMENTO);

		assertFalse(resultado);

		verify(repository).existsByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(repository);
	}

	@Test
	void deveSalvarClienteComSucesso() {
		when(mapper.toEntity(cliente)).thenReturn(clienteEntity);

		when(repository.save(clienteEntity)).thenReturn(clienteEntityPersistido);

		when(mapper.toDomain(clienteEntityPersistido)).thenReturn(cliente);

		Cliente resultado = gateway.save(cliente);

		assertSame(cliente, resultado);

		verify(mapper).toEntity(cliente);

		verify(repository).save(clienteEntity);

		verify(mapper).toDomain(clienteEntityPersistido);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveConverterPersistirEConverterClienteNestaOrdem() {
		when(mapper.toEntity(cliente)).thenReturn(clienteEntity);

		when(repository.save(clienteEntity)).thenReturn(clienteEntityPersistido);

		when(mapper.toDomain(clienteEntityPersistido)).thenReturn(cliente);

		gateway.save(cliente);

		InOrder ordemDasChamadas = inOrder(mapper, repository);

		ordemDasChamadas.verify(mapper).toEntity(cliente);

		ordemDasChamadas.verify(repository).save(clienteEntity);

		ordemDasChamadas.verify(mapper).toDomain(clienteEntityPersistido);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void deveListarTodosOsClientes() {
		when(repository.findAll()).thenReturn(List.of(clienteEntity, outroClienteEntity));

		when(mapper.toDomain(clienteEntity)).thenReturn(cliente);

		when(mapper.toDomain(outroClienteEntity)).thenReturn(outroCliente);

		List<Cliente> resultado = gateway.findAll();

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(cliente, resultado.get(0)),
				() -> assertSame(outroCliente, resultado.get(1)));

		verify(repository).findAll();

		verify(mapper).toDomain(clienteEntity);

		verify(mapper).toDomain(outroClienteEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void devePreservarOrdemDosClientesRetornadosPeloRepository() {
		when(repository.findAll()).thenReturn(List.of(outroClienteEntity, clienteEntity));

		when(mapper.toDomain(outroClienteEntity)).thenReturn(outroCliente);

		when(mapper.toDomain(clienteEntity)).thenReturn(cliente);

		List<Cliente> resultado = gateway.findAll();

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertSame(outroCliente, resultado.get(0)),
				() -> assertSame(cliente, resultado.get(1)));

		verify(repository).findAll();

		verify(mapper).toDomain(outroClienteEntity);

		verify(mapper).toDomain(clienteEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
		when(repository.findAll()).thenReturn(List.of());

		List<Cliente> resultado = gateway.findAll();

		assertAll(() -> assertTrue(resultado.isEmpty()), () -> assertEquals(0, resultado.size()));

		verify(repository).findAll();

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveBuscarClientePorIdComSucesso() {
		when(repository.findById(CLIENTE_ID)).thenReturn(Optional.of(clienteEntity));

		when(mapper.toDomain(clienteEntity)).thenReturn(cliente);

		Optional<Cliente> resultado = gateway.findById(CLIENTE_ID);

		assertTrue(resultado.isPresent());

		assertSame(cliente, resultado.orElseThrow());

		verify(repository).findById(CLIENTE_ID);

		verify(mapper).toDomain(clienteEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveRetornarOptionalVazioQuandoClienteNaoExistirPorId() {
		when(repository.findById(OUTRO_CLIENTE_ID)).thenReturn(Optional.empty());

		Optional<Cliente> resultado = gateway.findById(OUTRO_CLIENTE_ID);

		assertTrue(resultado.isEmpty());

		verify(repository).findById(OUTRO_CLIENTE_ID);

		verify(mapper, never()).toDomain(clienteEntity);

		verifyNoMoreInteractions(repository, mapper);
	}

	@Test
	void deveExcluirClientePorId() {
		gateway.deleteById(CLIENTE_ID);

		verify(repository).deleteById(CLIENTE_ID);

		verifyNoMoreInteractions(repository);
	}
}