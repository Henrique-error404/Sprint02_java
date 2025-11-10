package com.gestaoestabelecimentos.exception;

public class BusinessException extends RuntimeException {

    private String errorCode;
    private String details;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message, String errorCode, String details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // Getters
    public String getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}