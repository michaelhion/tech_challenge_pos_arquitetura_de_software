package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarClienteUseCaseTest {

	private static final String DOCUMENTO_ATUAL = "12345678901";

	private static final String DOCUMENTO_ATUALIZADO = "98765432100";

	private static final String NOME_ATUAL = "Cliente original";

	private static final String NOME_ATUALIZADO = "Cliente atualizado";

	private static final String EMAIL_ATUAL = "cliente.original@oficina360.com";

	private static final String EMAIL_ATUALIZADO = "cliente.atualizado@oficina360.com";

	private static final String TELEFONE_ATUAL = "11911111111";

	private static final String TELEFONE_ATUALIZADO = "11999999999";

	@Mock
	private ClienteGateway clienteGateway;

	@InjectMocks
	private AtualizarClienteUseCase useCase;

	private Cliente cliente;
	private ClienteCommand command;

	@BeforeEach
	void setUp() {
		cliente = new Cliente(UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd"), DOCUMENTO_ATUAL, NOME_ATUAL,
				EMAIL_ATUAL, TELEFONE_ATUAL);

		command = new ClienteCommand(DOCUMENTO_ATUALIZADO, NOME_ATUALIZADO, EMAIL_ATUALIZADO, TELEFONE_ATUALIZADO);
	}

	@Test
	void deveAtualizarClienteComSucesso() {
		when(clienteGateway.findByDocumento(DOCUMENTO_ATUAL)).thenReturn(Optional.of(cliente));

		when(clienteGateway.save(cliente)).thenAnswer(invocation -> invocation.getArgument(0));

		ClienteCommand resultado = useCase.edit(DOCUMENTO_ATUAL, command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO_ATUALIZADO, resultado.documento()),
				() -> assertEquals(NOME_ATUALIZADO, resultado.nome()),
				() -> assertEquals(EMAIL_ATUALIZADO, resultado.email()),
				() -> assertEquals(TELEFONE_ATUALIZADO, resultado.telefone()));

		assertAll(() -> assertEquals(DOCUMENTO_ATUALIZADO, cliente.getDocumento()),
				() -> assertEquals(NOME_ATUALIZADO, cliente.getNome()),
				() -> assertEquals(EMAIL_ATUALIZADO, cliente.getEmail()),
				() -> assertEquals(TELEFONE_ATUALIZADO, cliente.getTelefone()));

		verify(clienteGateway).findByDocumento(DOCUMENTO_ATUAL);

		verify(clienteGateway).save(cliente);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveLancarExcecaoQuandoClienteNaoForEncontrado() {
		when(clienteGateway.findByDocumento(DOCUMENTO_ATUAL)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.edit(DOCUMENTO_ATUAL, command));

		assertEquals(CLIENTE_NAO_ENCONTRADO, exception.getMessage());

		verify(clienteGateway).findByDocumento(DOCUMENTO_ATUAL);

		verify(clienteGateway, never()).save(any(Cliente.class));

		verifyNoMoreInteractions(clienteGateway);
	}
}