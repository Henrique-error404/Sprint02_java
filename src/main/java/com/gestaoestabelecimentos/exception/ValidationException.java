package com.gestaoestabelecimentos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends BusinessException {

    private List<String> errors = new ArrayList<>();

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
    }

    public ValidationException(String message, List<String> errors) {
        super(message, "VALIDATION_ERROR");
        this.errors = errors;
    }

    public ValidationException(List<String> errors) {
        super("Erro de validação", "VALIDATION_ERROR");
        this.errors = errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    // Getters
    public List<String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}