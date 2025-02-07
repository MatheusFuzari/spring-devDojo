package com.exemple.dev_dojo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomNotFoundException extends ResponseStatusException {

    public CustomNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
