package com.techchallenger.oficina360.usecases.finders;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoFinderTest {

	private static final String PLACA = "ABC1D23";

	private static final String PLACA_FORMATADA = " abc-1d23 ";

	@Mock
	private VeiculoGateway veiculoGateway;

	@Mock
	private Veiculo veiculo;

	@InjectMocks
	private VeiculoFinder veiculoFinder;

	@Test
	void deveBuscarVeiculoPorPlacaComSucesso() {
		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		Veiculo resultado = veiculoFinder.buscarPorPlacaOuFalhar(PLACA);

		assertSame(veiculo, resultado);

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveNormalizarPlacaAntesDeBuscarVeiculo() {
		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		Veiculo resultado = veiculoFinder.buscarPorPlacaOuFalhar(PLACA_FORMATADA);

		assertSame(veiculo, resultado);

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveRemoverEspacosDaPlacaAntesDeBuscarVeiculo() {
		String placaComEspacos = " AB C1 D23 ";

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		Veiculo resultado = veiculoFinder.buscarPorPlacaOuFalhar(placaComEspacos);

		assertSame(veiculo, resultado);

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveConverterPlacaParaMaiusculasAntesDaBusca() {
		String placaMinuscula = "abc1d23";

		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.of(veiculo));

		Veiculo resultado = veiculoFinder.buscarPorPlacaOuFalhar(placaMinuscula);

		assertSame(veiculo, resultado);

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveLancarExcecaoQuandoVeiculoNaoForEncontrado() {
		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> veiculoFinder.buscarPorPlacaOuFalhar(PLACA));

		assertEquals(VEICULO_NAO_ENCONTRADO, exception.getMessage());

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveNormalizarPlacaAntesDeLancarExcecao() {
		when(veiculoGateway.findByPlaca(PLACA)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> veiculoFinder.buscarPorPlacaOuFalhar(PLACA_FORMATADA));

		assertEquals(VEICULO_NAO_ENCONTRADO, exception.getMessage());

		verify(veiculoGateway).findByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoGateway);
	}

	@Test
	void deveBuscarComPlacaNulaELancarExcecaoQuandoNaoEncontrada() {
		when(veiculoGateway.findByPlaca(null)).thenReturn(Optional.empty());

		RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class,
				() -> veiculoFinder.buscarPorPlacaOuFalhar(null));

		assertEquals(VEICULO_NAO_ENCONTRADO, exception.getMessage());

		verify(veiculoGateway).findByPlaca(null);

		verifyNoMoreInteractions(veiculoGateway);
	}
}