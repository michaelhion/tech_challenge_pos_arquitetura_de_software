package com.techchallenger.oficina360.frameworks.mappers.estoque;

import com.techchallenger.oficina360.dominio.Estoque;
import com.techchallenger.oficina360.dtos.estoques.EstoqueDTO;
import com.techchallenger.oficina360.dtos.ordemservico.diagnostico.ReservaEstoqueDTO;
import com.techchallenger.oficina360.frameworks.persistence.entities.EstoqueEntity;
import com.techchallenger.oficina360.usecases.ordemservico.command.EstoqueCommand;
import com.techchallenger.oficina360.usecases.ordemservico.command.ReservaEstoqueCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstoqueDTOMapperTest {

	private static final UUID ESTOQUE_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String CODIGO = "EST-FILTRO-OLEO";

	private static final String NOME = "Filtro de óleo premium";

	private static final BigDecimal VALOR = new BigDecimal("47.83");

	private static final Integer QUANTIDADE = 23;

	private static final Integer RESERVADOS = 7;

	private static final Integer DISPONIVEIS = 16;

	@Mock
	private EstoqueEntity estoqueEntity;

	@Mock
	private Estoque estoque;

	private EstoqueDTOMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new EstoqueDTOMapper();
	}

	@Test
	void deveConverterEntityParaDomainMantendoOrdemDosCampos() {
		when(estoqueEntity.getId()).thenReturn(ESTOQUE_ID);

		when(estoqueEntity.getCodigo()).thenReturn(CODIGO);

		when(estoqueEntity.getNome()).thenReturn(NOME);

		when(estoqueEntity.getValor()).thenReturn(VALOR);

		when(estoqueEntity.getQuantidade()).thenReturn(QUANTIDADE);

		when(estoqueEntity.getReservados()).thenReturn(RESERVADOS);

		Estoque resultado = mapper.toDomain(estoqueEntity);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ESTOQUE_ID, resultado.getId(), "O ID deve ocupar o campo id"),
				() -> assertEquals(CODIGO, resultado.getCodigo(), "O código deve ocupar o campo codigo"),
				() -> assertEquals(NOME, resultado.getNome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(0, VALOR.compareTo(resultado.getValor()), "O valor deve ocupar o campo valor"),
				() -> assertEquals(QUANTIDADE, resultado.getQuantidade(),
						"A quantidade deve ocupar o campo quantidade"),
				() -> assertEquals(RESERVADOS, resultado.getReservados(),
						"A quantidade reservada deve ocupar reservados"),
				() -> assertEquals(DISPONIVEIS, resultado.getDisponiveis(),
						"Os disponíveis devem ser calculados corretamente"));
	}

	@Test
	void deveRetornarNullAoConverterEntityNulaParaDomain() {
		Estoque resultado = mapper.toDomain(null);

		assertNull(resultado);
	}

	@Test
	void deveConverterDomainParaEntityMantendoOrdemDosCampos() {
		configurarEstoqueDomain();

		EstoqueEntity resultado = mapper.toEntity(estoque);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ESTOQUE_ID, resultado.getId(), "O ID deve ocupar o campo id da entidade"),
				() -> assertEquals(CODIGO, resultado.getCodigo(), "O código deve ocupar o campo codigo"),
				() -> assertEquals(NOME, resultado.getNome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(0, VALOR.compareTo(resultado.getValor()), "O valor deve ocupar o campo valor"),
				() -> assertEquals(QUANTIDADE, resultado.getQuantidade(),
						"A quantidade deve ocupar o campo quantidade"),
				() -> assertEquals(RESERVADOS, resultado.getReservados(),
						"A quantidade reservada deve ocupar reservados"));
	}

	@Test
	void deveRetornarNullAoConverterDomainNuloParaEntity() {
		EstoqueEntity resultado = mapper.toEntity(null);

		assertNull(resultado);
	}

	@Test
	void deveAtualizarEntityComDadosDoDomain() {
		configurarEstoqueDomain();

		EstoqueEntity entityExistente = EstoqueEntity.builder().id(ESTOQUE_ID).codigo("EST-CODIGO-ANTIGO")
				.nome("Nome antigo").valor(new BigDecimal("10.00")).quantidade(100).reservados(50).build();

		mapper.updateEntityFromDomain(estoque, entityExistente);

		assertAll(() -> assertEquals(ESTOQUE_ID, entityExistente.getId(), "A atualização não deve alterar o ID"),
				() -> assertEquals(CODIGO, entityExistente.getCodigo(), "O código deve ser atualizado corretamente"),
				() -> assertEquals(NOME, entityExistente.getNome(), "O nome deve ser atualizado corretamente"),
				() -> assertEquals(0, VALOR.compareTo(entityExistente.getValor()),
						"O valor deve ser atualizado corretamente"),
				() -> assertEquals(QUANTIDADE, entityExistente.getQuantidade(),
						"A quantidade deve ser atualizada corretamente"),
				() -> assertEquals(RESERVADOS, entityExistente.getReservados(),
						"Os reservados devem ser atualizados corretamente"));
	}

	@Test
	void deveConverterEntityParaDTOMantendoOrdemDosCampos() {
		when(estoqueEntity.getCodigo()).thenReturn(CODIGO);

		when(estoqueEntity.getNome()).thenReturn(NOME);

		when(estoqueEntity.getValor()).thenReturn(VALOR);

		when(estoqueEntity.getQuantidade()).thenReturn(QUANTIDADE);

		when(estoqueEntity.getReservados()).thenReturn(RESERVADOS);

		when(estoqueEntity.getDisponiveis()).thenReturn(DISPONIVEIS);

		EstoqueDTO resultado = EstoqueDTOMapper.toDTO(estoqueEntity);

		assertNotNull(resultado);

		assertEstoqueDTO(resultado);
	}

	@Test
	void deveConverterCommandParaDTOMantendoOrdemDosCampos() {
		EstoqueCommand command = criarEstoqueCommand();

		EstoqueDTO resultado = EstoqueDTOMapper.commandToDTO(command);

		assertNotNull(resultado);

		assertEstoqueDTO(resultado);
	}

	@Test
	void deveConverterDTOParaCommandMantendoOrdemDosCampos() {
		EstoqueDTO dto = criarEstoqueDTO();

		EstoqueCommand resultado = EstoqueDTOMapper.dtoToCommand(dto);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(CODIGO, resultado.codigo(), "O código deve ocupar o campo codigo"),
				() -> assertEquals(NOME, resultado.nome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(0, VALOR.compareTo(resultado.valor()), "O valor deve ocupar o campo valor"),
				() -> assertEquals(QUANTIDADE, resultado.quantidade(), "A quantidade deve ocupar o campo quantidade"),
				() -> assertEquals(RESERVADOS, resultado.reservados(), "Os reservados devem ocupar o campo reservados"),
				() -> assertEquals(DISPONIVEIS, resultado.disponiveis(),
						"Os disponíveis devem ocupar o campo disponiveis"));
	}

	@Test
	void devePreservarCamposNaConversaoDeDTOParaCommandEVolta() {
		EstoqueDTO dtoOriginal = criarEstoqueDTO();

		EstoqueCommand command = EstoqueDTOMapper.dtoToCommand(dtoOriginal);

		EstoqueDTO dtoConvertido = EstoqueDTOMapper.commandToDTO(command);

		assertAll(() -> assertEquals(dtoOriginal.codigo(), dtoConvertido.codigo()),
				() -> assertEquals(dtoOriginal.nome(), dtoConvertido.nome()),
				() -> assertEquals(0, dtoOriginal.valor().compareTo(dtoConvertido.valor())),
				() -> assertEquals(dtoOriginal.quantidade(), dtoConvertido.quantidade()),
				() -> assertEquals(dtoOriginal.reservados(), dtoConvertido.reservados()),
				() -> assertEquals(dtoOriginal.disponiveis(), dtoConvertido.disponiveis()));
	}

	@Test
	void deveConverterReservaDTOParaCommand() {
		Integer quantidadeReserva = 9;

		ReservaEstoqueDTO dto = new ReservaEstoqueDTO(quantidadeReserva);

		ReservaEstoqueCommand resultado = EstoqueDTOMapper.reservarDTOToCommand(dto);

		assertNotNull(resultado);

		assertEquals(quantidadeReserva, resultado.quantidade(),
				"A quantidade da reserva deve ocupar " + "o campo quantidade do Command");
	}

	@Test
	void deveMapearCodigoENomeParaCamposDiferentes() {
		EstoqueDTO dto = new EstoqueDTO("EST-CODIGO-DISTINTO", "Nome completamente diferente", new BigDecimal("83.27"),
				31, 11, 20);

		EstoqueCommand resultado = EstoqueDTOMapper.dtoToCommand(dto);

		assertAll(() -> assertEquals("EST-CODIGO-DISTINTO", resultado.codigo(),
						"O código não pode ser confundido com o nome"),
				() -> assertEquals("Nome completamente diferente", resultado.nome(),
						"O nome não pode ser confundido com o código"));
	}

	@Test
	void deveMapearQuantidadeReservadosEDisponiveisParaCamposCorretos() {
		EstoqueDTO dto = new EstoqueDTO(CODIGO, NOME, VALOR, 91, 17, 74);

		EstoqueCommand resultado = EstoqueDTOMapper.dtoToCommand(dto);

		assertAll(() -> assertEquals(91, resultado.quantidade(), "A quantidade total deve ocupar quantidade"),
				() -> assertEquals(17, resultado.reservados(), "Os reservados devem ocupar reservados"),
				() -> assertEquals(74, resultado.disponiveis(), "Os disponíveis devem ocupar disponiveis"));
	}

	private void configurarEstoqueDomain() {
		lenient().when(estoque.getId()).thenReturn(ESTOQUE_ID);

		when(estoque.getCodigo()).thenReturn(CODIGO);

		when(estoque.getNome()).thenReturn(NOME);

		when(estoque.getValor()).thenReturn(VALOR);

		when(estoque.getQuantidade()).thenReturn(QUANTIDADE);

		when(estoque.getReservados()).thenReturn(RESERVADOS);
	}

	private EstoqueDTO criarEstoqueDTO() {
		return new EstoqueDTO(CODIGO, NOME, VALOR, QUANTIDADE, RESERVADOS, DISPONIVEIS);
	}

	private EstoqueCommand criarEstoqueCommand() {
		return new EstoqueCommand(CODIGO, NOME, VALOR, QUANTIDADE, RESERVADOS, DISPONIVEIS);
	}

	private void assertEstoqueDTO(EstoqueDTO resultado) {
		assertAll(() -> assertEquals(CODIGO, resultado.codigo(), "O código deve ocupar o campo codigo"),
				() -> assertEquals(NOME, resultado.nome(), "O nome deve ocupar o campo nome"),
				() -> assertEquals(0, VALOR.compareTo(resultado.valor()), "O valor deve ocupar o campo valor"),
				() -> assertEquals(QUANTIDADE, resultado.quantidade(), "A quantidade deve ocupar o campo quantidade"),
				() -> assertEquals(RESERVADOS, resultado.reservados(), "Os reservados devem ocupar o campo reservados"),
				() -> assertEquals(DISPONIVEIS, resultado.disponiveis(),
						"Os disponíveis devem ocupar o campo disponiveis"));
	}
}