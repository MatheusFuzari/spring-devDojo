package com.exemple.dev_dojo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handleNotFoundException(NotFoundException e){
        var error = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<DefaultErrorMessage> handleEmailAlreadyExistsException(EmailAlreadyExistsException e){
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getReason());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<DefaultErrorMessage> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        var error = new DefaultErrorMessage(HttpStatus.BAD_REQUEST.value(), "Duplicated entry for one of the unique fields");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
