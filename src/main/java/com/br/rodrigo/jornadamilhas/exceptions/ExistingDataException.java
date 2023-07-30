package com.br.rodrigo.jornadamilhas.exceptions;

public class ExistingDataException extends RuntimeException {
    public ExistingDataException(String message) {
        super(message);
    }
}
