package com.techchallenger.oficina360.usecases.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarItensEstoqueUseCaseTest {

	private static final UUID PRIMEIRO_ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final UUID SEGUNDO_ESTOQUE_ID = UUID.fromString("3c4efe7e-3f54-5a3f-9fb4-37825c2409f9");

	private static final String CODIGO_FILTRO = "FILTRO-DE-OLEO";

	private static final String CODIGO_PASTILHA = "PASTILHA-DE-FREIO";

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private ListarItensEstoqueUseCase useCase;

	@Test
	void deveListarItensEstoqueComSucesso() {
		Estoque filtroDeOleo = criarFiltroDeOleo();

		Estoque pastilhaDeFreio = criarPastilhaDeFreio();

		when(estoqueGateway.findAll()).thenReturn(List.of(filtroDeOleo, pastilhaDeFreio));

		List<EstoqueCommand> resultado = useCase.findAll();

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		EstoqueCommand primeiroItem = resultado.get(0);

		EstoqueCommand segundoItem = resultado.get(1);

		assertEstoque(primeiroItem, PRIMEIRO_ESTOQUE_ID, CODIGO_FILTRO, "Filtro de óleo", new BigDecimal("45.90"), 20,
				5, 15);

		assertEstoque(segundoItem, SEGUNDO_ESTOQUE_ID, CODIGO_PASTILHA, "Pastilha de freio", new BigDecimal("120.00"),
				10, 2, 8);

		verify(estoqueGateway, times(1)).findAll();

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void devePreservarOrdemDosItensRetornadosPeloGateway() {
		Estoque filtroDeOleo = criarFiltroDeOleo();

		Estoque pastilhaDeFreio = criarPastilhaDeFreio();

		when(estoqueGateway.findAll()).thenReturn(List.of(pastilhaDeFreio, filtroDeOleo));

		List<EstoqueCommand> resultado = useCase.findAll();

		assertAll(() -> assertEquals(2, resultado.size()),
				() -> assertEquals(CODIGO_PASTILHA, resultado.get(0).codigo()),
				() -> assertEquals(CODIGO_FILTRO, resultado.get(1).codigo()));

		verify(estoqueGateway, times(1)).findAll();

		verifyNoMoreInteractions(estoqueGateway);
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoExistiremItensEstoque() {
		when(estoqueGateway.findAll()).thenReturn(List.of());

		List<EstoqueCommand> resultado = useCase.findAll();

		assertAll(() -> assertNotNull(resultado), () -> assertTrue(resultado.isEmpty()));

		verify(estoqueGateway, times(1)).findAll();

		verifyNoMoreInteractions(estoqueGateway);
	}

	private Estoque criarFiltroDeOleo() {
		return new Estoque(PRIMEIRO_ESTOQUE_ID, "Filtro de óleo", new BigDecimal("45.90"), 20, 5, CODIGO_FILTRO);
	}

	private Estoque criarPastilhaDeFreio() {
		return new Estoque(SEGUNDO_ESTOQUE_ID, "Pastilha de freio", new BigDecimal("120.00"), 10, 2, CODIGO_PASTILHA);
	}

	private void assertEstoque(EstoqueCommand resultado, UUID idEsperado, String codigoEsperado, String nomeEsperado,
			BigDecimal valorEsperado, int quantidadeEsperada, int reservadosEsperados, int disponiveisEsperados) {
		assertAll(
				() -> assertEquals(codigoEsperado, resultado.codigo()),
				() -> assertEquals(nomeEsperado, resultado.nome()),
				() -> assertEquals(0, valorEsperado.compareTo(resultado.valor())),
				() -> assertEquals(quantidadeEsperada, resultado.quantidade()),
				() -> assertEquals(reservadosEsperados, resultado.reservados()),
				() -> assertEquals(disponiveisEsperados, resultado.disponiveis()));
	}
}