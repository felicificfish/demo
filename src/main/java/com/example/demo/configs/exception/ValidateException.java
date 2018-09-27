package com.example.demo.configs.exception;

public class ValidateException extends RuntimeException {
    public ValidateException(String message) {
        super(message);
    }

    public static ValidateException of(String msgCode) {
        return new ValidateException(msgCode);
    }
}
