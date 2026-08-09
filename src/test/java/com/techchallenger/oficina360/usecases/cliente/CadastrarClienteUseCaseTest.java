package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import com.techchallenger.oficina360.usecases.shared.exception.ClienteJaCadastradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_JA_CADASTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarClienteUseCaseTest {

	private static final String DOCUMENTO = "12345678901";

	private static final String DOCUMENTO_FORMATADO = "123.456.789-01";

	private static final String NOME = "João da Silva";

	private static final String EMAIL = "joao.silva@oficina360.com";

	private static final String TELEFONE = "11999999999";

	private static final UUID CLIENTE_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	@Mock
	private ClienteGateway clienteGateway;

	@InjectMocks
	private CadastrarClienteUseCase useCase;

	private ClienteCommand command;

	@BeforeEach
	void setUp() {
		command = new ClienteCommand(DOCUMENTO, NOME, EMAIL, TELEFONE);
	}

	@Test
	void deveCadastrarClienteComSucesso() {
		Cliente clienteSalvo = new Cliente(CLIENTE_ID, DOCUMENTO, NOME, EMAIL, TELEFONE);

		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(false);

		when(clienteGateway.save(any(Cliente.class))).thenReturn(clienteSalvo);

		ClienteCommand resultado = useCase.save(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO, resultado.documento()), () -> assertEquals(NOME, resultado.nome()),
				() -> assertEquals(EMAIL, resultado.email()), () -> assertEquals(TELEFONE, resultado.telefone()));

		verify(clienteGateway, times(1)).existsByDocumento(DOCUMENTO);

		verify(clienteGateway, times(1)).save(any(Cliente.class));

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveEnviarClienteCorretoParaPersistencia() {
		Cliente clienteSalvo = new Cliente(CLIENTE_ID, DOCUMENTO, NOME, EMAIL, TELEFONE);

		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(false);

		when(clienteGateway.save(any(Cliente.class))).thenReturn(clienteSalvo);

		useCase.save(command);

		ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);

		verify(clienteGateway).save(clienteCaptor.capture());

		Cliente clienteEnviado = clienteCaptor.getValue();

		assertNotNull(clienteEnviado);

		assertAll(() -> assertEquals(DOCUMENTO, clienteEnviado.getDocumento()),
				() -> assertEquals(NOME, clienteEnviado.getNome()),
				() -> assertEquals(EMAIL, clienteEnviado.getEmail()),
				() -> assertEquals(TELEFONE, clienteEnviado.getTelefone()));
	}

	@Test
	void deveNormalizarDocumentoAntesDeVerificarDuplicidade() {
		ClienteCommand commandComDocumentoFormatado = new ClienteCommand(DOCUMENTO_FORMATADO, NOME, EMAIL, TELEFONE);

		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		ClienteJaCadastradoException exception = assertThrows(ClienteJaCadastradoException.class,
				() -> useCase.save(commandComDocumentoFormatado));

		assertEquals(CLIENTE_JA_CADASTRADO, exception.getMessage());

		verify(clienteGateway, times(1)).existsByDocumento(DOCUMENTO);

		verify(clienteGateway, never()).save(any(Cliente.class));

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveLancarExcecaoQuandoClienteJaEstiverCadastrado() {
		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		ClienteJaCadastradoException exception = assertThrows(ClienteJaCadastradoException.class,
				() -> useCase.save(command));

		assertEquals(CLIENTE_JA_CADASTRADO, exception.getMessage());

		verify(clienteGateway, times(1)).existsByDocumento(DOCUMENTO);

		verify(clienteGateway, never()).save(any(Cliente.class));

		verifyNoMoreInteractions(clienteGateway);
	}
}