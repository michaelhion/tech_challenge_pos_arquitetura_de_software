package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.MensagemEmail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoEmailGatewayImplTest {

	private static final String REMETENTE = "oficina360@email.com";

	private static final String DESTINATARIO = "cliente@email.com";

	private static final String ASSUNTO = "Atualização da Ordem de Serviço";

	private static final String CONTEUDO = "A situação da sua Ordem de Serviço foi atualizada.";

	@Mock
	private JavaMailSender javaMailSender;

	@InjectMocks
	private NotificacaoEmailGatewayImpl gateway;

	private MensagemEmail mensagem;

	@BeforeEach
	void setUp() {
		ReflectionTestUtils.setField(gateway, "remetente", REMETENTE);

		mensagem = new MensagemEmail(DESTINATARIO, ASSUNTO, CONTEUDO);
	}

	@Test
	void deveEnviarEmailComSucesso() {
		assertDoesNotThrow(() -> gateway.enviar(mensagem));

		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

		verify(javaMailSender).send(captor.capture());

		SimpleMailMessage emailEnviado = captor.getValue();

		assertNotNull(emailEnviado);

		assertAll(() -> assertEquals(REMETENTE, emailEnviado.getFrom()),
				() -> assertArrayEquals(new String[] { DESTINATARIO }, emailEnviado.getTo()),
				() -> assertEquals(ASSUNTO, emailEnviado.getSubject()),
				() -> assertEquals(CONTEUDO, emailEnviado.getText()));

		verifyNoMoreInteractions(javaMailSender);
	}

	@Test
	void deveCapturarFalhaAoEnviarEmail() {
		MailSendException excecao = new MailSendException("Falha simulada no envio do e-mail");

		doThrow(excecao).when(javaMailSender).send(any(SimpleMailMessage.class));

		assertDoesNotThrow(() -> gateway.enviar(mensagem));

		verify(javaMailSender).send(any(SimpleMailMessage.class));

		verifyNoMoreInteractions(javaMailSender);
	}

	@Test
	void deveMontarEmailAntesDeTentarEnviarMesmoQuandoOcorrerFalha() {
		MailSendException excecao = new MailSendException("Servidor SMTP indisponível");

		ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);

		doThrow(excecao).when(javaMailSender).send(captor.capture());

		assertDoesNotThrow(() -> gateway.enviar(mensagem));

		SimpleMailMessage emailEnviado = captor.getValue();

		assertNotNull(emailEnviado);

		assertAll(() -> assertEquals(REMETENTE, emailEnviado.getFrom()),
				() -> assertArrayEquals(new String[] { DESTINATARIO }, emailEnviado.getTo()),
				() -> assertEquals(ASSUNTO, emailEnviado.getSubject()),
				() -> assertEquals(CONTEUDO, emailEnviado.getText()));

		verifyNoMoreInteractions(javaMailSender);
	}
}