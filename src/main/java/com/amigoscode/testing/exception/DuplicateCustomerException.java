package com.amigoscode.testing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateCustomerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DuplicateCustomerException(String message) {
        super(message);
    }

    public DuplicateCustomerException(String message, Throwable cause) {
        super(message, cause);
    }
}
