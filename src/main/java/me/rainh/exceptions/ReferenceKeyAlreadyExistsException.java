package me.rainh.exceptions;

public class ReferenceKeyAlreadyExistsException extends RuntimeException{
    public ReferenceKeyAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
