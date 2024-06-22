package com.dev.gbk.exception;

public class GBKAPIException extends RuntimeException {

    public GBKAPIException() {
        super();
    }

    public GBKAPIException(String message) {
        super(message);
    }

    public GBKAPIException(String message, Throwable cause) {
        super(message, cause);
    }

    public GBKAPIException(Throwable cause) {
        super(cause);
    }
}