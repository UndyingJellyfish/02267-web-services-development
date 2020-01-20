package com.example.webservices.library.exceptions;

public class UsedTokenException extends TokenException {
    private static String defaultErrorString = "Token has already been used.";

    public UsedTokenException(String message) {
        super(message);
    }
    public UsedTokenException(){
        this(defaultErrorString);
    }
}
