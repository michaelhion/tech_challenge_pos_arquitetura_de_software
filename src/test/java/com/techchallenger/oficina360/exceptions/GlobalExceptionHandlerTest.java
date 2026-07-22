package com.techchallenger.oficina360.exceptions;

import com.techchallenger.oficina360.frameworks.web.exceptions.ConflitoException;
import com.techchallenger.oficina360.frameworks.web.exceptions.ErroRegraDeNegocioResponse;
import com.techchallenger.oficina360.frameworks.web.exceptions.ErroResponse;
import com.techchallenger.oficina360.frameworks.web.exceptions.GlobalExceptionHandler;
import com.techchallenger.oficina360.usecases.shared.exception.RecursoNaoEncontradoException;
import com.techchallenger.oficina360.usecases.shared.exception.RegraDeNegocioException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

	@InjectMocks
	private GlobalExceptionHandler globalExceptionHandler;

	@Test
	void deveRetornar404QuandoRecursoNaoEncontrado() {

		RecursoNaoEncontradoException ex = new RecursoNaoEncontradoException("Cliente não encontrado");

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleRecursoNaoEncontrado(ex);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

		assertEquals("Cliente não encontrado", response.getBody().mensagem());
	}

	@Test
	void deveRetornar409QuandoConflito() {

		ConflitoException ex = new ConflitoException("Email já cadastrado");

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleConflito(ex);

		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
	}

	@Test
	void deveRetornar422QuandoRegraDeNegocio() {

		RegraDeNegocioException ex = new RegraDeNegocioException("Regra inválida");

		ResponseEntity<ErroRegraDeNegocioResponse> response = globalExceptionHandler.handleRegraDeNegocio(ex);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
	}

	@Test
	void deveRetornar403QuandoAcessoNegado() {

		AuthorizationDeniedException ex = mock(AuthorizationDeniedException.class);

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleAccessDenied(ex);

		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

		assertEquals("Acesso negado", response.getBody().erro());
	}

	@Test
	void deveRetornar403QuandoAccessDeniedException() {

		AccessDeniedException ex = new AccessDeniedException("Negado");

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleAccessDenied(ex);

		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}

	@Test
	void deveRetornar500QuandoErroJpa() {

		JpaSystemException ex = mock(JpaSystemException.class);

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleJpaSystemException(ex);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void deveRetornar500QuandoExcecaoGenerica() {

		Exception ex = new RuntimeException("Erro");

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleException(ex);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	void deveRetornar400QuandoMethodArgumentNotValid() {

		BindingResult bindingResult = mock(BindingResult.class);
		FieldError fieldError = new FieldError("objeto", "email", "e-mail inválido");

		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

		when(ex.getBindingResult()).thenReturn(bindingResult);

		when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleMethodArgumentNotValid(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("email: e-mail inválido", response.getBody().mensagem());
	}

	@Test
	void deveRetornar400QuandoHandlerMethodValidationException() {

		HandlerMethodValidationException ex = mock(HandlerMethodValidationException.class);

		when(ex.getMessage()).thenReturn("Parâmetro inválido");

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleHandlerMethodValidation(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("Parâmetros inválidos", response.getBody().erro());
	}

	@Test
	void deveRetornar400QuandoConstraintViolationException() {

		ConstraintViolation<?> violation = mock(ConstraintViolation.class);

		when(violation.getMessage()).thenReturn("CPF inválido");

		ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleConstraintViolation(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("CPF inválido", response.getBody().mensagem());
	}

	@Test
	void deveRetornar400QuandoPayloadInvalido() {

		RuntimeException causa = new RuntimeException("JSON inválido");

		HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Erro", causa, null);

		ResponseEntity<ErroResponse> response = globalExceptionHandler.handleMessageNotReadable(ex);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertEquals("Payload inválido", response.getBody().erro());
	}
}