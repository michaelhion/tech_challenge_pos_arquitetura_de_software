package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.usecases.shared.exception.VeiculosJaCadastradoException;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_VEICULO_CADASTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CadastrarVeiculoUseCaseTest {

	private static final String PLACA = "ABC1D23";

	private static final String MARCA = "Volkswagen";

	private static final String MODELO = "Gol";

	private static final String ANO = "2020";

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	@Mock
	private VeiculoGateway veiculoGateway;

	@Mock
	private ClienteFinder clienteFinder;

	@Mock
	private Cliente cliente;

	@Mock
	private Veiculo veiculoSalvo;

	@InjectMocks
	private CadastrarVeiculoUseCase useCase;

	private VeiculoCommand command;

	@BeforeEach
	void setUp() {
		command = new VeiculoCommand(PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);
	}

	@Test
	void deveCadastrarVeiculoComSucesso() {
		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		when(veiculoGateway.existsByPlaca(PLACA)).thenReturn(false);

		when(veiculoGateway.save(any(Veiculo.class))).thenReturn(veiculoSalvo);

		configurarVeiculoSalvo();

		VeiculoCommand resultado = useCase.save(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(PLACA, resultado.placa()), () -> assertEquals(MARCA, resultado.marca()),
				() -> assertEquals(MODELO, resultado.modelo()), () -> assertEquals(ANO, resultado.ano()),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.clienteDocumento()));

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway).existsByPlaca(PLACA);

		verify(veiculoGateway).save(any(Veiculo.class));
	}

	@Test
	void deveEnviarVeiculoCorretoParaPersistencia() {
		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		when(veiculoGateway.existsByPlaca(PLACA)).thenReturn(false);

		when(veiculoGateway.save(any(Veiculo.class))).thenReturn(veiculoSalvo);

		configurarVeiculoSalvo();

		useCase.save(command);

		ArgumentCaptor<Veiculo> captor = ArgumentCaptor.forClass(Veiculo.class);

		verify(veiculoGateway).save(captor.capture());

		Veiculo veiculoEnviado = captor.getValue();

		assertNotNull(veiculoEnviado);

		assertAll(() -> assertEquals(PLACA, veiculoEnviado.getPlaca()),
				() -> assertEquals(MARCA, veiculoEnviado.getMarca()),
				() -> assertEquals(MODELO, veiculoEnviado.getModelo()),
				() -> assertEquals(ANO, veiculoEnviado.getAno()),
				() -> assertEquals(DOCUMENTO_CLIENTE, veiculoEnviado.getClienteDocumento()));

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway).existsByPlaca(PLACA);

		verifyNoMoreInteractions(clienteFinder, veiculoGateway);
	}

	@Test
	void deveNormalizarPlacaEDocumentoAntesDasValidacoes() {
		VeiculoCommand commandFormatado = new VeiculoCommand(" abc1d23 ", MARCA, MODELO, ANO, "123.456.789-01");

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		when(veiculoGateway.existsByPlaca(PLACA)).thenReturn(true);

		VeiculosJaCadastradoException exception = assertThrows(VeiculosJaCadastradoException.class,
				() -> useCase.save(commandFormatado));

		assertEquals(VEICULO_SERV_VEICULO_CADASTRADO, exception.getMessage());

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway).existsByPlaca(PLACA);

		verify(veiculoGateway, never()).save(any(Veiculo.class));

		verifyNoMoreInteractions(clienteFinder, veiculoGateway);
	}

	@Test
	void deveLancarExcecaoQuandoPlacaJaEstiverCadastrada() {
		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		when(veiculoGateway.existsByPlaca(PLACA)).thenReturn(true);

		VeiculosJaCadastradoException exception = assertThrows(VeiculosJaCadastradoException.class,
				() -> useCase.save(command));

		assertEquals(VEICULO_SERV_VEICULO_CADASTRADO, exception.getMessage());

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway).existsByPlaca(PLACA);

		verify(veiculoGateway, never()).save(any(Veiculo.class));

		verifyNoMoreInteractions(clienteFinder, veiculoGateway);
	}

	@Test
	void devePropagarExcecaoQuandoClienteNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException("Cliente não encontrado");

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.save(command));

		assertSame(excecaoEsperada, excecaoObtida);

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway, never()).existsByPlaca(PLACA);

		verify(veiculoGateway, never()).save(any(Veiculo.class));

		verifyNoMoreInteractions(clienteFinder, veiculoGateway);
	}

	private void configurarVeiculoSalvo() {

		when(veiculoSalvo.getPlaca()).thenReturn(PLACA);

		when(veiculoSalvo.getMarca()).thenReturn(MARCA);

		when(veiculoSalvo.getModelo()).thenReturn(MODELO);

		when(veiculoSalvo.getAno()).thenReturn(ANO);

		when(veiculoSalvo.getClienteDocumento()).thenReturn(DOCUMENTO_CLIENTE);
	}
}