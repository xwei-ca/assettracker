package com.example.assettracker.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handle404(NoHandlerFoundException ex) {
        return new ResponseEntity<>(
            buildErrorBody(HttpStatus.NOT_FOUND, "The requested URL was not found on this server.", ex.getRequestURL()),
            HttpStatus.NOT_FOUND);
        
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRecordNotFound(RecordNotFoundException ex, 
                                                                    HttpServletRequest request) {
        return new ResponseEntity<>(
            buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<Map<String, Object>> handleDupicateRecord(DuplicateRecordException ex, 
                                                                    HttpServletRequest request) {
        return new ResponseEntity<>(
            buildErrorBody(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI()),
            HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .map(e -> e.getField() + ": " + e.getDefaultMessage())
            .collect(Collectors.joining(", "));
        return new ResponseEntity<>(
            buildErrorBody(HttpStatus.BAD_REQUEST, message, request.getRequestURI()),
            HttpStatus.BAD_REQUEST);
    }
    
    private Map<String, Object> buildErrorBody(HttpStatus status, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", path);
        return body;
    }
}
