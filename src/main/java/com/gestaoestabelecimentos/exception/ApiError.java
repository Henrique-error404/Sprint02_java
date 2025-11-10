package com.gestaoestabelecimentos.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private int status;
    private String error;
    private String message;
    private String errorCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private String path;
    private List<String> details;

    // Construtores
    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(int status, String error, String message) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ApiError(int status, String error, String message, String errorCode) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
    }

    public ApiError(int status, String error, String message, String errorCode, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
        this.path = path;
    }

    public ApiError(int status, String error, String message, List<String> details) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

    // Getters e Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}