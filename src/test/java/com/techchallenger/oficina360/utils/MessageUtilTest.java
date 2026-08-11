package com.techchallenger.oficina360.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageUtilTest {

	@Mock
	private MessageSource messageSource;

	private MessageUtil messageUtil;

	@BeforeEach
	void setup() {
		messageUtil = new MessageUtil(messageSource);
		LocaleContextHolder.setLocale(Locale.forLanguageTag("pt-BR"));
	}

	@Test
	void deveObterMensagemPorChave() {

		when(messageSource.getMessage(eq("mensagem.teste"), isNull(), eq(LocaleContextHolder.getLocale()))).thenReturn(
				"Mensagem Teste");

		String resultado = messageUtil.getMessage("mensagem.teste");

		assertEquals("Mensagem Teste", resultado);

		verify(messageSource).getMessage("mensagem.teste", null, LocaleContextHolder.getLocale());
	}

	@Test
	void deveObterMensagemComArgumentos() {

		Object[] args = { "João" };

		when(messageSource.getMessage(eq("usuario.bemvindo"), eq(args),
				eq(LocaleContextHolder.getLocale()))).thenReturn("Bem-vindo João");

		String resultado = messageUtil.getMessage("usuario.bemvindo", args);

		assertEquals("Bem-vindo João", resultado);

		verify(messageSource).getMessage("usuario.bemvindo", args, LocaleContextHolder.getLocale());
	}

	@Test
	void deveObterMensagemComValorPadrao() {

		Object[] args = { "João" };

		when(messageSource.getMessage(eq("usuario.nao.encontrado"), eq(args), eq("Usuário não encontrado"),
				eq(LocaleContextHolder.getLocale()))).thenReturn("Usuário não encontrado");

		String resultado = messageUtil.getMessageWithDefault("usuario.nao.encontrado", "Usuário não encontrado", args);

		assertEquals("Usuário não encontrado", resultado);

		verify(messageSource).getMessage("usuario.nao.encontrado", args, "Usuário não encontrado",
				LocaleContextHolder.getLocale());
	}
}