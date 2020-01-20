package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.*;

import java.math.BigDecimal;
import java.util.UUID;

public interface IPaymentService {
    TransactionDto transfer(TransactionDto transactionDto) throws EntryNotFoundException, TokenException, BankException, InvalidTransferAmountException, DuplicateEntryException;

    void refund(UUID transactionId) throws EntryNotFoundException, DuplicateEntryException;
}
