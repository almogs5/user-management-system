package com.tasks.user_management_system.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.tasks.user_management_system.util.ConstantsUtil.ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred", ex);
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, "An unexpected error occurred. Please try again later.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found", ex);
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument", ex);
        Map<String, String> error = new HashMap<>();
        error.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, String>> handleDatabaseException(DataAccessException ex) {
        String errorMessage = "An error occurred while connecting to the database. Please try again later.";
        log.error(errorMessage, ex);

        if (ex.getCause() instanceof SQLException sqlException) {
            if (sqlException.getErrorCode() == 0) {
                errorMessage = "Could not connect to the database. Please check the database server.";
            }
        }

        Map<String, String> error = new HashMap<>();
        error.put(ERROR, errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
