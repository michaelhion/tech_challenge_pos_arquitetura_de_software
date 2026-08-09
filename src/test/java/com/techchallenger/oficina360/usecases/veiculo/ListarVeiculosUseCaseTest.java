package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarVeiculosUseCaseTest {

	private static final String PRIMEIRA_PLACA = "ABC1D23";

	private static final String SEGUNDA_PLACA = "DEF2G34";

	private static final String PRIMEIRO_DOCUMENTO = "12345678901";

	private static final String SEGUNDO_DOCUMENTO = "98765432100";
	private static final String ANO = "2022";

	@Mock
	private VeiculoGateway veiculoGateway;

	@Mock
	private Veiculo primeiroVeiculo;

	@Mock
	private Veiculo segundoVeiculo;

	@InjectMocks
	private ListarVeiculosUseCase useCase;

	@Test
	void deveListarVeiculosComSucesso() {
		configurarPrimeiroVeiculo();
		configurarSegundoVeiculo();

		when(veiculoGateway.findAll()).thenReturn(List.of(primeiroVeiculo, segundoVeiculo));

		List<VeiculoCommand> resultado = useCase.findAll();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		assertVeiculo(resultado.get(0), PRIMEIRA_PLACA, "Volkswagen", "Gol", "2022", PRIMEIRO_DOCUMENTO);

		assertVeiculo(resultado.get(1), SEGUNDA_PLACA, "Toyota", "Corolla",ANO, SEGUNDO_DOCUMENTO);

		verify(veiculoGateway).findAll();

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void devePreservarOrdemRetornadaPeloGateway() {
		configurarPrimeiroVeiculo();
		configurarSegundoVeiculo();

		when(veiculoGateway.findAll()).thenReturn(List.of(segundoVeiculo, primeiroVeiculo));

		List<VeiculoCommand> resultado = useCase.findAll();

		assertAll(() -> assertEquals(2, resultado.size()), () -> assertEquals(SEGUNDA_PLACA, resultado.get(0).placa()),
				() -> assertEquals(PRIMEIRA_PLACA, resultado.get(1).placa()));

		verify(veiculoGateway).findAll();

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremVeiculos() {
		when(veiculoGateway.findAll()).thenReturn(List.of());

		List<VeiculoCommand> resultado = useCase.findAll();

		assertAll(() -> assertNotNull(resultado), () -> assertTrue(resultado.isEmpty()));

		verify(veiculoGateway).findAll();

		verifyNoMoreInteractions(veiculoGateway);
	}

	private void configurarPrimeiroVeiculo() {
		when(primeiroVeiculo.getPlaca()).thenReturn(PRIMEIRA_PLACA);

		when(primeiroVeiculo.getMarca()).thenReturn("Volkswagen");

		when(primeiroVeiculo.getModelo()).thenReturn("Gol");

		when(primeiroVeiculo.getAno()).thenReturn(ANO);

		when(primeiroVeiculo.getClienteDocumento()).thenReturn(PRIMEIRO_DOCUMENTO);
	}

	private void configurarSegundoVeiculo() {
		when(segundoVeiculo.getPlaca()).thenReturn(SEGUNDA_PLACA);

		when(segundoVeiculo.getMarca()).thenReturn("Toyota");

		when(segundoVeiculo.getModelo()).thenReturn("Corolla");

		when(segundoVeiculo.getAno()).thenReturn(ANO);

		when(segundoVeiculo.getClienteDocumento()).thenReturn(SEGUNDO_DOCUMENTO);
	}

	private void assertVeiculo(VeiculoCommand resultado, String placaEsperada, String marcaEsperada,
			String modeloEsperado, String anoEsperado, String documentoEsperado) {
		assertAll(() -> assertEquals(placaEsperada, resultado.placa()),
				() -> assertEquals(marcaEsperada, resultado.marca()),
				() -> assertEquals(modeloEsperado, resultado.modelo()),
				() -> assertEquals(anoEsperado, resultado.ano()),
				() -> assertEquals(documentoEsperado, resultado.clienteDocumento()));
	}
}