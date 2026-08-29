package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.frameworks.dtos.veiculos.VeiculoDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.VeiculosController;
import com.techchallenger.oficina360.usecases.veiculo.AtualizarVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.BuscarVeiculoPorPlacaUseCase;
import com.techchallenger.oficina360.usecases.veiculo.CadastrarVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.ExcluirVeiculoUseCase;
import com.techchallenger.oficina360.usecases.veiculo.ListarVeiculosUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.techchallenger.oficina360.frameworks.mappers.veiculo.VeiculoDTOMapper.dtoToCommand;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculosControllerTest {

	private static final String PLACA = "ABC1D23";
	private static final String PLACA_MASCARADA = "ABC***23";

	private static final String MARCA = "Volkswagen";

	private static final String MODELO = "Gol";

	private static final Integer ANO = 2020;

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String PLACA_ATUALIZADA = "DEF2G34";
	private static final String PLACA_ATUALIZADA_MASCARADA = "DEF***34";

	private static final String PLACA_INEXISTENTE = "ZZZ9Z99";

	@Mock
	private CadastrarVeiculoUseCase cadastrarVeiculoUseCase;

	@Mock
	private AtualizarVeiculoUseCase atualizarVeiculoUseCase;

	@Mock
	private BuscarVeiculoPorPlacaUseCase buscarVeiculoPorPlacaUseCase;

	@Mock
	private ListarVeiculosUseCase listarVeiculosUseCase;

	@Mock
	private ExcluirVeiculoUseCase excluirVeiculoUseCase;

	@InjectMocks
	private VeiculosController veiculosController;

	private VeiculoDTO veiculoDTO;

	@BeforeEach
	void setUp() {
		veiculoDTO = new VeiculoDTO(PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);
	}

	@Test
	void deveBuscarVeiculoPorPlacaComSucesso() {
		var commandRetornado = dtoToCommand(veiculoDTO);

		when(buscarVeiculoPorPlacaUseCase.findByPlaca(PLACA)).thenReturn(commandRetornado);

		ResponseEntity<VeiculoDTO> response = veiculosController.buscarPorPlaca(PLACA);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertVeiculo(response.getBody(), PLACA_MASCARADA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		verify(buscarVeiculoPorPlacaUseCase, times(1)).findByPlaca(PLACA);

		verifyNoMoreInteractions(buscarVeiculoPorPlacaUseCase);
	}

	@Test
	void devePropagarExcecaoQuandoVeiculoNaoExistir() {
		RuntimeException excecaoEsperada = new RuntimeException("Veículo não encontrado.");

		when(buscarVeiculoPorPlacaUseCase.findByPlaca(PLACA_INEXISTENTE)).thenThrow(excecaoEsperada);

		RuntimeException excecaoObtida = assertThrows(RuntimeException.class,
				() -> veiculosController.buscarPorPlaca(PLACA_INEXISTENTE));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals("Veículo não encontrado.", excecaoObtida.getMessage()));

		verify(buscarVeiculoPorPlacaUseCase, times(1)).findByPlaca(PLACA_INEXISTENTE);

		verifyNoMoreInteractions(buscarVeiculoPorPlacaUseCase);
	}

	@Test
	void deveSalvarVeiculoComSucesso() {
		var commandEsperado = dtoToCommand(veiculoDTO);

		when(cadastrarVeiculoUseCase.save(commandEsperado)).thenReturn(commandEsperado);

		ResponseEntity<VeiculoDTO> response = veiculosController.salvar(veiculoDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		assertNotNull(response.getBody());

		assertVeiculo(response.getBody(), PLACA_MASCARADA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		verify(cadastrarVeiculoUseCase, times(1)).save(commandEsperado);

		verifyNoMoreInteractions(cadastrarVeiculoUseCase);
	}

	@Test
	void deveEditarVeiculoComSucesso() {
		VeiculoDTO veiculoAtualizado = new VeiculoDTO(PLACA_ATUALIZADA, "Toyota", "Corolla", 2022, DOCUMENTO_CLIENTE);

		var commandEsperado = dtoToCommand(veiculoAtualizado);

		when(atualizarVeiculoUseCase.edit(PLACA, commandEsperado)).thenReturn(commandEsperado);

		ResponseEntity<VeiculoDTO> response = veiculosController.editar(PLACA, veiculoAtualizado);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertVeiculo(response.getBody(), PLACA_ATUALIZADA_MASCARADA, "Toyota", "Corolla", 2022, DOCUMENTO_CLIENTE);

		verify(atualizarVeiculoUseCase, times(1)).edit(PLACA, commandEsperado);

		verifyNoMoreInteractions(atualizarVeiculoUseCase);
	}

	@Test
	void deveDeletarVeiculoPorPlacaComSucesso() {
		doNothing().when(excluirVeiculoUseCase).delete(PLACA);

		ResponseEntity<Void> response = veiculosController.deletar(PLACA);

		assertAll(() -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
				() -> assertNull(response.getBody()));

		verify(excluirVeiculoUseCase, times(1)).delete(PLACA);

		verifyNoMoreInteractions(excluirVeiculoUseCase);
	}

	@Test
	void deveListarVeiculosComSucesso() {
		VeiculoDTO segundoVeiculoDTO = new VeiculoDTO(PLACA_ATUALIZADA, "Toyota", "Corolla", 2022, "98765432100");

		var primeiroVeiculoCommand = dtoToCommand(veiculoDTO);

		var segundoVeiculoCommand = dtoToCommand(segundoVeiculoDTO);

		when(listarVeiculosUseCase.findAll()).thenReturn(List.of(primeiroVeiculoCommand, segundoVeiculoCommand));

		ResponseEntity<List<VeiculoDTO>> response = veiculosController.listarVeiculos();

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEquals(2, response.getBody().size());

		assertVeiculo(response.getBody().get(0), PLACA_MASCARADA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		assertVeiculo(response.getBody().get(1), PLACA_ATUALIZADA_MASCARADA, "Toyota", "Corolla", 2022, "98765432100");

		verify(listarVeiculosUseCase, times(1)).findAll();

		verifyNoMoreInteractions(listarVeiculosUseCase);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremVeiculos() {
		when(listarVeiculosUseCase.findAll()).thenReturn(List.of());

		ResponseEntity<List<VeiculoDTO>> response = veiculosController.listarVeiculos();

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()), () -> assertNotNull(response.getBody()),
				() -> assertTrue(response.getBody().isEmpty()));

		verify(listarVeiculosUseCase, times(1)).findAll();

		verifyNoMoreInteractions(listarVeiculosUseCase);
	}

	private void assertVeiculo(VeiculoDTO resultado, String placaEsperada, String marcaEsperada, String modeloEsperado,
			Integer anoEsperado, String documentoClienteEsperado) {
		assertAll(() -> assertEquals(placaEsperada, resultado.placa()),
				() -> assertEquals(marcaEsperada, resultado.marca()),
				() -> assertEquals(modeloEsperado, resultado.modelo()),
				() -> assertEquals(anoEsperado, resultado.ano()),
				() -> assertEquals(documentoClienteEsperado, resultado.clienteDocumento()));
	}
}