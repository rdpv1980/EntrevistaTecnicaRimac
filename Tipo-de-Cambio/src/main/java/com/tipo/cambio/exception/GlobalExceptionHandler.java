package com.tipo.cambio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = Map.of(
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now()
        );

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(MissingServletRequestParameterException  ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "El parámetro '" + ex.getParameterName() + "' es obligatorio.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
