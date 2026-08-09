package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteCommandMapperTest {

	private static final UUID CLIENTE_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final String DOCUMENTO = "12345678901";

	private static final String NOME = "João Cliente da Silva";

	private static final String EMAIL = "joao.cliente@oficina360.com";

	private static final String TELEFONE = "11987654321";

	@Test
	void deveConverterDominioParaCommandMantendoOrdemDosCampos() {
		Cliente cliente = new Cliente(CLIENTE_ID, DOCUMENTO, NOME, EMAIL, TELEFONE);

		ClienteCommand resultado = ClienteCommandMapper.domainToCommand(cliente);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO, resultado.documento(), "O documento deve ocupar o campo documento"),
				() -> assertEquals(NOME, resultado.nome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(EMAIL, resultado.email(), "O e-mail deve ocupar o campo email"),
				() -> assertEquals(TELEFONE, resultado.telefone(), "O telefone deve ocupar o campo telefone"));
	}

	@Test
	void deveConverterCommandParaDominioMantendoOrdemDosCampos() {
		ClienteCommand command = new ClienteCommand(DOCUMENTO, NOME, EMAIL, TELEFONE);

		Cliente resultado = ClienteCommandMapper.domainToCommand(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO, resultado.getDocumento(),
						"O documento deve ocupar o campo documento " + "do domínio"),
				() -> assertEquals(NOME, resultado.getNome(), "O nome deve ocupar o campo nome do domínio"),
				() -> assertEquals(EMAIL, resultado.getEmail(), "O e-mail deve ocupar o campo email do domínio"),
				() -> assertEquals(TELEFONE, resultado.getTelefone(),
						"O telefone deve ocupar o campo telefone " + "do domínio"));
	}

	@Test
	void deveManterDadosNaConversaoDeIdaEVolta() {
		Cliente clienteOriginal = new Cliente(CLIENTE_ID, DOCUMENTO, NOME, EMAIL, TELEFONE);

		ClienteCommand command = ClienteCommandMapper.domainToCommand(clienteOriginal);

		Cliente clienteConvertido = ClienteCommandMapper.domainToCommand(command);

		assertNotNull(command);
		assertNotNull(clienteConvertido);

		assertAll(() -> assertEquals(clienteOriginal.getDocumento(), clienteConvertido.getDocumento(),
						"O documento deve ser preservado"),
				() -> assertEquals(clienteOriginal.getNome(), clienteConvertido.getNome(),
						"O nome deve ser preservado"),
				() -> assertEquals(clienteOriginal.getEmail(), clienteConvertido.getEmail(),
						"O e-mail deve ser preservado"),
				() -> assertEquals(clienteOriginal.getTelefone(), clienteConvertido.getTelefone(),
						"O telefone deve ser preservado"));
	}

	@Test
	void deveMapearDocumentoSemConfundirComTelefone() {
		Cliente cliente = new Cliente(CLIENTE_ID, "11122233344", "Cliente Documento Distinto",
				"documento.distinto@teste.com", "11999998888");

		ClienteCommand resultado = ClienteCommandMapper.domainToCommand(cliente);

		assertAll(() -> assertEquals("11122233344", resultado.documento()),
				() -> assertEquals("11999998888", resultado.telefone()));
	}

	@Test
	void deveMapearNomeSemConfundirComEmail() {
		ClienteCommand command = new ClienteCommand("98765432100", "Nome totalmente diferente",
				"email.totalmente.diferente@teste.com", "11888887777");

		Cliente resultado = ClienteCommandMapper.domainToCommand(command);

		assertAll(() -> assertEquals("Nome totalmente diferente", resultado.getNome()),
				() -> assertEquals("email.totalmente.diferente@teste.com", resultado.getEmail()));
	}

	@Test
	void devePossuirConstrutorPrivado() throws Exception {
		Constructor<ClienteCommandMapper> constructor = ClienteCommandMapper.class.getDeclaredConstructor();

		assertTrue(Modifier.isPrivate(constructor.getModifiers()));

		constructor.setAccessible(true);

		ClienteCommandMapper instancia = constructor.newInstance();

		assertNotNull(instancia);
	}
}