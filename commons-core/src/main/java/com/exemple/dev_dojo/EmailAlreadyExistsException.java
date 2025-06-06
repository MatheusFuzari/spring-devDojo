package com.exemple.dev_dojo;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailAlreadyExistsException extends ResponseStatusException {

    public EmailAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
