package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarClientesUseCaseTest {

	private static final UUID PRIMEIRO_CLIENTE_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID SEGUNDO_CLIENTE_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final String PRIMEIRO_DOCUMENTO = "12345678901";

	private static final String SEGUNDO_DOCUMENTO = "98765432100";

	@Mock
	private ClienteGateway clienteGateway;

	@InjectMocks
	private ListarClientesUseCase useCase;

	@Test
	void deveListarClientesComSucesso() {
		Cliente primeiroCliente = criarPrimeiroCliente();

		Cliente segundoCliente = criarSegundoCliente();

		when(clienteGateway.findAll()).thenReturn(List.of(primeiroCliente, segundoCliente));

		List<ClienteCommand> resultado = useCase.findAll();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		ClienteCommand primeiroResultado = resultado.get(0);

		ClienteCommand segundoResultado = resultado.get(1);

		assertAll(() -> assertEquals(PRIMEIRO_DOCUMENTO, primeiroResultado.documento()),
				() -> assertEquals("João da Silva", primeiroResultado.nome()),
				() -> assertEquals("joao.silva@oficina360.com", primeiroResultado.email()),
				() -> assertEquals("11999999999", primeiroResultado.telefone()));

		assertAll(() -> assertEquals(SEGUNDO_DOCUMENTO, segundoResultado.documento()),
				() -> assertEquals("Maria Oliveira", segundoResultado.nome()),
				() -> assertEquals("maria.oliveira@oficina360.com", segundoResultado.email()),
				() -> assertEquals("11888888888", segundoResultado.telefone()));

		verify(clienteGateway, times(1)).findAll();

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void devePreservarOrdemDosClientesRetornadosPeloGateway() {
		Cliente primeiroCliente = criarPrimeiroCliente();

		Cliente segundoCliente = criarSegundoCliente();

		when(clienteGateway.findAll()).thenReturn(List.of(segundoCliente, primeiroCliente));

		List<ClienteCommand> resultado = useCase.findAll();

		assertAll(() -> assertEquals(2, resultado.size()),
				() -> assertEquals(SEGUNDO_DOCUMENTO, resultado.get(0).documento()),
				() -> assertEquals(PRIMEIRO_DOCUMENTO, resultado.get(1).documento()));

		verify(clienteGateway).findAll();

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremClientes() {
		when(clienteGateway.findAll()).thenReturn(List.of());

		List<ClienteCommand> resultado = useCase.findAll();

		assertAll(() -> assertNotNull(resultado), () -> assertTrue(resultado.isEmpty()));

		verify(clienteGateway, times(1)).findAll();

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveRetornarListaNaoModificavel() {
		Cliente cliente = criarPrimeiroCliente();

		when(clienteGateway.findAll()).thenReturn(List.of(cliente));

		List<ClienteCommand> resultado = useCase.findAll();

		assertEquals(1, resultado.size());
		ClienteCommand clienteCommand = resultado.get(0);
		assertThrows(UnsupportedOperationException.class,
				() -> resultado.add(clienteCommand));

		verify(clienteGateway).findAll();

		verifyNoMoreInteractions(clienteGateway);
	}

	private Cliente criarPrimeiroCliente() {
		return new Cliente(PRIMEIRO_CLIENTE_ID, PRIMEIRO_DOCUMENTO, "João da Silva", "joao.silva@oficina360.com",
				"11999999999");
	}

	private Cliente criarSegundoCliente() {
		return new Cliente(SEGUNDO_CLIENTE_ID, SEGUNDO_DOCUMENTO, "Maria Oliveira", "maria.oliveira@oficina360.com",
				"11888888888");
	}
}