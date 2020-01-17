package com.example.webservices.library.exceptions;

public class EntryNotFoundException extends Exception{
    private static String defaultErrorString = "Item with that key does not exists in collection";
    public EntryNotFoundException(String message) {
        super(message);
    }
    public EntryNotFoundException(){
        this(defaultErrorString);
    }
}
