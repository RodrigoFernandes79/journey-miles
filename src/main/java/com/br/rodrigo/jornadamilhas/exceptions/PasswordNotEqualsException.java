package com.br.rodrigo.jornadamilhas.exceptions;

public class PasswordNotEqualsException extends RuntimeException {
    public PasswordNotEqualsException(String message) {
        super(message);
    }
}
