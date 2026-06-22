package com.techchallenger.oficina360.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex
    ) {
        return erro(
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ErroResponse> handleConflito(
            ConflitoException ex
    ) {
        return erro(
                HttpStatus.CONFLICT,
                "Conflito",
                ex.getMessage()
        );
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroRegraDeNegocioResponse> handleRegraDeNegocio(
            RegraDeNegocioException ex
    ) {
        ErroRegraDeNegocioResponse erro = new ErroRegraDeNegocioResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Regra de negócio violada",
                ex.getMessage(),
                ex.getMensagens(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");

        return erro(
                HttpStatus.BAD_REQUEST,
                "Dados inválidos",
                mensagem
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErroResponse> handleHandlerMethodValidation(
            HandlerMethodValidationException ex
    ) {
        return erro(
                HttpStatus.BAD_REQUEST,
                "Parâmetros inválidos",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraintViolation(
            ConstraintViolationException ex
    ) {

        String mensagem = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Validação inválida");

        return erro(
                HttpStatus.BAD_REQUEST,
                "Validação inválida",
                mensagem
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex
    ) {
        return erro(
                HttpStatus.BAD_REQUEST,
                "Payload inválido",
                "O corpo da requisição possui formato inválido"
        );
    }

    @ExceptionHandler({
            AuthorizationDeniedException.class,
            AccessDeniedException.class
    })
    public ResponseEntity<ErroResponse> handleAccessDenied(
            Exception ex
    ) {
        return erro(
                HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não tem permissão para executar esta operação"
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {

        log.warn("Violação de integridade de dados", ex);

        return erro(
                HttpStatus.CONFLICT,
                "Violação de integridade",
                "Operação não permitida devido à integridade dos dados"
        );
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<ErroResponse> handleJpaSystemException(
            JpaSystemException ex
    ) {

        log.error("Erro de persistência", ex);

        return erro(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro de persistência",
                "Falha ao processar os dados da aplicação"
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleException(
            Exception ex
    ) {

        log.error("Erro inesperado", ex);

        return erro(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu um erro inesperado"
        );
    }

    private ResponseEntity<ErroResponse> erro(
            HttpStatus status,
            String erro,
            String mensagem
    ) {

        return ResponseEntity
                .status(status)
                .body(
                        new ErroResponse(
                                status.value(),
                                erro,
                                mensagem,
                                LocalDateTime.now()
                        )
                );
    }
}