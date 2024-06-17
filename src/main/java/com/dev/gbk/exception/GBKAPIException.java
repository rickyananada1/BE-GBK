package com.dev.gbk.exception;

public class GBKAPIException extends RuntimeException {

    private final String message;

    public GBKAPIException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}