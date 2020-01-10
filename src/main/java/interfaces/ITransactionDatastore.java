package interfaces;

import models.Account;
import models.Customer;
import models.Transaction;

import java.util.List;
import java.util.UUID;

public interface ITransactionDatastore {
    Transaction GetTransactionByTokenId(UUID tokenId);
    void AddTransaction(Transaction transaction);

    List<Transaction> getTransactions(Account account);
}
