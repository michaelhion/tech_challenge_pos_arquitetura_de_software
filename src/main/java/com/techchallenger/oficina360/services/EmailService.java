package com.techchallenger.oficina360.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmail(
            String destinatario,
            String assunto,
            String mensagem
    ) {

        SimpleMailMessage email =
                new SimpleMailMessage();

        email.setTo(destinatario);
        email.setSubject(assunto);
        email.setText(mensagem);

        mailSender.send(email);
    }

    public void enviarOrcamentoParaAprovacao(
            String emailCliente,
            String numeroOs
    ) {
        log.info("TENTANDO ENVIAR EMAIL");
        String assunto =
                "Oficina360 - Orçamento disponível";

        String mensagem = """
            Olá,

            O diagnóstico da sua ordem de serviço foi concluído.

            Ordem de serviço: %s

            Seu orçamento está disponível para aprovação.

            Acesse o sistema para visualizar os detalhes.

            Equipe Oficina360.
            """.formatted(numeroOs);

        enviarEmail(
                emailCliente,
                assunto,
                mensagem
        );
    }
}