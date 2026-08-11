package com.techchallenger.oficina360.usecases.validators;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.dominio.Veiculo;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.gateways.OrdemServicoGateway;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.OS_ORDEM_DE_SERVICO_ATIVA_PARA_O_VEICULO;
import static com.techchallenger.oficina360.constants.MensagensDeErroConstant.OS_VEICULO_NAO_PERTENCE_AO_CLIENTE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrdemServicoValidatorTest {

	@Mock
	private OrdemServicoGateway ordemServicoGateway;

	@InjectMocks
	private OrdemServicoValidator validator;

	@Test
	void deveValidarQuandoVeiculoPertenceAoCliente() {

		Cliente cliente = new Cliente(UUID.randomUUID(), "12345678901", "João", "joao@email.com", "11999999999");

		Veiculo veiculo = new Veiculo(UUID.randomUUID(), "ABC1234", "Ford", "Ka", "2020", "12345678901");

		assertDoesNotThrow(() -> validator.validarVeiculoPertenceAoCliente(veiculo, cliente));
	}

	@Test
	void deveLancarExcecaoQuandoVeiculoNaoPertenceAoCliente() {

		Cliente cliente = new Cliente(UUID.randomUUID(), "12345678901", "João", "joao@email.com", "11999999999");

		Veiculo veiculo = new Veiculo(UUID.randomUUID(), "ABC1234", "Ford", "Ka", "2020", "99999999999");

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
				() -> validator.validarVeiculoPertenceAoCliente(veiculo, cliente));

		assertEquals(OS_VEICULO_NAO_PERTENCE_AO_CLIENTE, exception.getMessage());
	}

	@Test
	void naoDeveLancarExcecaoQuandoNaoExisteOrdemServicoAtiva() {

		when(ordemServicoGateway.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(anyString(),
				anyCollection())).thenReturn(Optional.empty());

		assertDoesNotThrow(() -> validator.validarNaoExisteOrdemServicoAtiva("ABC1234"));

		verify(ordemServicoGateway).findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(anyString(), anyCollection());
	}

	@Test
	void deveConsultarGatewayComPlacaNormalizada() {

		when(ordemServicoGateway.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(anyString(),
				anyCollection())).thenReturn(Optional.empty());

		validator.validarNaoExisteOrdemServicoAtiva("ABC-1234");

		verify(ordemServicoGateway).findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(eq("ABC1234"), anyCollection());
	}

	@Test
	void deveLancarExcecaoQuandoExisteOrdemServicoAtiva() {

		OrdemServico ordemServico = criarOrdemServico(UUID.randomUUID(), "ABC1234",
				OrdemDeServicoStatus.EM_DIAGNOSTICO);

		when(ordemServicoGateway.findFirstByPlacaVeiculoAndOrdemDeServicoStatusIn(anyString(),
				anyCollection())).thenReturn(Optional.of(ordemServico));

		RegraDeNegocioException exception = assertThrows(RegraDeNegocioException.class,
				() -> validator.validarNaoExisteOrdemServicoAtiva("ABC1234"));

		String mensagemEsperada = String.format(OS_ORDEM_DE_SERVICO_ATIVA_PARA_O_VEICULO,
				ordemServico.getPlacaVeiculo(), ordemServico.getId(), ordemServico.getOrdemDeServicoStatus());

		assertEquals(mensagemEsperada, exception.getMessage());
	}

	private OrdemServico criarOrdemServico(UUID id, String placa, OrdemDeServicoStatus status) {

		return new OrdemServico(id, "12345678901", placa, LocalDateTime.now(), null, "Problema no veículo", status,
				null, List.of(), List.of(), null, null);
	}
}