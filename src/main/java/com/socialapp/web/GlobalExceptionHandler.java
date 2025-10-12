package com.socialapp.web;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(error("Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> unauthorized(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error("Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validation(MethodArgumentNotValidException ex) {
        var first = ex.getBindingResult().getFieldErrors().stream().findFirst();
        String msg = first.map(f -> f.getField() + " " + f.getDefaultMessage()).orElse("Validation error");
        return ResponseEntity.badRequest().body(error("Validation error", msg));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> server(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error("Server Error", ex.getMessage()));
    }

    private Map<String, Object> error(String code, String message) {
        return Map.of("timestamp", Instant.now().toString(), "code", code, "message", message);
    }
}
