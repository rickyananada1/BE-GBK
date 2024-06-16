package com.dev.gbk.exception;

import org.springframework.http.HttpStatus;

public class GBKAPIException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public GBKAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public GBKAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}