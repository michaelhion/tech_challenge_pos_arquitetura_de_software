package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteFinderTest {

	private static final UUID CLIENTE_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final String DOCUMENTO = "12345678901";

	private static final String DOCUMENTO_FORMATADO = "123.456.789-01";

	private static final String NOME = "João da Silva";

	private static final String EMAIL = "joao.silva@email.com";

	private static final String TELEFONE = "11999999999";

	@Mock
	private ClienteGateway clienteGateway;

	@InjectMocks
	private ClienteFinder clienteFinder;

	@Test
	void deveBuscarClientePorDocumentoComSucesso() {
		Cliente cliente = criarCliente();

		when(clienteGateway.findByDocumento(DOCUMENTO)).thenReturn(Optional.of(cliente));

		Cliente resultado = clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO);

		assertSame(cliente, resultado);

		assertEquals(CLIENTE_ID, resultado.getId());

		assertEquals(DOCUMENTO, resultado.getDocumento());

		verify(clienteGateway).findByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveNormalizarDocumentoAntesDeBuscarCliente() {
		Cliente cliente = criarCliente();

		when(clienteGateway.findByDocumento(DOCUMENTO)).thenReturn(Optional.of(cliente));

		Cliente resultado = clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_FORMATADO);

		assertSame(cliente, resultado);

		verify(clienteGateway).findByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveRemoverEspacosDoDocumentoAntesDeBuscarCliente() {
		String documentoComEspacos = "  12345678901  ";

		Cliente cliente = criarCliente();

		when(clienteGateway.findByDocumento(DOCUMENTO)).thenReturn(Optional.of(cliente));

		Cliente resultado = clienteFinder.buscarPorDocumentoOuFalhar(documentoComEspacos);

		assertSame(cliente, resultado);

		verify(clienteGateway).findByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveLancarExcecaoQuandoClienteNaoForEncontrado() {
		when(clienteGateway.findByDocumento(DOCUMENTO)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO));

		assertEquals(CLIENTE_NAO_ENCONTRADO, exception.getMessage());

		verify(clienteGateway).findByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveNormalizarDocumentoAntesDeLancarExcecao() {
		when(clienteGateway.findByDocumento(DOCUMENTO)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_FORMATADO));

		assertEquals(CLIENTE_NAO_ENCONTRADO, exception.getMessage());

		verify(clienteGateway).findByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveBuscarComDocumentoNulo() {
		when(clienteGateway.findByDocumento(null)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> clienteFinder.buscarPorDocumentoOuFalhar(null));

		assertEquals(CLIENTE_NAO_ENCONTRADO, exception.getMessage());

		verify(clienteGateway).findByDocumento(null);

		verifyNoMoreInteractions(clienteGateway);
	}

	private Cliente criarCliente() {
		return new Cliente(CLIENTE_ID, DOCUMENTO, NOME, EMAIL, TELEFONE);
	}
}