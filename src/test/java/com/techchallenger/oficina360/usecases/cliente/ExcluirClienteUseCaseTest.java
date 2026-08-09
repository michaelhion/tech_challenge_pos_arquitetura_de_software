package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirClienteUseCaseTest {

	private static final String DOCUMENTO = "12345678901";

	private static final String DOCUMENTO_FORMATADO = "123.456.789-01";

	@Mock
	private ClienteGateway clienteGateway;

	@InjectMocks
	private ExcluirClienteUseCase useCase;

	@Test
	void deveExcluirClienteComSucesso() {
		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		assertDoesNotThrow(() -> useCase.delete(DOCUMENTO));

		verify(clienteGateway).existsByDocumento(DOCUMENTO);

		verify(clienteGateway).deleteByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveVerificarExistenciaAntesDeExcluirCliente() {
		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		useCase.delete(DOCUMENTO);

		InOrder ordemDasChamadas = inOrder(clienteGateway);

		ordemDasChamadas.verify(clienteGateway).existsByDocumento(DOCUMENTO);

		ordemDasChamadas.verify(clienteGateway).deleteByDocumento(DOCUMENTO);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void deveLancarExcecaoQuandoClienteNaoForEncontrado() {
		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(false);

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.delete(DOCUMENTO));

		assertEquals(CLIENTE_NAO_ENCONTRADO, exception.getMessage());

		verify(clienteGateway).existsByDocumento(DOCUMENTO);

		verify(clienteGateway, never()).deleteByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void deveNormalizarDocumentoAntesDeExcluirCliente() {
		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(true);

		useCase.delete(DOCUMENTO_FORMATADO);

		verify(clienteGateway).existsByDocumento(DOCUMENTO);

		verify(clienteGateway).deleteByDocumento(DOCUMENTO);

		verifyNoMoreInteractions(clienteGateway);
	}

	@Test
	void naoDeveExcluirQuandoDocumentoFormatadoNaoForEncontrado() {
		when(clienteGateway.existsByDocumento(DOCUMENTO)).thenReturn(false);

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.delete(DOCUMENTO_FORMATADO));

		assertEquals(CLIENTE_NAO_ENCONTRADO, exception.getMessage());

		verify(clienteGateway).existsByDocumento(DOCUMENTO);

		verify(clienteGateway, never()).deleteByDocumento(anyString());

		verifyNoMoreInteractions(clienteGateway);
	}
}