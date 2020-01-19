package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.InvalidTransferAmountException;
import com.example.webservices.library.exceptions.TokenException;

import java.math.BigDecimal;
import java.util.UUID;

public interface IPaymentService {
    TransactionDto transfer(TransactionDto transactionDto) throws EntryNotFoundException, TokenException, BankException, InvalidTransferAmountException;

    void refund(UUID transactionId);
}
