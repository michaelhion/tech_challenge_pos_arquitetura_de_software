package com.techchallenger.oficina360.frameworks.mappers.cliente;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dtos.clientes.ClienteDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.ClienteEntity;
import com.techchallenger.oficina360.usecases.ordemservico.command.ClienteCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDTOMapperTest {

	private static final UUID CLIENTE_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final String DOCUMENTO = "12345678901";

	private static final String NOME = "João Cliente da Silva";

	private static final String EMAIL = "joao.cliente@oficina360.com";

	private static final String TELEFONE = "11987654321";

	private ClienteDTOMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ClienteDTOMapper();
	}

	@Test
	void deveConverterEntityParaDominioMantendoOrdemDosCampos() {
		ClienteEntity entity = criarClienteEntity();

		Cliente resultado = mapper.toDomain(entity);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CLIENTE_ID, resultado.getId(), "O ID deve ocupar o campo id"),
				() -> assertEquals(DOCUMENTO, resultado.getDocumento(), "O documento deve ocupar o campo documento"),
				() -> assertEquals(NOME, resultado.getNome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(EMAIL, resultado.getEmail(), "O e-mail deve ocupar o campo email"),
				() -> assertEquals(TELEFONE, resultado.getTelefone(), "O telefone deve ocupar o campo telefone"));
	}

	@Test
	void deveRetornarNullAoConverterEntityNulaParaDominio() {
		Cliente resultado = mapper.toDomain(null);

		assertNull(resultado);
	}

	@Test
	void deveConverterDominioParaEntityMantendoOrdemDosCampos() {
		Cliente cliente = criarCliente();

		ClienteEntity resultado = mapper.toEntity(cliente);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CLIENTE_ID, resultado.getId(), "O ID deve ocupar o campo id da entidade"),
				() -> assertEquals(DOCUMENTO, resultado.getDocumento(), "O documento deve ocupar o campo documento"),
				() -> assertEquals(NOME, resultado.getNome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(EMAIL, resultado.getEmail(), "O e-mail deve ocupar o campo email"),
				() -> assertEquals(TELEFONE, resultado.getTelefone(), "O telefone deve ocupar o campo telefone"));
	}

	@Test
	void deveRetornarNullAoConverterDominioNuloParaEntity() {
		ClienteEntity resultado = mapper.toEntity((Cliente) null);

		assertNull(resultado);
	}

	@Test
	void deveConverterEntityParaDTOMantendoOrdemDosCampos() {
		ClienteEntity entity = criarClienteEntity();

		ClienteDTO resultado = ClienteDTOMapper.toDTO(entity);

		assertNotNull(resultado);

		assertAll(() -> assertEquals("***8901", resultado.documento(),
						"O documento deve ser mascarado e ocupar o campo documento"),
				() -> assertEquals(NOME, resultado.nome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(EMAIL, resultado.email(), "O e-mail deve ocupar o campo email"),
				() -> assertEquals(TELEFONE, resultado.telefone(), "O telefone deve ocupar o campo telefone"));
	}

	@Test
	void deveConverterDTOParaEntityMantendoOrdemDosCampos() {
		ClienteDTO dto = criarClienteDTO();

		ClienteEntity resultado = ClienteDTOMapper.toEntity(dto);

		assertNotNull(resultado);

		assertAll(() -> assertNull(resultado.getId(),
						"Uma entidade criada a partir do DTO " + "não deve receber ID automaticamente"),
				() -> assertEquals(DOCUMENTO, resultado.getDocumento(), "O documento deve ocupar o campo documento"),
				() -> assertEquals(NOME, resultado.getNome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(EMAIL, resultado.getEmail(), "O e-mail deve ocupar o campo email"),
				() -> assertEquals(TELEFONE, resultado.getTelefone(), "O telefone deve ocupar o campo telefone"));
	}

	@Test
	void deveAtualizarEntityComDadosDoDTO() {
		ClienteEntity entity = ClienteEntity.builder().id(CLIENTE_ID).documento("00000000000").nome("Nome anterior")
				.email("email.anterior@teste.com").telefone("11000000000").build();

		ClienteDTO dto = criarClienteDTO();

		ClienteDTOMapper.updateEntityFromDto(dto, entity);

		assertAll(() -> assertEquals(CLIENTE_ID, entity.getId(), "A atualização não deve alterar o ID"),
				() -> assertEquals(DOCUMENTO, entity.getDocumento(), "O documento deve ser atualizado"),
				() -> assertEquals(NOME, entity.getNome(), "O nome deve ser atualizado"),
				() -> assertEquals(EMAIL, entity.getEmail(), "O e-mail deve ser atualizado"),
				() -> assertEquals(TELEFONE, entity.getTelefone(), "O telefone deve ser atualizado"));
	}

	@Test
	void naoDeveAlterarEntityQuandoDTOForNulo() {
		ClienteEntity entity = criarClienteEntity();

		ClienteDTOMapper.updateEntityFromDto(null, entity);

		assertAll(() -> assertEquals(CLIENTE_ID, entity.getId()), () -> assertEquals(DOCUMENTO, entity.getDocumento()),
				() -> assertEquals(NOME, entity.getNome()), () -> assertEquals(EMAIL, entity.getEmail()),
				() -> assertEquals(TELEFONE, entity.getTelefone()));
	}

	@Test
	void naoDeveLancarErroQuandoEntityForNulaNaAtualizacao() {
		ClienteDTO dto = criarClienteDTO();


		assertDoesNotThrow(()->ClienteDTOMapper.updateEntityFromDto(dto, null));
	}

	@Test
	void deveConverterCommandParaDTOComCamposNaOrdemCorreta() {
		ClienteCommand command = criarClienteCommand();

		ClienteDTO resultado = ClienteDTOMapper.commandToDTO(command);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO, resultado.documento(),
						"O documento não pode ser confundido " + "com o telefone"),
				() -> assertEquals(NOME, resultado.nome(), "O nome não pode ser confundido " + "com o e-mail"),
				() -> assertEquals(EMAIL, resultado.email(), "O e-mail deve ocupar o campo email"),
				() -> assertEquals(TELEFONE, resultado.telefone(), "O telefone deve ocupar o campo telefone"));
	}

	@Test
	void deveConverterDTOParaCommandComCamposNaOrdemCorreta() {
		ClienteDTO dto = criarClienteDTO();

		ClienteCommand resultado = ClienteDTOMapper.dtoToCommand(dto);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(DOCUMENTO, resultado.documento(), "O documento deve ocupar o campo documento"),
				() -> assertEquals(NOME, resultado.nome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(EMAIL, resultado.email(), "O e-mail deve ocupar o campo email"),
				() -> assertEquals(TELEFONE, resultado.telefone(), "O telefone deve ocupar o campo telefone"));
	}

	@Test
	void devePreservarDadosNaConversaoDeDTOParaCommandEVolta() {
		ClienteDTO dtoOriginal = criarClienteDTO();

		ClienteCommand command = ClienteDTOMapper.dtoToCommand(dtoOriginal);

		ClienteDTO dtoConvertido = ClienteDTOMapper.commandToDTO(command);

		assertAll(() -> assertEquals(dtoOriginal.documento(), dtoConvertido.documento()),
				() -> assertEquals(dtoOriginal.nome(), dtoConvertido.nome()),
				() -> assertEquals(dtoOriginal.email(), dtoConvertido.email()),
				() -> assertEquals(dtoOriginal.telefone(), dtoConvertido.telefone()));
	}

	private Cliente criarCliente() {
		return new Cliente(CLIENTE_ID, DOCUMENTO, NOME, EMAIL, TELEFONE);
	}

	private ClienteEntity criarClienteEntity() {
		return ClienteEntity.builder().id(CLIENTE_ID).documento(DOCUMENTO).nome(NOME).email(EMAIL).telefone(TELEFONE)
				.build();
	}

	private ClienteDTO criarClienteDTO() {
		return new ClienteDTO(DOCUMENTO, NOME, EMAIL, TELEFONE);
	}

	private ClienteCommand criarClienteCommand() {
		return new ClienteCommand(DOCUMENTO, NOME, EMAIL, TELEFONE);
	}
}