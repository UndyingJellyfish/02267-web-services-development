package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenException;

import java.math.BigDecimal;
import java.util.UUID;

public interface IPaymentService {
    TransactionDto transfer(UUID tokenId, UUID merchantId, BigDecimal amount, String description) throws EntryNotFoundException, TokenException, BankException;

    TransactionDto transfer(UUID tokenId, UUID merchantId, BigDecimal amount, boolean isRefund, String description) throws EntryNotFoundException, TokenException, BankException;

    void refund(UUID customerId, UUID merchantId, UUID tokenId) throws TokenException, BankException, EntryNotFoundException;
}
