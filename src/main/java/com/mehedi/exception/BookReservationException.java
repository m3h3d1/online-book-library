package com.mehedi.exception;

public class BookReservationException extends RuntimeException {

    public BookReservationException() {
        super("Book reservation exception occurred.");
    }

    public BookReservationException(String message) {
        super(message);
    }

    public BookReservationException(String message, Throwable cause) {
        super(message, cause);
    }
}
