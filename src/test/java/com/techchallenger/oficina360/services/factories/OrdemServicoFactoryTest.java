package com.techchallenger.oficina360.services.factories;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.frameworks.adapters.RelogioSistema;
import com.techchallenger.oficina360.usecases.factories.OrdemServicoFactory;
import com.techchallenger.oficina360.usecases.ordemservico.command.CriarOrdemServicoCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemServicoFactoryTest {

	private static final ZoneId ZONA_SAO_PAULO = ZoneId.of("America/Sao_Paulo");

	private static final String DOCUMENTO_CLIENTE = "12345678901";

	private static final String PLACA_VEICULO = "ABC1D23";

	private static final String DESCRICAO_PROBLEMA = "Veículo apresenta ruído ao frear";

	@InjectMocks
	private OrdemServicoFactory ordemServicoFactory;

	private static final LocalDateTime DATA_HORA_FIXA = LocalDateTime.of(2026, 8, 9, 10, 37);

	@Mock
	private RelogioSistema relogio;

	@BeforeEach
	void setUp() {
		when(relogio.agora()).thenReturn(DATA_HORA_FIXA);
	}

	@Test
	void deveCriarOrdemServicoComDadosIniciaisCorretos() {
		CriarOrdemServicoCommand command = criarOrdemServicoCommand();

		Cliente cliente = criarCliente();

		Veiculo veiculo = criarVeiculo();

		LocalDateTime instanteAnterior = relogio.agora();

		OrdemServico resultado = ordemServicoFactory.criar(command, cliente.getDocumento(), veiculo.getPlaca());

		LocalDateTime instantePosterior = relogio.agora();

		assertNotNull(resultado);

		assertAll(() -> assertNull(resultado.getId(), "Uma nova OS ainda não deve possuir ID"),
				() -> assertEquals(DOCUMENTO_CLIENTE, resultado.getDocumentoCliente()),
				() -> assertEquals(PLACA_VEICULO, resultado.getPlacaVeiculo()),
				() -> assertEquals(DESCRICAO_PROBLEMA, resultado.getDescricaoProblema()),
				() -> assertEquals(OrdemDeServicoStatus.RECEBIDA, resultado.getOrdemDeServicoStatus()),
				() -> assertNotNull(resultado.getDtHoraAbertura()),
				() -> assertFalse(resultado.getDtHoraAbertura().isBefore(instanteAnterior),
						"A abertura não deve ser anterior " + "à execução da factory"),
				() -> assertFalse(resultado.getDtHoraAbertura().isAfter(instantePosterior),
						"A abertura não deve ser posterior " + "à execução da factory"),
				() -> assertNull(resultado.getDtHoraFechamento()),
				() -> assertNull(resultado.getDtHoraInicioExecucao()),
				() -> assertNull(resultado.getDtHoraFimExecucao()), () -> assertTrue(resultado.getServicos().isEmpty()),
				() -> assertTrue(resultado.getItensEstoque().isEmpty()),
				() -> assertEquals(0, BigDecimal.ZERO.compareTo(resultado.getValorServicos())),
				() -> assertEquals(0, BigDecimal.ZERO.compareTo(resultado.getValorPecasInsumos())),
				() -> assertEquals(0, BigDecimal.ZERO.compareTo(resultado.getValorOs())));
	}

	private CriarOrdemServicoCommand criarOrdemServicoCommand() {
		return new CriarOrdemServicoCommand(DOCUMENTO_CLIENTE, PLACA_VEICULO, DESCRICAO_PROBLEMA,
				OrdemDeServicoStatus.RECEBIDA);
	}

	private Cliente criarCliente() {
		return new Cliente(UUID.randomUUID(), DOCUMENTO_CLIENTE, "João da Silva", "joao.silva@email.com",
				"11999999999");
	}

	private Veiculo criarVeiculo() {
		return new Veiculo(UUID.randomUUID(), PLACA_VEICULO, "Volkswagen", "Gol", "2020", DOCUMENTO_CLIENTE);
	}
}