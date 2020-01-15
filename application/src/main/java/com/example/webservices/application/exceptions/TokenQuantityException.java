package com.example.webservices.application.exceptions;

public class TokenQuantityException extends TokenException {
    private static String defaultErrorString = "Too many active tokens.";

    public TokenQuantityException(String message) {
        super(message);
    }
    public TokenQuantityException(){
        this(defaultErrorString);
    }
}
