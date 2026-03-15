package com.autum.examapp.exception;

public class QuizNotFoundException extends RuntimeException {

    public QuizNotFoundException(String message) {
        super(message);
    }
}