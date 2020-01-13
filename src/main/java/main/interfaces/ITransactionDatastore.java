package main.interfaces;

import main.models.Account;
import main.models.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionDatastore {
    Transaction GetTransactionByTokenId(UUID tokenId);
    void AddTransaction(Transaction transaction);

    List<Transaction> getTransactions(Account account);
}