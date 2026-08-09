package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstoqueCommandMapperTest {

	private static final String CODIGO = "EST-FILTRO-OLEO";

	private static final String NOME = "Filtro de óleo premium";

	private static final BigDecimal VALOR = new BigDecimal("47.83");

	private static final Integer QUANTIDADE = 23;

	private static final Integer RESERVADOS = 7;

	private static final Integer DISPONIVEIS = 16;

	@Mock
	private Estoque estoque;

	@Test
	void deveConverterDominioParaCommandMantendoOrdemDosCampos() {
		when(estoque.getCodigo()).thenReturn(CODIGO);

		when(estoque.getNome()).thenReturn(NOME);

		when(estoque.getValor()).thenReturn(VALOR);

		when(estoque.getQuantidade()).thenReturn(QUANTIDADE);

		when(estoque.getReservados()).thenReturn(RESERVADOS);

		when(estoque.getDisponiveis()).thenReturn(DISPONIVEIS);

		EstoqueCommand resultado = EstoqueCommandMapper.domaintoCommand(estoque);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CODIGO, resultado.codigo(), "O código deve ocupar o campo codigo"),
				() -> assertEquals(NOME, resultado.nome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(0, VALOR.compareTo(resultado.valor()), "O valor deve ocupar o campo valor"),
				() -> assertEquals(QUANTIDADE, resultado.quantidade(), "A quantidade deve ocupar o campo quantidade"),
				() -> assertEquals(RESERVADOS, resultado.reservados(), "A quantidade reservada deve ocupar reservados"),
				() -> assertEquals(DISPONIVEIS, resultado.disponiveis(),
						"A quantidade disponível deve ocupar disponiveis"));
	}

	@Test
	void deveConverterCommandParaDominioMantendoOrdemDosCampos() {
		EstoqueCommand command = new EstoqueCommand(CODIGO, NOME, VALOR, QUANTIDADE, RESERVADOS, DISPONIVEIS);

		Estoque resultado = EstoqueCommandMapper.commandToDomain(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CODIGO, resultado.getCodigo(), "O código deve ocupar o campo codigo do domínio"),
				() -> assertEquals(NOME, resultado.getNome(), "O nome deve ocupar o campo nome do domínio"),
				() -> assertEquals(0, VALOR.compareTo(resultado.getValor()),
						"O valor deve ocupar o campo valor do domínio"),
				() -> assertEquals(QUANTIDADE, resultado.getQuantidade(),
						"A quantidade deve ocupar o campo quantidade"));
	}

	@Test
	void deveCriarDominioComEstoqueInicialSemReservas() {
		EstoqueCommand command = new EstoqueCommand(CODIGO, NOME, VALOR, QUANTIDADE, RESERVADOS, DISPONIVEIS);

		Estoque resultado = EstoqueCommandMapper.commandToDomain(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(0, resultado.getReservados(), "Um item novo deve iniciar sem reservas"),
				() -> assertEquals(QUANTIDADE, resultado.getDisponiveis(),
						"Toda a quantidade inicial deve estar disponível"));
	}

	@Test
	void deveIgnorarReservadosEDisponiveisDoCommandAoCriarNovoDominio() {
		EstoqueCommand command = new EstoqueCommand(CODIGO, NOME, VALOR, QUANTIDADE, 19, 4);

		Estoque resultado = EstoqueCommandMapper.commandToDomain(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(QUANTIDADE, resultado.getQuantidade()),
				() -> assertEquals(0, resultado.getReservados(),
						"O mapper cria um item novo e não reaproveita " + "as reservas do Command"),
				() -> assertEquals(QUANTIDADE, resultado.getDisponiveis(),
						"A disponibilidade inicial deve ser igual " + "à quantidade total"));
	}

	@Test
	void deveManterDadosPrincipaisEmConversaoDeIdaEVolta() {
		when(estoque.getCodigo()).thenReturn(CODIGO);

		when(estoque.getNome()).thenReturn(NOME);

		when(estoque.getValor()).thenReturn(VALOR);

		when(estoque.getQuantidade()).thenReturn(QUANTIDADE);

		when(estoque.getReservados()).thenReturn(RESERVADOS);

		when(estoque.getDisponiveis()).thenReturn(DISPONIVEIS);

		EstoqueCommand command = EstoqueCommandMapper.domaintoCommand(estoque);

		Estoque dominioConvertido = EstoqueCommandMapper.commandToDomain(command);

		assertNotNull(command);
		assertNotNull(dominioConvertido);

		assertAll(() -> assertEquals(CODIGO, dominioConvertido.getCodigo(), "O código deve ser preservado"),
				() -> assertEquals(NOME, dominioConvertido.getNome(), "O nome deve ser preservado"),
				() -> assertEquals(0, VALOR.compareTo(dominioConvertido.getValor()), "O valor deve ser preservado"),
				() -> assertEquals(QUANTIDADE, dominioConvertido.getQuantidade(), "A quantidade deve ser preservada"));
	}

	@Test
	void devePossuirConstrutorPrivado() throws Exception {
		Constructor<EstoqueCommandMapper> constructor = EstoqueCommandMapper.class.getDeclaredConstructor();

		assertTrue(Modifier.isPrivate(constructor.getModifiers()));

		constructor.setAccessible(true);

		EstoqueCommandMapper instancia = constructor.newInstance();

		assertNotNull(instancia);
	}
}