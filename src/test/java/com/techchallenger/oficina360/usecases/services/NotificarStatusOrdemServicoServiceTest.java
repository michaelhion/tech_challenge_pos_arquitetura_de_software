package com.techchallenger.oficina360.usecases.services;

import com.techchallenger.oficina360.dominio.Cliente;
import com.techchallenger.oficina360.dominio.MensagemEmail;
import com.techchallenger.oficina360.dominio.OrdemServico;
import com.techchallenger.oficina360.enums.OrdemDeServicoStatus;
import com.techchallenger.oficina360.gateways.ClienteGateway;
import com.techchallenger.oficina360.gateways.NotificacaoEmailGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificarStatusOrdemServicoServiceTest {

	@Mock
	private ClienteGateway clienteGateway;

	@Mock
	private NotificacaoEmailGateway emailGateway;

	@InjectMocks
	private NotificarStatusOrdemServicoService service;

	@Test
	void deveEnviarEmailQuandoClientePossuiEmailValido() {

		OrdemServico os = criarOs();

		Cliente cliente = new Cliente(UUID.randomUUID(), "12345678901", "João Silva", "joao@email.com", "11999999999");

		when(clienteGateway.findByDocumento("12345678901")).thenReturn(Optional.of(cliente));

		service.notificar(os);

		ArgumentCaptor<MensagemEmail> captor = ArgumentCaptor.forClass(MensagemEmail.class);

		verify(emailGateway).enviar(captor.capture());

		MensagemEmail mensagem = captor.getValue();

		assertEquals("joao@email.com", mensagem.destinatario());

		assertTrue(mensagem.assunto().contains("Atualização da Ordem de Serviço"));

		assertTrue(mensagem.assunto().contains(os.getId().toString()));

		assertTrue(mensagem.mensagem().contains("João Silva"));

		assertTrue(mensagem.mensagem().contains(os.getPlacaVeiculo()));

		assertTrue(mensagem.mensagem().contains(os.getDescricaoProblema()));

		assertTrue(mensagem.mensagem().contains("Em diagnostico"));
	}

	@Test
	void naoDeveEnviarEmailQuandoClienteNaoPossuiEmail() {

		OrdemServico os = criarOs();

		Cliente cliente = new Cliente(UUID.randomUUID(), "12345678901", "João Silva", null, "11999999999");

		when(clienteGateway.findByDocumento("12345678901")).thenReturn(Optional.of(cliente));

		service.notificar(os);

		verifyNoInteractions(emailGateway);
	}

	@Test
	void naoDeveEnviarEmailQuandoEmailEstiverEmBranco() {

		OrdemServico os = criarOs();

		Cliente cliente = new Cliente(UUID.randomUUID(), "12345678901", "João Silva", "   ", "11999999999");

		when(clienteGateway.findByDocumento("12345678901")).thenReturn(Optional.of(cliente));

		service.notificar(os);

		verifyNoInteractions(emailGateway);
	}

	@Test
	void deveLancarExcecaoQuandoClienteNaoForEncontrado() {

		OrdemServico os = criarOs();

		when(clienteGateway.findByDocumento("12345678901")).thenReturn(Optional.empty());

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.notificar(os));

		assertEquals("Cliente da Ordem de Serviço não foi encontrado.", exception.getMessage());

		verifyNoInteractions(emailGateway);
	}

	@Test
	void deveFormatarStatusNoConteudoDoEmail() {

		OrdemServico os = criarOs();

		Cliente cliente = new Cliente(UUID.randomUUID(), "12345678901", "João Silva", "joao@email.com", "11999999999");

		when(clienteGateway.findByDocumento("12345678901")).thenReturn(Optional.of(cliente));

		service.notificar(os);

		ArgumentCaptor<MensagemEmail> captor = ArgumentCaptor.forClass(MensagemEmail.class);

		verify(emailGateway).enviar(captor.capture());

		MensagemEmail mensagem = captor.getValue();

		assertTrue(mensagem.mensagem().contains("Em diagnostico"));
	}

	private OrdemServico criarOs() {

		return new OrdemServico(UUID.randomUUID(), "12345678901", "ABC1234", LocalDateTime.now(), null,
				"Problema no motor", OrdemDeServicoStatus.EM_DIAGNOSTICO, null, List.of(), List.of(), null, null);
	}
}