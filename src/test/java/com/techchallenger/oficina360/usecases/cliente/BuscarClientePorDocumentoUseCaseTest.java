package com.techchallenger.oficina360.usecases.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.CLIENTE_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarClientePorDocumentoUseCaseTest {

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String NOME_CLIENTE = "João da Silva";

	private static final String EMAIL_CLIENTE = "joao.silva@oficina360.com";

	private static final String TELEFONE_CLIENTE = "11999999999";

	@Mock
	private ClienteFinder clienteFinder;

	@InjectMocks
	private BuscarClientePorDocumentoUseCase useCase;

	@Test
	void deveBuscarClientePorDocumentoComSucesso() {
		Cliente cliente = criarCliente();

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		ClienteCommand resultado = useCase.findByDocumento(DOCUMENTO_CLIENTE);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO_CLIENTE, resultado.documento()),
				() -> assertEquals(NOME_CLIENTE, resultado.nome()),
				() -> assertEquals(EMAIL_CLIENTE, resultado.email()),
				() -> assertEquals(TELEFONE_CLIENTE, resultado.telefone()));

		verify(clienteFinder, times(1)).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verifyNoMoreInteractions(clienteFinder);
	}

	@Test
	void devePropagarExcecaoQuandoClienteNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException(CLIENTE_NAO_ENCONTRADO);

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.findByDocumento(DOCUMENTO_CLIENTE));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals(CLIENTE_NAO_ENCONTRADO, excecaoObtida.getMessage()));

		verify(clienteFinder, times(1)).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verifyNoMoreInteractions(clienteFinder);
	}

	private Cliente criarCliente() {
		return new Cliente(UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd"), DOCUMENTO_CLIENTE, NOME_CLIENTE,
				EMAIL_CLIENTE, TELEFONE_CLIENTE);
	}
}