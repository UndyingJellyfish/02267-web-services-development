package com.example.webservices.library.exceptions;

/** @author s164407*/
public class DuplicateEntryException extends Exception{
    private static String defaultErrorString = "Item with that key already exists in collection";
    public DuplicateEntryException(String message) {
        super(message);
    }
    public DuplicateEntryException(){
        this(defaultErrorString);
    }
}
