package com.br.rodrigo.jornadamilhas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ApiExceptionsHandler {

    @ExceptionHandler(ExistingDataException.class)
    public ResponseEntity<String> existingData(ExistingDataException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<String> dataNotFound(DataNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PasswordNotEqualsException.class)
    public ResponseEntity<String> passwordNotEquals(PasswordNotEqualsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DataFieldValidation>> dataField(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors();
        var error = errors.stream().map(DataFieldValidation::new).toList();
        return ResponseEntity.badRequest().body(error);
    }

    private record DataFieldValidation(String error, String message) {
        private DataFieldValidation(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
