package com.paradox.savemoney.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String entityName) {
        super(entityName + " not found.");
    }
}
