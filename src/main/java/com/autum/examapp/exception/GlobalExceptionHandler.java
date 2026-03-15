package com.autum.examapp.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import com.autum.examapp.exception.QuizNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(UserNotFoundException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<?> handleQuizNotFound(QuizNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", 404);
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<?> handleInvalidOtp(InvalidOtpException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", 400);
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<?> handleOtpExpired(OtpExpiredException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", 400);
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(OtpNotRequestedException.class)
    public ResponseEntity<?> handleOtpNotRequested(OtpNotRequestedException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", 400);
        error.put("message", ex.getMessage());
        error.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}