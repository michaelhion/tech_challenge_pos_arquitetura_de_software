package com.techchallenger.oficina360.usecases.veiculo;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.gateways.VeiculoGateway;
import com.techchallenger.oficina360.usecases.finders.ClienteFinder;
import com.techchallenger.oficina360.usecases.finders.VeiculoFinder;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;
import com.techchallenger.oficina360.usecases.veiculo.exception.PlacaJaExisteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.VEICULO_SERV_VEICULO_CADASTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtualizarVeiculoUseCaseTest {

	private static final UUID VEICULO_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String PLACA_ATUAL = "ABC1D23";

	private static final String NOVA_PLACA = "DEF2G34";

	private static final String MARCA = "Toyota";

	private static final String MODELO = "Corolla";

	private static final String ANO = "2022";

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	@Mock
	private VeiculoGateway veiculoGateway;

	@Mock
	private VeiculoFinder veiculoFinder;

	@Mock
	private ClienteFinder clienteFinder;

	@Mock
	private Veiculo veiculo;

	@Mock
	private Cliente cliente;

	@InjectMocks
	private AtualizarVeiculoUseCase useCase;

	private VeiculoCommand command;

	@BeforeEach
	void setUp() {
		command = new VeiculoCommand(NOVA_PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);
	}

	@Test
	void deveAtualizarVeiculoComSucesso() {
		configurarFluxoValido();

		configurarVeiculoAtualizado();

		VeiculoCommand resultado = useCase.edit(PLACA_ATUAL, command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(NOVA_PLACA, resultado.placa()), () -> assertEquals(MARCA, resultado.marca()),
				() -> assertEquals(MODELO, resultado.modelo()), () -> assertEquals(ANO, resultado.ano()),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.clienteDocumento()));

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA_ATUAL);

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway).existsByPlacaAndIdNot(NOVA_PLACA, VEICULO_ID);

		verify(veiculo).editar(NOVA_PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		verify(veiculoGateway).save(veiculo);
	}

	@Test
	void deveNormalizarPlacaAtualNovaPlacaEDocumento() {
		String placaAtualInformada = " abc1d23 ";

		VeiculoCommand commandComDadosFormatados = new VeiculoCommand(" def2g34 ", MARCA, MODELO, ANO,
				"123.456.789-01");

		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA_ATUAL)).thenReturn(veiculo);

		when(veiculo.getId()).thenReturn(VEICULO_ID);

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		when(veiculoGateway.existsByPlacaAndIdNot(NOVA_PLACA, VEICULO_ID)).thenReturn(false);

		when(veiculoGateway.save(veiculo)).thenReturn(veiculo);

		configurarVeiculoAtualizado();

		useCase.edit(placaAtualInformada, commandComDadosFormatados);

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA_ATUAL);

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway).existsByPlacaAndIdNot(NOVA_PLACA, VEICULO_ID);

		verify(veiculo).editar(" def2g34 ", MARCA, MODELO, ANO, "123.456.789-01");
	}

	@Test
	void deveBuscarVeiculoClienteValidarPlacaEditarESalvarNestaOrdem() {
		configurarFluxoValido();
		configurarVeiculoAtualizado();

		useCase.edit(PLACA_ATUAL, command);

		InOrder ordemDasChamadas = inOrder(veiculoFinder, clienteFinder, veiculoGateway, veiculo);

		ordemDasChamadas.verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA_ATUAL);

		ordemDasChamadas.verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		ordemDasChamadas.verify(veiculo).getId();

		ordemDasChamadas.verify(veiculoGateway).existsByPlacaAndIdNot(NOVA_PLACA, VEICULO_ID);

		ordemDasChamadas.verify(veiculo).editar(NOVA_PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		ordemDasChamadas.verify(veiculoGateway).save(veiculo);
	}

	@Test
	void deveLancarExcecaoQuandoNovaPlacaPertencerAOutroVeiculo() {
		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA_ATUAL)).thenReturn(veiculo);

		when(veiculo.getId()).thenReturn(VEICULO_ID);

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		when(veiculoGateway.existsByPlacaAndIdNot(NOVA_PLACA, VEICULO_ID)).thenReturn(true);

		PlacaJaExisteException exception = assertThrows(PlacaJaExisteException.class,
				() -> useCase.edit(PLACA_ATUAL, command));

		assertEquals(VEICULO_SERV_VEICULO_CADASTRADO, exception.getMessage());

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA_ATUAL);

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verify(veiculoGateway).existsByPlacaAndIdNot(NOVA_PLACA, VEICULO_ID);

		verify(veiculo, never()).editar(any(), any(), any(), any(), any());

		verify(veiculoGateway, never()).save(any(Veiculo.class));

		verifyNoMoreInteractions(clienteFinder, veiculoGateway);
	}

	@Test
	void devePropagarExcecaoQuandoVeiculoAtualNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException("Veículo não encontrado");

		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA_ATUAL)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.edit(PLACA_ATUAL, command));

		assertSame(excecaoEsperada, excecaoObtida);

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA_ATUAL);

		verifyNoMoreInteractions(veiculoFinder);

		verifyNoMoreInteractions(clienteFinder, veiculoGateway, veiculo);
	}

	@Test
	void devePropagarExcecaoQuandoClienteNaoForEncontrado() {
		RecursoNaoEncontradoException excecaoEsperada = new RecursoNaoEncontradoException("Cliente não encontrado");

		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA_ATUAL)).thenReturn(veiculo);

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenThrow(excecaoEsperada);

		RecursoNaoEncontradoException excecaoObtida = assertThrows(RecursoNaoEncontradoException.class,
				() -> useCase.edit(PLACA_ATUAL, command));

		assertSame(excecaoEsperada, excecaoObtida);

		verify(veiculoFinder).buscarPorPlacaOuFalhar(PLACA_ATUAL);

		verify(clienteFinder).buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE);

		verifyNoMoreInteractions(veiculoFinder, clienteFinder);

		verifyNoMoreInteractions(veiculoGateway, veiculo);
	}

	private void configurarFluxoValido() {
		when(veiculoFinder.buscarPorPlacaOuFalhar(PLACA_ATUAL)).thenReturn(veiculo);

		when(veiculo.getId()).thenReturn(VEICULO_ID);

		when(clienteFinder.buscarPorDocumentoOuFalhar(DOCUMENTO_CLIENTE)).thenReturn(cliente);

		when(veiculoGateway.existsByPlacaAndIdNot(NOVA_PLACA, VEICULO_ID)).thenReturn(false);

		when(veiculoGateway.save(veiculo)).thenReturn(veiculo);
	}

	private void configurarVeiculoAtualizado() {
		when(veiculo.getPlaca()).thenReturn(NOVA_PLACA);

		when(veiculo.getMarca()).thenReturn(MARCA);

		when(veiculo.getModelo()).thenReturn(MODELO);

		when(veiculo.getAno()).thenReturn(ANO);

		when(veiculo.getClienteDocumento()).thenReturn(DOCUMENTO_CLIENTE);
	}
}