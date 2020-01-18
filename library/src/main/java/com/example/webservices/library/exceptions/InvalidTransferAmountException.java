package com.example.webservices.library.exceptions;

public class InvalidTransferAmountException extends Exception {

    public InvalidTransferAmountException(){
        super("Invalid amount to transfer");
    }

    public InvalidTransferAmountException(String message){
        super(message);
    }
}
