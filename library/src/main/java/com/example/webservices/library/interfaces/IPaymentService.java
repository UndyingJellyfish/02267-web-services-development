package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.*;

import java.util.UUID;

public interface IPaymentService {
    /**
     * @param transactionDto data object used to transfer money from one account to another using a token
     * @return gives the transfer details
     * @throws EntryNotFoundException thrown when either of the accounts (debtor or creditor) do not exist
     * @throws TokenException thrown when the token specified in {@param transactionDto} is invalid
     * @throws BankException thrown when the bank is unavailable for transfers at the current time
     * @throws InvalidTransferAmountException thrown when the amount of money specified is not allowed
     * @throws DuplicateEntryException thrown when the transaction already exists
     */
    TransactionDto transfer(TransactionDto transactionDto) throws EntryNotFoundException, TokenException, BankException, InvalidTransferAmountException, DuplicateEntryException;

    /**
     * @param transactionId the transaction to refund
     * @throws EntryNotFoundException thrown when the transaction does not exist
     * @throws DuplicateEntryException thrown when the transaction has already been refunded
     */
    void refund(UUID transactionId) throws EntryNotFoundException, DuplicateEntryException;
}
