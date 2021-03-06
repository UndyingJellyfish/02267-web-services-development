package com.example.webservices.library.exceptions;

/** @author s164395 */

public class InvalidTokenException extends TokenException{
    private static String defaultErrorString = "Token does not exist.";
    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException(){
        this(defaultErrorString);
    }
}
