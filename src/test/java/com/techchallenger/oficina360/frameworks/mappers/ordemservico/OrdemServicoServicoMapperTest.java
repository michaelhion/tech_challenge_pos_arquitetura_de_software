package com.techchallenger.oficina360.frameworks.mappers.ordemservico;

import com.techchallenger.oficina360.dominio.OrdemServicoServico;
import com.techchallenger.oficina360.frameworks.persistence.entities.OrdemServicoServicoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrdemServicoServicoMapperTest {

	private static final UUID ORDEM_SERVICO_SERVICO_ID = UUID.fromString("7b5a3247-a14a-44f8-872f-016e179a92fd");

	private static final UUID SERVICO_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final UUID OUTRO_ORDEM_SERVICO_SERVICO_ID = UUID.fromString("8c6b4358-b25b-55f9-983f-127f280ba3fe");

	private static final UUID OUTRO_SERVICO_ID = UUID.fromString("3c4efe7e-3f54-5a3f-9fb4-37825c2409f9");

	private static final String DESCRICAO = "Substituição completa das pastilhas de freio";

	private static final String OUTRA_DESCRICAO = "Alinhamento e balanceamento das rodas";

	private static final BigDecimal VALOR = new BigDecimal("287.43");

	private static final BigDecimal OUTRO_VALOR = new BigDecimal("149.87");

	private OrdemServicoServicoMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new OrdemServicoServicoMapper();
	}

	@Test
	void deveConverterEntityParaDomainMantendoOrdemDosCampos() {
		OrdemServicoServicoEntity entity = criarEntity();

		OrdemServicoServico resultado = mapper.toDomain(entity);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ORDEM_SERVICO_SERVICO_ID, resultado.getId(),
						"O ID do registro deve ocupar o campo id"), () -> assertEquals(SERVICO_ID, resultado.getServicoId(),
						"O ID do serviço deve ocupar o campo servicoId"),
				() -> assertEquals(DESCRICAO, resultado.getDescricao(), "A descrição deve ocupar o campo descricao"),
				() -> assertBigDecimalEquals(VALOR, resultado.getValor()));
	}

	@Test
	void deveConverterDomainParaEntityMantendoOrdemDosCampos() {
		OrdemServicoServico domain = criarDomain();

		OrdemServicoServicoEntity resultado = mapper.toEntity(domain);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(ORDEM_SERVICO_SERVICO_ID, resultado.getId(),
						"O ID do registro deve ocupar o campo id"), () -> assertEquals(SERVICO_ID, resultado.getServicoId(),
						"O ID do serviço deve ocupar o campo servicoId"),
				() -> assertEquals(DESCRICAO, resultado.getDescricao(), "A descrição deve ocupar o campo descricao"),
				() -> assertBigDecimalEquals(VALOR, resultado.getValor()));
	}

	@Test
	void deveRetornarNullAoConverterDomainNuloParaEntity() {
		OrdemServicoServicoEntity resultado = mapper.toEntity(null);

		assertNull(resultado);
	}

	@Test
	void deveConverterListaDeEntitiesParaDomainsMantendoOrdem() {
		OrdemServicoServicoEntity primeiraEntity = criarEntity();

		OrdemServicoServicoEntity segundaEntity = criarOutraEntity();

		List<OrdemServicoServico> resultado = mapper.toDomainList(List.of(primeiraEntity, segundaEntity));

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		OrdemServicoServico primeiroResultado = resultado.get(0);

		OrdemServicoServico segundoResultado = resultado.get(1);

		assertAll(() -> assertEquals(ORDEM_SERVICO_SERVICO_ID, primeiroResultado.getId()),
				() -> assertEquals(SERVICO_ID, primeiroResultado.getServicoId()),
				() -> assertEquals(DESCRICAO, primeiroResultado.getDescricao()),
				() -> assertBigDecimalEquals(VALOR, primeiroResultado.getValor()),
				() -> assertEquals(OUTRO_ORDEM_SERVICO_SERVICO_ID, segundoResultado.getId()),
				() -> assertEquals(OUTRO_SERVICO_ID, segundoResultado.getServicoId()),
				() -> assertEquals(OUTRA_DESCRICAO, segundoResultado.getDescricao()),
				() -> assertBigDecimalEquals(OUTRO_VALOR, segundoResultado.getValor()));
	}

	@Test
	void deveRetornarListaVaziaAoConverterListaNulaDeEntities() {
		List<OrdemServicoServico> resultado = mapper.toDomainList(null);

		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}

	@Test
	void deveRetornarListaVaziaAoConverterListaVaziaDeEntities() {
		List<OrdemServicoServico> resultado = mapper.toDomainList(List.of());

		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}

	@Test
	void deveConverterListaDeDomainsParaEntitiesMantendoOrdem() {
		OrdemServicoServico primeiroDomain = criarDomain();

		OrdemServicoServico segundoDomain = criarOutroDomain();

		List<OrdemServicoServicoEntity> resultado = mapper.toEntityList(List.of(primeiroDomain, segundoDomain));

		assertNotNull(resultado);
		assertEquals(2, resultado.size());

		OrdemServicoServicoEntity primeiraEntity = resultado.get(0);

		OrdemServicoServicoEntity segundaEntity = resultado.get(1);

		assertAll(() -> assertEquals(ORDEM_SERVICO_SERVICO_ID, primeiraEntity.getId()),
				() -> assertEquals(SERVICO_ID, primeiraEntity.getServicoId()),
				() -> assertEquals(DESCRICAO, primeiraEntity.getDescricao()),
				() -> assertBigDecimalEquals(VALOR, primeiraEntity.getValor()),
				() -> assertEquals(OUTRO_ORDEM_SERVICO_SERVICO_ID, segundaEntity.getId()),
				() -> assertEquals(OUTRO_SERVICO_ID, segundaEntity.getServicoId()),
				() -> assertEquals(OUTRA_DESCRICAO, segundaEntity.getDescricao()),
				() -> assertBigDecimalEquals(OUTRO_VALOR, segundaEntity.getValor()));
	}

	@Test
	void deveRetornarListaVaziaAoConverterListaNulaDeDomains() {
		List<OrdemServicoServicoEntity> resultado = mapper.toEntityList(null);

		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}

	@Test
	void deveRetornarListaVaziaAoConverterListaVaziaDeDomains() {
		List<OrdemServicoServicoEntity> resultado = mapper.toEntityList(List.of());

		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}

	@Test
	void devePreservarDadosNaConversaoDeEntityParaDomainEVolta() {
		OrdemServicoServicoEntity entityOriginal = criarEntity();

		OrdemServicoServico domain = mapper.toDomain(entityOriginal);

		OrdemServicoServicoEntity entityConvertida = mapper.toEntity(domain);

		assertNotNull(domain);
		assertNotNull(entityConvertida);

		assertAll(() -> assertEquals(entityOriginal.getId(), entityConvertida.getId()),
				() -> assertEquals(entityOriginal.getServicoId(), entityConvertida.getServicoId()),
				() -> assertEquals(entityOriginal.getDescricao(), entityConvertida.getDescricao()),
				() -> assertBigDecimalEquals(entityOriginal.getValor(), entityConvertida.getValor()));
	}

	@Test
	void deveDiferenciarIdDoRegistroEIdDoServico() {
		OrdemServicoServicoEntity entity = criarEntity();

		OrdemServicoServico resultado = mapper.toDomain(entity);

		assertAll(() -> assertEquals(ORDEM_SERVICO_SERVICO_ID, resultado.getId(),
						"O ID do registro não pode receber o ID do serviço"),
				() -> assertEquals(SERVICO_ID, resultado.getServicoId(),
						"O ID do serviço não pode receber o ID do registro"));
	}

	private OrdemServicoServicoEntity criarEntity() {
		return OrdemServicoServicoEntity.builder().id(ORDEM_SERVICO_SERVICO_ID).servicoId(SERVICO_ID)
				.descricao(DESCRICAO).valor(VALOR).build();
	}

	private OrdemServicoServicoEntity criarOutraEntity() {
		return OrdemServicoServicoEntity.builder().id(OUTRO_ORDEM_SERVICO_SERVICO_ID).servicoId(OUTRO_SERVICO_ID)
				.descricao(OUTRA_DESCRICAO).valor(OUTRO_VALOR).build();
	}

	private OrdemServicoServico criarDomain() {
		return new OrdemServicoServico(ORDEM_SERVICO_SERVICO_ID, SERVICO_ID, DESCRICAO, VALOR);
	}

	private OrdemServicoServico criarOutroDomain() {
		return new OrdemServicoServico(OUTRO_ORDEM_SERVICO_SERVICO_ID, OUTRO_SERVICO_ID, OUTRA_DESCRICAO, OUTRO_VALOR);
	}

	private void assertBigDecimalEquals(BigDecimal esperado, BigDecimal atual) {
		assertNotNull(atual);

		assertEquals(0, esperado.compareTo(atual), "Os valores financeiros devem ser numericamente iguais");
	}
}