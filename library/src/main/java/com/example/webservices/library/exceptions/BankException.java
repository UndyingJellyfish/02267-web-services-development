package com.example.webservices.library.exceptions;

/** @author s164410 */
public class BankException extends Exception {
    private static String defaultErrorString = "Bank operation failed.";

    public BankException(String message) {
        super(message);
    }
    public BankException(){
        this(defaultErrorString);
    }
}
