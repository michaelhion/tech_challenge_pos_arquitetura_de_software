package com.techchallenger.oficina360.mappers;

import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.usecases.veiculo.commands.VeiculoCommand;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoCommandMapperTest {

	private static final UUID VEICULO_ID = UUID.fromString("2b3ded6d-2e43-4f2f-8ea3-26714b1398f8");

	private static final String PLACA = "ABC1D23";

	private static final String MARCA = "Volkswagen";

	private static final String MODELO = "Gol Comfortline";

	private static final String ANO = "2022";

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	@Test
	void deveConverterDominioParaCommandMantendoOrdemDosCampos() {
		Veiculo dominio = new Veiculo(VEICULO_ID, PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		VeiculoCommand resultado = VeiculoCommandMapper.domainToCommand(dominio);

		assertNotNull(resultado);

		assertAll(() -> assertEquals(PLACA, resultado.placa(), "A placa deve ser mapeada para o campo placa"),
				() -> assertEquals(MARCA, resultado.marca(), "A marca deve ser mapeada para o campo marca"),
				() -> assertEquals(MODELO, resultado.modelo(), "O modelo deve ser mapeado para o campo modelo"),
				() -> assertEquals(ANO, resultado.ano(), "O ano deve ser mapeado para o campo ano"),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.clienteDocumento(),
						"O documento deve ser mapeado para clienteDocumento"));
	}

	@Test
	void deveConverterCommandParaDominioMantendoOrdemDosCampos() {
		VeiculoCommand command = new VeiculoCommand(PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		Veiculo resultado = VeiculoCommandMapper.commandToDomain(command);

		assertNotNull(resultado);

		assertAll(() -> assertNull(resultado.getId(),
						"Um veículo criado a partir do Command " + "ainda não deve possuir ID"),
				() -> assertEquals(PLACA, resultado.getPlaca(), "A placa deve ocupar o campo placa do domínio"),
				() -> assertEquals(MARCA, resultado.getMarca(), "A marca deve ocupar o campo marca do domínio"),
				() -> assertEquals(MODELO, resultado.getModelo(), "O modelo deve ocupar o campo modelo do domínio"),
				() -> assertEquals(ANO, resultado.getAno(), "O ano deve ocupar o campo ano do domínio"),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.getClienteDocumento(),
						"O documento deve ocupar o campo " + "clienteDocumento do domínio"));
	}

	@Test
	void deveManterDadosEmConversaoDeIdaEVolta() {
		Veiculo dominioOriginal = new Veiculo(VEICULO_ID, PLACA, MARCA, MODELO, ANO, DOCUMENTO_CLIENTE);

		VeiculoCommand command = VeiculoCommandMapper.domainToCommand(dominioOriginal);

		Veiculo dominioConvertido = VeiculoCommandMapper.commandToDomain(command);

		assertNotNull(command);
		assertNotNull(dominioConvertido);

		assertAll(() -> assertNull(dominioConvertido.getId(), "O ID não faz parte do VeiculoCommand"),
				() -> assertEquals(dominioOriginal.getPlaca(), dominioConvertido.getPlaca()),
				() -> assertEquals(dominioOriginal.getMarca(), dominioConvertido.getMarca()),
				() -> assertEquals(dominioOriginal.getModelo(), dominioConvertido.getModelo()),
				() -> assertEquals(dominioOriginal.getAno(), dominioConvertido.getAno()),
				() -> assertEquals(dominioOriginal.getClienteDocumento(), dominioConvertido.getClienteDocumento()));
	}

	@Test
	void devePossuirConstrutorPrivado() throws Exception {
		Constructor<VeiculoCommandMapper> constructor = VeiculoCommandMapper.class.getDeclaredConstructor();

		assertTrue(Modifier.isPrivate(constructor.getModifiers()));

		constructor.setAccessible(true);

		VeiculoCommandMapper instancia = constructor.newInstance();

		assertNotNull(instancia);
	}
}