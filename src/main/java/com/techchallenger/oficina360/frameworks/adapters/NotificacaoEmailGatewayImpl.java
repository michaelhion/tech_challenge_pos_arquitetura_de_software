package com.techchallenger.oficina360.frameworks.adapters;

import com.techchallenger.oficina360.dominio.MensagemEmail;
import com.techchallenger.oficina360.gateways.NotificacaoEmailGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificacaoEmailGatewayImpl implements NotificacaoEmailGateway {

	private final JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String remetente;

	@Override
	public void enviar(MensagemEmail mensagem) {
		SimpleMailMessage email = new SimpleMailMessage();

		email.setFrom(remetente);
		email.setTo(mensagem.destinatario());
		email.setSubject(mensagem.assunto());
		email.setText(mensagem.mensagem());
		try{
			log.info("enviando email para " + remetente);
			javaMailSender.send(email);
		} catch (MailException e) {
			log.info(e.getMessage());
			log.error("falha ao enviar email");
		}
	}
}