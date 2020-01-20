package com.example.webservices.transactions.interfaces;

import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.transactions.models.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionDatastore {
    Transaction getTransaction(UUID transactionId) throws EntryNotFoundException;
    UUID addTransaction(Transaction transaction);
    List<Transaction> getTransactions(UUID accountId);
}
