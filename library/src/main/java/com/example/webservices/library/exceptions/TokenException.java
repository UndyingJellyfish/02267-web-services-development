package com.example.webservices.library.exceptions;

/** @author s164398 */

public abstract class TokenException extends Exception {
    private static String defaultErrorString = "Generic token exception.";

    public TokenException(String message){
        super(message);
    }
    public TokenException(){
        this(defaultErrorString);
    }
}

