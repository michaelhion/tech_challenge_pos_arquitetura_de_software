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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueControllerTest {

	private static final String FILTRO_DE_OLEO = "FILTRO-DE-OLEO";
	private static final String FILTRO_DE_OLEO_PREMIUM = "FILTRO-DE-OLEO-PREMIUM";

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

	private UUID estoqueId;

	private EstoqueDTO estoqueDTO;

	@BeforeEach
	void setUp() {
		estoqueController = new EstoqueController(criarItemEstoqueUseCase, editarItemEstoqueUseCase,
				buscarItemEstoqueUseCase, listarItensEstoqueUseCase, excluirItemEstoqueUseCase,reservarEstoqueUseCase);

		estoqueId = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

		estoqueDTO = new EstoqueDTO(
				estoqueId,
				FILTRO_DE_OLEO,
				"Filtro de óleo",
				BigDecimal.valueOf(45.90),
				20,
				5,
				15
		);
	}

	@Test
	void deveBuscarPorIdComSucesso() {
		when(buscarItemEstoqueUseCase.findByCodigo(estoqueDTO.codigo()))
				.thenReturn(Optional.of(estoqueDTO));

		ResponseEntity<EstoqueDTO> response = estoqueController.buscarPorId(estoqueDTO.codigo());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Filtro de óleo", response.getBody().nome());
		assertEquals(BigDecimal.valueOf(45.90), response.getBody().valor());
		assertEquals(20, response.getBody().quantidade());
		assertEquals(5, response.getBody().reservados());
		assertEquals(15, response.getBody().disponiveis());

		verify(buscarItemEstoqueUseCase, times(1)).findByCodigo(estoqueDTO.codigo());
	}

	@Test
	void deveRetornarNotFoundQuandoNaoEncontrarPorId() {
		when(buscarItemEstoqueUseCase.findByCodigo(estoqueDTO.codigo()))
				.thenReturn(Optional.empty());

		ResponseEntity<EstoqueDTO> response = estoqueController.buscarPorId(estoqueDTO.codigo());

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());

		verify(buscarItemEstoqueUseCase, times(1)).findByCodigo(estoqueDTO.codigo());
	}

	@Test
	void deveSalvarComSucesso() {
		when(criarItemEstoqueUseCase.save(estoqueDTO))
				.thenReturn(estoqueDTO);

		ResponseEntity<EstoqueDTO> response = estoqueController.salvar(estoqueDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Filtro de óleo", response.getBody().nome());
		assertEquals(BigDecimal.valueOf(45.90), response.getBody().valor());
		assertEquals(20, response.getBody().quantidade());
		assertEquals(5, response.getBody().reservados());
		assertEquals(15, response.getBody().disponiveis());

		verify(criarItemEstoqueUseCase, times(1)).save(estoqueDTO);
	}

	@Test
	void deveEditarComSucesso() {
		EstoqueDTO estoqueAtualizado = new EstoqueDTO(
				estoqueId,
				FILTRO_DE_OLEO_PREMIUM,
				"Filtro de óleo premium",
				BigDecimal.valueOf(60.00),
				30,
				10,
				20
		);

		when(editarItemEstoqueUseCase.edit(FILTRO_DE_OLEO_PREMIUM, estoqueAtualizado))
				.thenReturn(estoqueAtualizado);

		ResponseEntity<EstoqueDTO> response = estoqueController.editar(FILTRO_DE_OLEO_PREMIUM, estoqueAtualizado);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Filtro de óleo premium", response.getBody().nome());
		assertEquals(BigDecimal.valueOf(60.00), response.getBody().valor());
		assertEquals(30, response.getBody().quantidade());
		assertEquals(10, response.getBody().reservados());
		assertEquals(20, response.getBody().disponiveis());

		verify(editarItemEstoqueUseCase, times(1)).edit(FILTRO_DE_OLEO_PREMIUM, estoqueAtualizado);
	}

	@Test
	void deveDeletarComSucesso() {
		doNothing().when(excluirItemEstoqueUseCase).delete(FILTRO_DE_OLEO);

		ResponseEntity<Void> response = estoqueController.deletar(FILTRO_DE_OLEO);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
		assertNull(response.getBody());

		verify(excluirItemEstoqueUseCase, times(1)).delete(FILTRO_DE_OLEO);
	}

	@Test
	void deveListarEstoquesComSucesso() {
		EstoqueDTO segundoItem = new EstoqueDTO(
				estoqueId,
				"PASTILHA-DE-FREIO",
				"Pastilha de freio",
				BigDecimal.valueOf(120.00),
				10,
				2,
				8
		);

		when(listarItensEstoqueUseCase.findAll())
				.thenReturn(List.of(estoqueDTO, segundoItem));

		ResponseEntity<List<EstoqueDTO>> response = estoqueController.listarEstoques();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(2, response.getBody().size());

		assertEquals("Filtro de óleo", response.getBody().get(0).nome());
		assertEquals(BigDecimal.valueOf(45.90), response.getBody().get(0).valor());
		assertEquals(20, response.getBody().get(0).quantidade());
		assertEquals(5, response.getBody().get(0).reservados());
		assertEquals(15, response.getBody().get(0).disponiveis());

		assertEquals("Pastilha de freio", response.getBody().get(1).nome());
		assertEquals(BigDecimal.valueOf(120.00), response.getBody().get(1).valor());
		assertEquals(10, response.getBody().get(1).quantidade());
		assertEquals(2, response.getBody().get(1).reservados());
		assertEquals(8, response.getBody().get(1).disponiveis());

		verify(listarItensEstoqueUseCase, times(1)).findAll();
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremEstoques() {
		when(listarItensEstoqueUseCase.findAll())
				.thenReturn(List.of());

		ResponseEntity<List<EstoqueDTO>> response = estoqueController.listarEstoques();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody().isEmpty());

		verify(listarItensEstoqueUseCase, times(1)).findAll();
	}

	@Test
	void deveReservarComSucesso() {
		ReservaEstoqueDTO reservaDTO = new ReservaEstoqueDTO(3);

		EstoqueDTO estoqueReservado = new EstoqueDTO(
				estoqueId,
				FILTRO_DE_OLEO,
				"Filtro de óleo",
				BigDecimal.valueOf(45.90),
				20,
				8,
				12
		);

		when(reservarEstoqueUseCase.reservar(FILTRO_DE_OLEO, reservaDTO))
				.thenReturn(estoqueReservado);

		ResponseEntity<EstoqueDTO> response = estoqueController.reservar(FILTRO_DE_OLEO, reservaDTO);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Filtro de óleo", response.getBody().nome());
		assertEquals(BigDecimal.valueOf(45.90), response.getBody().valor());
		assertEquals(20, response.getBody().quantidade());
		assertEquals(8, response.getBody().reservados());
		assertEquals(12, response.getBody().disponiveis());

		verify(reservarEstoqueUseCase, times(1)).reservar(FILTRO_DE_OLEO, reservaDTO);
	}
}