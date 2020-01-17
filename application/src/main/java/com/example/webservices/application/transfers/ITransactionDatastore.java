package com.example.webservices.application.transfers;

import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ITransactionDatastore {
    Transaction GetTransactionByTokenId(UUID tokenId) throws EntryNotFoundException;
    void AddTransaction(Transaction transaction);

    List<Transaction> getTransactions(UUID accountId);
}
