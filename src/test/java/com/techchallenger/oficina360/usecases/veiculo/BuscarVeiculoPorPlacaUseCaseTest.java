package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarVeiculoPorPlacaUseCaseTest {

	private static final String PLACA = "ABC1D23";

	private static final String MARCA = "Volkswagen";

	private static final String MODELO = "Gol";

	private static final String ANO = "2020";

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String MENSAGEM_VEICULO_NAO_ENCONTRADO = "Veiculo não encontrado";

	@Mock
	private VeiculoGateway veiculoGateway;

	@Mock
	private Veiculo veiculo;

	@InjectMocks
	private BuscarVeiculoPorPlacaUseCase useCase;

	@Test
	void deveBuscarVeiculoPorPlacaComSucesso() {
		configurarVeiculo();

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		VeiculoCommand resultado = useCase.findByPlaca(PLACA);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(PLACA, resultado.placa()), () -> assertEquals(MARCA, resultado.marca()),
				() -> assertEquals(MODELO, resultado.modelo()), () -> assertEquals(ANO, resultado.ano()),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.clienteDocumento()));

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveNormalizarPlacaAntesDeBuscarVeiculo() {
		String placaInformada = " abc1d23 ";

		configurarVeiculo();

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		VeiculoCommand resultado = useCase.findByPlaca(placaInformada);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(PLACA, resultado.placa()), () -> assertEquals(MARCA, resultado.marca()),
				() -> assertEquals(MODELO, resultado.modelo()), () -> assertEquals(ANO, resultado.ano()),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.clienteDocumento()));

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveLancarExcecaoQuandoVeiculoNaoForEncontrado() {
		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.findByPlaca(PLACA));

		assertEquals(MENSAGEM_VEICULO_NAO_ENCONTRADO, exception.getMessage());

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveNormalizarPlacaAntesDeVerificarSeVeiculoExiste() {
		String placaInformada = " abc1d23 ";

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.findByPlaca(placaInformada));

		assertEquals(MENSAGEM_VEICULO_NAO_ENCONTRADO, exception.getMessage());

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	private void configurarVeiculo() {
		when(veiculo.getPlaca()).thenReturn(PLACA);

		when(veiculo.getMarca()).thenReturn(MARCA);

		when(veiculo.getModelo()).thenReturn(MODELO);

		when(veiculo.getAno()).thenReturn(ANO);

		when(veiculo.getClienteDocumento()).thenReturn(DOCUMENTO_CLIENTE);
	}
}