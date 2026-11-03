package com.example.assettracker.exception;

public class RecordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    /*
    // Consider of including structured data for future use
    private final String entity;
    private final Object id;

    public RecordNotFoundException(String entity, Object id) {
        super(entity + " not found: " + id);
        this.entity = entity;
        this.id = id;
    }
    // Getters...
    */

    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
