package com.example.siapraja.handler;

import com.example.siapraja.dto.ResponData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Menangkap RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponData<?>> handleRuntimeException(RuntimeException ex) {
        ResponData<?> response = new ResponData<>();
        response.setStatus(false);
        response.getMessage().add(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 2. Menangkap error Security (Jika user paksa akses data orang lain)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponData<?>> handleAccessDeniedException(AccessDeniedException ex) {
        ResponData<?> response = new ResponData<>();
        response.setStatus(false);
        response.getMessage().add("You do not have permission to access this data!");

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // 3. Menangkap error umum lainnya (Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponData<?>> handleGeneralException(Exception ex) {
        ResponData<?> response = new ResponData<>();
        response.setStatus(false);
        response.getMessage().add("A system error occurred: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    //Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponData<Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        ResponData<Object> response = new ResponData<>();
        response.setStatus(false);

        ex.getBindingResult().getAllErrors().forEach(err -> {
            response.getMessage().add(err.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}