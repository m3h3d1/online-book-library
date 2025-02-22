package com.mehedi.exception;

public class ErrorMessage extends RuntimeException {
    public ErrorMessage(String message) {
        super(message);
    }
}