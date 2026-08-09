package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.EstoqueController;
import com.techchallenger.oficina360.usecases.estoque.BuscarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.CriarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.EditarItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ExcluirItemEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ListarItensEstoqueUseCase;
import com.techchallenger.oficina360.usecases.estoque.ReservarEstoqueUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.techchallenger.oficina360.frameworks.mappers.estoque.EstoqueDTOMapper.dtoToCommand;
import static com.techchallenger.oficina360.frameworks.mappers.estoque.EstoqueDTOMapper.reservarDTOToCommand;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueControllerTest {

	private static final String FILTRO_DE_OLEO = "FILTRO-DE-OLEO";

	private static final String FILTRO_DE_OLEO_PREMIUM = "FILTRO-DE-OLEO-PREMIUM";

	private static final String NOME_FILTRO_DE_OLEO = "Filtro de óleo";

	private static final BigDecimal VALOR_FILTRO_DE_OLEO = new BigDecimal("45.90");

	private static final int QUANTIDADE_FILTRO_DE_OLEO = 20;

	private static final int RESERVADOS_FILTRO_DE_OLEO = 5;

	private static final int DISPONIVEIS_FILTRO_DE_OLEO = 15;

	@Mock
	private CriarItemEstoqueUseCase criarItemEstoqueUseCase;

	@Mock
	private EditarItemEstoqueUseCase editarItemEstoqueUseCase;

	@Mock
	private BuscarItemEstoqueUseCase buscarItemEstoqueUseCase;

	@Mock
	private ListarItensEstoqueUseCase listarItensEstoqueUseCase;

	@Mock
	private ExcluirItemEstoqueUseCase excluirItemEstoqueUseCase;

	@Mock
	private ReservarEstoqueUseCase reservarEstoqueUseCase;

	private EstoqueController estoqueController;

	private EstoqueDTO estoqueDTO;

	@BeforeEach
	void setUp() {
		estoqueController = new EstoqueController(criarItemEstoqueUseCase, editarItemEstoqueUseCase,
				buscarItemEstoqueUseCase, listarItensEstoqueUseCase, excluirItemEstoqueUseCase, reservarEstoqueUseCase);

		estoqueDTO = new EstoqueDTO(FILTRO_DE_OLEO, NOME_FILTRO_DE_OLEO, VALOR_FILTRO_DE_OLEO,
				QUANTIDADE_FILTRO_DE_OLEO, RESERVADOS_FILTRO_DE_OLEO,DISPONIVEIS_FILTRO_DE_OLEO);
	}

	@Test
	void deveBuscarPorCodigoComSucesso() {
		var commandRetornado = dtoToCommand(estoqueDTO);

		when(buscarItemEstoqueUseCase.findByCodigo(FILTRO_DE_OLEO)).thenReturn(commandRetornado);

		ResponseEntity<EstoqueDTO> response = estoqueController.buscarPorId(FILTRO_DE_OLEO);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEstoque(response.getBody(), FILTRO_DE_OLEO, NOME_FILTRO_DE_OLEO, VALOR_FILTRO_DE_OLEO,
				QUANTIDADE_FILTRO_DE_OLEO, RESERVADOS_FILTRO_DE_OLEO);

		verify(buscarItemEstoqueUseCase, times(1)).findByCodigo(FILTRO_DE_OLEO);

		verifyNoMoreInteractions(buscarItemEstoqueUseCase);
	}

	@Test
	void devePropagarExcecaoQuandoNaoEncontrarPorCodigo() {
		RuntimeException excecaoEsperada = new RuntimeException("Item de estoque não encontrado.");

		when(buscarItemEstoqueUseCase.findByCodigo(FILTRO_DE_OLEO)).thenThrow(excecaoEsperada);

		RuntimeException excecaoObtida = assertThrows(RuntimeException.class,
				() -> estoqueController.buscarPorId(FILTRO_DE_OLEO));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals("Item de estoque não encontrado.", excecaoObtida.getMessage()));

		verify(buscarItemEstoqueUseCase, times(1)).findByCodigo(FILTRO_DE_OLEO);

		verifyNoMoreInteractions(buscarItemEstoqueUseCase);
	}

	@Test
	void deveSalvarComSucesso() {
		var commandEsperado = dtoToCommand(estoqueDTO);

		when(criarItemEstoqueUseCase.save(commandEsperado)).thenReturn(commandEsperado);

		ResponseEntity<EstoqueDTO> response = estoqueController.salvar(estoqueDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEstoque(response.getBody(), FILTRO_DE_OLEO, NOME_FILTRO_DE_OLEO, VALOR_FILTRO_DE_OLEO,
				QUANTIDADE_FILTRO_DE_OLEO, RESERVADOS_FILTRO_DE_OLEO);

		verify(criarItemEstoqueUseCase, times(1)).save(commandEsperado);

		verifyNoMoreInteractions(criarItemEstoqueUseCase);
	}

	@Test
	void deveEditarComSucesso() {
		EstoqueDTO estoqueAtualizado = new EstoqueDTO(FILTRO_DE_OLEO_PREMIUM, "Filtro de óleo premium",
				new BigDecimal("60.00"), 30, 10,0);

		var commandEsperado = dtoToCommand(estoqueAtualizado);

		when(editarItemEstoqueUseCase.edit(FILTRO_DE_OLEO, commandEsperado)).thenReturn(commandEsperado);

		ResponseEntity<EstoqueDTO> response = estoqueController.editar(FILTRO_DE_OLEO, estoqueAtualizado);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEstoque(response.getBody(), FILTRO_DE_OLEO_PREMIUM, "Filtro de óleo premium",
				new BigDecimal("60.00"), 30, 10);

		verify(editarItemEstoqueUseCase, times(1)).edit(FILTRO_DE_OLEO, commandEsperado);

		verifyNoMoreInteractions(editarItemEstoqueUseCase);
	}

	@Test
	void deveDeletarComSucesso() {
		doNothing().when(excluirItemEstoqueUseCase).delete(FILTRO_DE_OLEO);

		ResponseEntity<Void> response = estoqueController.deletar(FILTRO_DE_OLEO);

		assertAll(() -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
				() -> assertNull(response.getBody()));

		verify(excluirItemEstoqueUseCase, times(1)).delete(FILTRO_DE_OLEO);

		verifyNoMoreInteractions(excluirItemEstoqueUseCase);
	}

	@Test
	void deveListarEstoquesComSucesso() {

		EstoqueDTO segundoItem = new EstoqueDTO("PASTILHA-DE-FREIO", "Pastilha de freio",
				new BigDecimal("120.00"), 10, 2,0);

		var primeiroItemCommand = dtoToCommand(estoqueDTO);

		var segundoItemCommand = dtoToCommand(segundoItem);

		when(listarItensEstoqueUseCase.findAll()).thenReturn(List.of(primeiroItemCommand, segundoItemCommand));

		ResponseEntity<List<EstoqueDTO>> response = estoqueController.listarEstoques();

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEquals(2, response.getBody().size());

		assertEstoque(response.getBody().get(0), FILTRO_DE_OLEO, NOME_FILTRO_DE_OLEO, VALOR_FILTRO_DE_OLEO,
				QUANTIDADE_FILTRO_DE_OLEO, RESERVADOS_FILTRO_DE_OLEO);

		assertEstoque(response.getBody().get(1), "PASTILHA-DE-FREIO", "Pastilha de freio",
				new BigDecimal("120.00"), 10, 2);

		verify(listarItensEstoqueUseCase, times(1)).findAll();

		verifyNoMoreInteractions(listarItensEstoqueUseCase);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremEstoques() {
		when(listarItensEstoqueUseCase.findAll()).thenReturn(List.of());

		ResponseEntity<List<EstoqueDTO>> response = estoqueController.listarEstoques();

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()), () -> assertNotNull(response.getBody()),
				() -> assertTrue(response.getBody().isEmpty()));

		verify(listarItensEstoqueUseCase, times(1)).findAll();

		verifyNoMoreInteractions(listarItensEstoqueUseCase);
	}

	@Test
	void deveReservarComSucesso() {
		ReservaEstoqueDTO reservaDTO = new ReservaEstoqueDTO(3);

		var reservaCommandEsperado = reservarDTOToCommand(reservaDTO);

		EstoqueDTO estoqueReservado = new EstoqueDTO(FILTRO_DE_OLEO, NOME_FILTRO_DE_OLEO,
				VALOR_FILTRO_DE_OLEO, QUANTIDADE_FILTRO_DE_OLEO, 8,0);

		var estoqueReservadoCommand = dtoToCommand(estoqueReservado);

		when(reservarEstoqueUseCase.reservar(FILTRO_DE_OLEO, reservaCommandEsperado)).thenReturn(
				estoqueReservadoCommand);

		ResponseEntity<EstoqueDTO> response = estoqueController.reservar(FILTRO_DE_OLEO, reservaDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEstoque(response.getBody(),  FILTRO_DE_OLEO, NOME_FILTRO_DE_OLEO, VALOR_FILTRO_DE_OLEO,
				QUANTIDADE_FILTRO_DE_OLEO, 8);

		verify(reservarEstoqueUseCase, times(1)).reservar(FILTRO_DE_OLEO, reservaCommandEsperado);

		verifyNoMoreInteractions(reservarEstoqueUseCase);
	}

	private void assertEstoque(EstoqueDTO resultado, String codigoEsperado, String nomeEsperado,
			BigDecimal valorEsperado, int quantidadeEsperada, int reservadosEsperados) {
		assertAll(() -> assertEquals(codigoEsperado, resultado.codigo()),
				() -> assertEquals(nomeEsperado, resultado.nome()),
				() -> assertEquals(0, valorEsperado.compareTo(resultado.valor())),
				() -> assertEquals(quantidadeEsperada, resultado.quantidade()),
				() -> assertEquals(reservadosEsperados, resultado.reservados()));
	}
}