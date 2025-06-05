package com.personal.laneheroes.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, String identifier) {
        super(entityName + " not found with identifier: " + identifier);
    }
}