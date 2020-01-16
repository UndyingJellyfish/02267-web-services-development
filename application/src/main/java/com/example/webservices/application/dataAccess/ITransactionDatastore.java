package com.example.webservices.application.dataAccess;

import com.example.webservices.application.models.Account;
import com.example.webservices.application.models.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionDatastore {
    Transaction GetTransactionByTokenId(UUID tokenId);
    void AddTransaction(Transaction transaction);

    List<Transaction> getTransactions(Account account);
}
