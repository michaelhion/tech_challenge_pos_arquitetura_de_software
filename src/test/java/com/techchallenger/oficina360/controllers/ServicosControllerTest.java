package com.techchallenger.oficina360.controllers;

import com.techchallenger.oficina360.frameworks.dtos.servicos.ServicoDTO;
import com.techchallenger.oficina360.frameworks.web.controllers.ServicosController;
import com.techchallenger.oficina360.usecases.servicos.AtualizarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.BuscarServicoPorCodigoUseCase;
import com.techchallenger.oficina360.usecases.servicos.CadastrarServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ExcluirServicoUseCase;
import com.techchallenger.oficina360.usecases.servicos.ListarServicosUseCase;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static com.techchallenger.oficina360.frameworks.mappers.servico.ServicoDTOMapper.dtoToCommand;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicosControllerTest {

	private static final String CODIGO_TROCA_OLEO = "TROCA-DE-OLEO";

	private static final String DESCRICAO_TROCA_OLEO = "Troca de óleo";

	private static final BigDecimal VALOR_TROCA_OLEO = new BigDecimal("150.00");

	private static final Integer TEMPO_MEDIO_TROCA_OLEO = 1;

	private static final String ALINHAMENTO_E_BALANCEAMENTO = "ALINHAMENTO-E-BALANCEAMENTO";

	@Mock
	private CadastrarServicoUseCase cadastrarServicoUseCase;

	@Mock
	private BuscarServicoPorCodigoUseCase buscarServicoPorCodigoUseCase;

	@Mock
	private ListarServicosUseCase listarServicosUseCase;

	@Mock
	private AtualizarServicoUseCase atualizarServicoUseCase;

	@Mock
	private ExcluirServicoUseCase excluirServicoUseCase;

	@InjectMocks
	private ServicosController servicosController;

	private ServicoDTO servicoDTO;
	private ServicoCommand servicoCommand;

	@BeforeEach
	void setUp() {
		servicoDTO = new ServicoDTO(CODIGO_TROCA_OLEO, DESCRICAO_TROCA_OLEO, VALOR_TROCA_OLEO, TEMPO_MEDIO_TROCA_OLEO);

		servicoCommand = dtoToCommand(servicoDTO);
	}

	@Test
	void deveBuscarServicoPorCodigoComSucesso() {
		when(buscarServicoPorCodigoUseCase.findByCodigo(CODIGO_TROCA_OLEO)).thenReturn(servicoCommand);

		ResponseEntity<ServicoDTO> response = servicosController.buscarPorId(CODIGO_TROCA_OLEO);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertServico(response.getBody(), CODIGO_TROCA_OLEO, DESCRICAO_TROCA_OLEO, VALOR_TROCA_OLEO,
				TEMPO_MEDIO_TROCA_OLEO);

		verify(buscarServicoPorCodigoUseCase, times(1)).findByCodigo(CODIGO_TROCA_OLEO);

		verifyNoMoreInteractions(buscarServicoPorCodigoUseCase);
	}

	@Test
	void devePropagarExcecaoQuandoServicoNaoExistir() {
		RuntimeException excecaoEsperada = new RuntimeException("Serviço não encontrado.");

		when(buscarServicoPorCodigoUseCase.findByCodigo(CODIGO_TROCA_OLEO)).thenThrow(excecaoEsperada);

		RuntimeException excecaoObtida = assertThrows(RuntimeException.class,
				() -> servicosController.buscarPorId(CODIGO_TROCA_OLEO));

		assertAll(() -> assertEquals(excecaoEsperada, excecaoObtida),
				() -> assertEquals("Serviço não encontrado.", excecaoObtida.getMessage()));

		verify(buscarServicoPorCodigoUseCase, times(1)).findByCodigo(CODIGO_TROCA_OLEO);

		verifyNoMoreInteractions(buscarServicoPorCodigoUseCase);
	}

	@Test
	void deveSalvarServicoComSucesso() {
		when(cadastrarServicoUseCase.save(servicoCommand)).thenReturn(servicoCommand);

		ResponseEntity<ServicoDTO> response = servicosController.salvar(servicoDTO);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		assertNotNull(response.getBody());

		assertServico(response.getBody(), CODIGO_TROCA_OLEO, DESCRICAO_TROCA_OLEO, VALOR_TROCA_OLEO,
				TEMPO_MEDIO_TROCA_OLEO);

		verify(cadastrarServicoUseCase, times(1)).save(servicoCommand);

		verifyNoMoreInteractions(cadastrarServicoUseCase);
	}

	@Test
	void deveEditarServicoComSucesso() {
		ServicoDTO servicoAtualizado = new ServicoDTO(ALINHAMENTO_E_BALANCEAMENTO, "Alinhamento e balanceamento",
				new BigDecimal("220.00"), 1);

		ServicoCommand commandEsperado = dtoToCommand(servicoAtualizado);

		when(atualizarServicoUseCase.edit(ALINHAMENTO_E_BALANCEAMENTO, commandEsperado)).thenReturn(commandEsperado);

		ResponseEntity<ServicoDTO> response = servicosController.editar(ALINHAMENTO_E_BALANCEAMENTO, servicoAtualizado);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertServico(response.getBody(), ALINHAMENTO_E_BALANCEAMENTO, "Alinhamento e balanceamento",
				new BigDecimal("220.00"), 1);

		verify(atualizarServicoUseCase, times(1)).edit(ALINHAMENTO_E_BALANCEAMENTO, commandEsperado);

		verifyNoMoreInteractions(atualizarServicoUseCase);
	}

	@Test
	void deveDeletarServicoComSucesso() {
		doNothing().when(excluirServicoUseCase).delete(ALINHAMENTO_E_BALANCEAMENTO);

		ResponseEntity<Void> response = servicosController.deletar(ALINHAMENTO_E_BALANCEAMENTO);

		assertAll(() -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
				() -> assertNull(response.getBody()));

		verify(excluirServicoUseCase, times(1)).delete(ALINHAMENTO_E_BALANCEAMENTO);

		verifyNoMoreInteractions(excluirServicoUseCase);
	}

	@Test
	void deveListarServicosComSucesso() {
		ServicoDTO segundoServicoDTO = new ServicoDTO("TROCA-DE-PASTILHA-DE-FREIO", "Troca de pastilha de freio",
				new BigDecimal("300.00"), 3);

		ServicoCommand segundoServicoCommand = dtoToCommand(segundoServicoDTO);

		when(listarServicosUseCase.findAll()).thenReturn(List.of(servicoCommand, segundoServicoCommand));

		ResponseEntity<List<ServicoDTO>> response = servicosController.listarServicos();

		assertEquals(HttpStatus.OK, response.getStatusCode());

		assertNotNull(response.getBody());

		assertEquals(2, response.getBody().size());

		assertServico(response.getBody().get(0), CODIGO_TROCA_OLEO, DESCRICAO_TROCA_OLEO, VALOR_TROCA_OLEO,
				TEMPO_MEDIO_TROCA_OLEO);

		assertServico(response.getBody().get(1), "TROCA-DE-PASTILHA-DE-FREIO", "Troca de pastilha de freio",
				new BigDecimal("300.00"), 3);

		verify(listarServicosUseCase, times(1)).findAll();

		verifyNoMoreInteractions(listarServicosUseCase);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremServicos() {
		when(listarServicosUseCase.findAll()).thenReturn(List.of());

		ResponseEntity<List<ServicoDTO>> response = servicosController.listarServicos();

		assertAll(() -> assertEquals(HttpStatus.OK, response.getStatusCode()), () -> assertNotNull(response.getBody()),
				() -> assertTrue(response.getBody().isEmpty()));

		verify(listarServicosUseCase, times(1)).findAll();

		verifyNoMoreInteractions(listarServicosUseCase);
	}

	private void assertServico(ServicoDTO resultado, String codigoEsperado, String descricaoEsperada,
			BigDecimal valorEsperado, Integer tempoMedioEsperado) {
		assertAll(() -> assertEquals(codigoEsperado, resultado.codigo()),
				() -> assertEquals(descricaoEsperada, resultado.descricao()),
				() -> assertEquals(0, valorEsperado.compareTo(resultado.valor())),
				() -> assertEquals(tempoMedioEsperado, resultado.tempoDeExecucaoMedio()));
	}
}