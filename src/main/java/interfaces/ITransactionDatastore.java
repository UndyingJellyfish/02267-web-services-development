package interfaces;

import models.Transaction;

import java.util.UUID;

public interface ITransactionDatastore {
    Transaction GetTransactionByTokenId(UUID tokenId);
    void AddTransaction(Transaction transaction);
}
