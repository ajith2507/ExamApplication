package com.autum.examapp.exception;

public class OtpExpiredException extends RuntimeException {

    public OtpExpiredException(String message) {
        super(message);
    }
}