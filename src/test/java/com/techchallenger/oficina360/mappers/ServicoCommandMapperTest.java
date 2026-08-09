package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Servico;
import com.techchallenger.oficina360.usecases.servicos.commands.ServicoCommand;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ServicoCommandMapperTest {

	private static final UUID SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final String CODIGO = "SRV-TROCA-OLEO";

	private static final String DESCRICAO = "Troca completa de óleo do motor";

	private static final BigDecimal VALOR = new BigDecimal("187.43");

	private static final Integer TEMPO_MEDIO_EXECUCAO = 47;

	@Test
	void deveConverterDominioParaCommandMantendoOrdemDosCampos() {
		Servico dominio = criarServico();

		dominio.inicializaTempoDeExecucao(TEMPO_MEDIO_EXECUCAO);

		ServicoCommand resultado = ServicoCommandMapper.domainToCommand(dominio);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CODIGO, resultado.codigo(), "O código deve ocupar o campo codigo"),
				() -> assertEquals(DESCRICAO, resultado.descricao(), "A descrição deve ocupar o campo descricao"),
				() -> assertEquals(0, VALOR.compareTo(resultado.valor()), "O valor deve ocupar o campo valor"),
				() -> assertEquals(TEMPO_MEDIO_EXECUCAO, resultado.tempoDeExecucaoMedio(),
						"O tempo médio deve ocupar o campo " + "tempoMedioExecucaoMinutos"));
	}

	@Test
	void deveConverterCommandParaDominioMantendoOrdemDosCampos() {
		ServicoCommand command = new ServicoCommand(CODIGO, DESCRICAO, VALOR, TEMPO_MEDIO_EXECUCAO);

		Servico resultado = ServicoCommandMapper.commandToDomain(command);

		assertNotNull(resultado);

		assertAll(() -> assertNull(resultado.getId(), "Um serviço criado pelo Command " + "ainda não deve possuir ID"),
				() -> assertEquals(CODIGO, resultado.getCodigo(),
						"O código deve ocupar o campo codigo " + "do domínio"),
				() -> assertEquals(DESCRICAO, resultado.getDescricao(),
						"A descrição deve ocupar o campo descricao " + "do domínio"),
				() -> assertEquals(0, VALOR.compareTo(resultado.getValor()),
						"O valor deve ocupar o campo valor " + "do domínio"));
	}

	@Test
	void deveManterDadosPrincipaisEmConversaoDeIdaEVolta() {
		Servico dominioOriginal = criarServico();

		dominioOriginal.inicializaTempoDeExecucao(TEMPO_MEDIO_EXECUCAO);

		ServicoCommand command = ServicoCommandMapper.domainToCommand(dominioOriginal);

		Servico dominioConvertido = ServicoCommandMapper.commandToDomain(command);

		assertNotNull(command);
		assertNotNull(dominioConvertido);

		assertAll(() -> assertNull(dominioConvertido.getId(), "O ID não faz parte do ServicoCommand"),
				() -> assertEquals(dominioOriginal.getCodigo(), dominioConvertido.getCodigo(),
						"O código deve ser preservado"),
				() -> assertEquals(dominioOriginal.getDescricao(), dominioConvertido.getDescricao(),
						"A descrição deve ser preservada"),
				() -> assertEquals(0, dominioOriginal.getValor().compareTo(dominioConvertido.getValor()),
						"O valor deve ser preservado"));
	}

	@Test
	void deveMapearCadaCampoParaPosicaoCorretaDoCommand() {
		Servico dominio = criarServico();

		dominio.inicializaTempoDeExecucao(TEMPO_MEDIO_EXECUCAO);

		ServicoCommand resultado = ServicoCommandMapper.domainToCommand(dominio);

		assertAll(() -> assertEquals("SRV-TROCA-OLEO", resultado.codigo()),
				() -> assertEquals("Troca completa de óleo do motor", resultado.descricao()),
				() -> assertEquals(new BigDecimal("187.43"), resultado.valor()),
				() -> assertEquals(47, resultado.tempoDeExecucaoMedio()));
	}

	@Test
	void devePossuirConstrutorPrivado() throws Exception {
		Constructor<ServicoCommandMapper> constructor = ServicoCommandMapper.class.getDeclaredConstructor();

		assertTrue(Modifier.isPrivate(constructor.getModifiers()));

		constructor.setAccessible(true);

		ServicoCommandMapper instancia = constructor.newInstance();

		assertNotNull(instancia);
	}

	private Servico criarServico() {
		return new Servico(SERVICO_ID, DESCRICAO, VALOR, CODIGO);
	}
}