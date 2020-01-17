package com.example.webservices.library.exceptions;

public class BankException extends Exception {
    private static String defaultErrorString = "Bank operation failed.";

    public BankException(String message) {
        super(message);
    }
    public BankException(){
        this(defaultErrorString);
    }
}
