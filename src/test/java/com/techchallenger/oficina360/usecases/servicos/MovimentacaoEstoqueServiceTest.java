package com.techchallenger.oficina360.usecases.servicos;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dominio.OrdemServicoItemEstoque;
import com.techchallenger.oficina360.dominio.shared.exception.ItemEstoqueInvalidoException;
import com.techchallenger.oficina360.gateways.EstoqueGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimentacaoEstoqueServiceTest {

	@Mock
	private EstoqueGateway estoqueGateway;

	@InjectMocks
	private MovimentacaoEstoqueService service;

	@Test
	void naoDeveFazerNadaQuandoListaForNula() {

		service.liberarReservas(null);

		verifyNoInteractions(estoqueGateway);
	}

	@Test
	void naoDeveFazerNadaQuandoListaForVazia() {

		service.liberarReservas(List.of());

		verifyNoInteractions(estoqueGateway);
	}

	@Test
	void deveLiberarReservaComSucesso() {

		UUID estoqueId = UUID.randomUUID();

		OrdemServicoItemEstoque item = mock(OrdemServicoItemEstoque.class);

		when(item.getEstoqueId()).thenReturn(estoqueId);
		when(item.getQuantidade()).thenReturn(2);

		Estoque estoque = new Estoque(estoqueId, "Óleo", BigDecimal.TEN, 10, 5, "EST001");

		when(estoqueGateway.findByIdIn(List.of(estoqueId))).thenReturn(List.of(estoque));

		service.liberarReservas(List.of(item));

		assertEquals(3, estoque.getReservados());

		verify(estoqueGateway).saveAll(anyList());
	}

	@Test
	void deveConsumirReservaComSucesso() {

		UUID estoqueId = UUID.randomUUID();

		OrdemServicoItemEstoque item = mock(OrdemServicoItemEstoque.class);

		when(item.getEstoqueId()).thenReturn(estoqueId);
		when(item.getQuantidade()).thenReturn(2);

		Estoque estoque = new Estoque(estoqueId, "Filtro", BigDecimal.TEN, 10, 5, "EST001");

		when(estoqueGateway.findByIdIn(List.of(estoqueId))).thenReturn(List.of(estoque));

		service.consumirReservas(List.of(item));

		assertEquals(3, estoque.getReservados());
		assertEquals(8, estoque.getQuantidade());

		verify(estoqueGateway).saveAll(anyList());
	}

	@Test
	void deveSomarQuantidadesDoMesmoEstoque() {

		UUID estoqueId = UUID.randomUUID();

		OrdemServicoItemEstoque item1 = mock(OrdemServicoItemEstoque.class);
		OrdemServicoItemEstoque item2 = mock(OrdemServicoItemEstoque.class);

		when(item1.getEstoqueId()).thenReturn(estoqueId);
		when(item1.getQuantidade()).thenReturn(2);

		when(item2.getEstoqueId()).thenReturn(estoqueId);
		when(item2.getQuantidade()).thenReturn(3);

		Estoque estoque = new Estoque(estoqueId, "Filtro", BigDecimal.TEN, 20, 10, "EST001");

		when(estoqueGateway.findByIdIn(anyList())).thenReturn(List.of(estoque));

		service.liberarReservas(List.of(item1, item2));

		assertEquals(5, estoque.getReservados());

		verify(estoqueGateway).saveAll(anyList());
	}

	@Test
	void deveLancarExcecaoQuandoEstoqueNaoForEncontrado() {

		UUID estoqueId = UUID.randomUUID();

		OrdemServicoItemEstoque item = mock(OrdemServicoItemEstoque.class);

		when(item.getEstoqueId()).thenReturn(estoqueId);
		when(item.getQuantidade()).thenReturn(1);

		when(estoqueGateway.findByIdIn(anyList())).thenReturn(List.of());

		ItemEstoqueInvalidoException exception = assertThrows(ItemEstoqueInvalidoException.class,
				() -> service.liberarReservas(List.of(item)));

		assertTrue(exception.getMessage().contains("Itens de estoque não encontrados"));

		verify(estoqueGateway, never()).saveAll(anyList());
	}

	@Test
	void deveSalvarEstoquesAposLiberarReservas() {

		UUID estoqueId = UUID.randomUUID();

		OrdemServicoItemEstoque item = mock(OrdemServicoItemEstoque.class);

		when(item.getEstoqueId()).thenReturn(estoqueId);
		when(item.getQuantidade()).thenReturn(1);

		Estoque estoque = new Estoque(estoqueId, "Peça", BigDecimal.TEN, 10, 3, "EST001");

		when(estoqueGateway.findByIdIn(anyList())).thenReturn(List.of(estoque));

		service.liberarReservas(List.of(item));

		ArgumentCaptor<List<Estoque>> captor = ArgumentCaptor.forClass(List.class);

		verify(estoqueGateway).saveAll(captor.capture());

		assertEquals(1, captor.getValue().size());
		assertEquals(estoqueId, captor.getValue().get(0).getId());
	}
}