package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcluirVeiculoUseCaseTest {

	private static final String PLACA = "ABC1D23";

	private static final String PLACA_FORMATADA = " abc1d23 ";

	private static final String VEICULO_NAO_ENCONTRADO = "Veículo não encontrado";

	@Mock
	private VeiculoGateway veiculoGateway;

	@Mock
	private VeiculoFinder veiculoFinder;

	@Mock
	private Veiculo veiculo;

	@InjectMocks
	private ExcluirVeiculoUseCase useCase;

	@Test
	void deveExcluirVeiculoComSucesso() {
		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA)).thenReturn(veiculo);

		assertDoesNotThrow(() -> useCase.delete(PLACA));

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA);

		verify(veiculoGateway).deleteByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoFinder, veiculoGateway);
	}

	@Test
	void deveNormalizarPlacaAntesDeBuscarEExcluirVeiculo() {
		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA)).thenReturn(veiculo);

		useCase.delete(PLACA_FORMATADA);

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA);

		verify(veiculoGateway).deleteByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoFinder, veiculoGateway);
	}

	@Test
	void deveBuscarVeiculoAntesDeExcluir() {
		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA)).thenReturn(veiculo);

		useCase.delete(PLACA);

		InOrder ordemDasChamadas = inOrder(veiculoFinder, veiculoGateway);

		ordemDasChamadas.verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA);

		ordemDasChamadas.verify(veiculoGateway).deleteByPlaca(PLACA);

		ordemDasChamadas.verifyNoMoreInteractions();
	}

	@Test
	void devePropagarExcecaoQuandoVeiculoNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException(VEICULO_NAO_ENCONTRADO);

		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.delete(PLACA));

		assertSame(excecaoEsperada, excecaoObtida);

		assertEquals(VEICULO_NAO_ENCONTRADO, excecaoObtida.getMessage());

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA);

		verify(veiculoGateway, never()).deleteByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoFinder, veiculoGateway);
	}

	@Test
	void naoDeveExcluirVeiculoFormatadoQuandoNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException(VEICULO_NAO_ENCONTRADO);

		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.delete(PLACA_FORMATADA));

		assertSame(excecaoEsperada, excecaoObtida);

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA);

		verify(veiculoGateway, never()).deleteByPlaca(PLACA);

		verifyNoMoreInteractions(veiculoFinder, veiculoGateway);
	}
}