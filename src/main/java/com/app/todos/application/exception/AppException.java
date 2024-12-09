package com.app.todos.application.exception;

import java.io.Serial;

public class AppException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public AppException(String message) {
        super(message);
    }
}
