package com.mehedi.exception;

public class BookReturnException extends RuntimeException {

    public BookReturnException(String message) {
        super(message);
    }

    public BookReturnException(String message, Throwable cause) {
        super(message, cause);
    }
}