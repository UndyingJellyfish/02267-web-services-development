package com.example.webservices.library.exceptions;

public abstract class TokenException extends Exception {
    private static String defaultErrorString = "Generic token exception.";

    public TokenException(String message){
        super(message);
    }
    public TokenException(){
        this(defaultErrorString);
    }
}

