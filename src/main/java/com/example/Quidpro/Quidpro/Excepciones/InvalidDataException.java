package com.example.Quidpro.Quidpro.Excepciones;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }
}
