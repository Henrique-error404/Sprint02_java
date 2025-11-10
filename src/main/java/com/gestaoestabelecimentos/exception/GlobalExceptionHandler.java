package com.gestaoestabelecimentos.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handler para ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        logger.warn("Recurso não encontrado: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Recurso não encontrado",
                ex.getMessage(),
                "RESOURCE_NOT_FOUND",
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // Handler para DuplicateResourceException
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {

        logger.warn("Recurso duplicado: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Recurso duplicado",
                ex.getMessage(),
                "DUPLICATE_RESOURCE",
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    // Handler para ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(
            ValidationException ex, HttpServletRequest request) {

        logger.warn("Erro de validação: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                ex.getMessage(),
                "VALIDATION_ERROR",
                request.getRequestURI()
        );

        if (ex.hasErrors()) {
            apiError.setDetails(ex.getErrors());
        }

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handler para validações do Bean Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        logger.warn("Erro de validação de argumentos: {}", errors);

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação",
                "Dados de entrada inválidos",
                "INVALID_INPUT",
                request.getRequestURI()
        );
        apiError.setDetails(errors);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handler para ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        logger.warn("Violação de constraints: {}", errors);

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Violação de constraints",
                "Dados inválidos fornecidos",
                "CONSTRAINT_VIOLATION",
                request.getRequestURI()
        );
        apiError.setDetails(errors);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handler para DataIntegrityViolationException (ex: violação de unique constraint)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        logger.error("Violação de integridade de dados: {}", ex.getMessage());

        // Verifica se é violação de constraint única
        String message = "Erro de integridade de dados";
        if (ex.getMessage() != null && ex.getMessage().contains("constraint")) {
            if (ex.getMessage().toLowerCase().contains("unique") ||
                    ex.getMessage().toLowerCase().contains("duplicate")) {
                message = "Dados duplicados. Verifique se o recurso já existe.";
            }
        }

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                "Violação de integridade de dados",
                message,
                "DATA_INTEGRITY_VIOLATION",
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    // Handler para BusinessException genérica
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {

        logger.warn("Erro de negócio: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de negócio",
                ex.getMessage(),
                ex.getErrorCode() != null ? ex.getErrorCode() : "BUSINESS_ERROR",
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handler genérico para todas as outras exceções
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
            Exception ex, HttpServletRequest request) {

        logger.error("Erro interno do servidor: {}", ex.getMessage(), ex);

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno do servidor",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.",
                "INTERNAL_SERVER_ERROR",
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}