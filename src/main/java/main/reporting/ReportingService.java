package main.reporting;

import main.dataAccess.IAccountDatastore;
import main.dataAccess.ITransactionDatastore;
import main.models.Account;
import main.models.Merchant;
import main.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportingService {
    private ITransactionDatastore transactionDatastore;
    private IAccountDatastore accountDatastore;

    public ReportingService(ITransactionDatastore transactionDatastore, IAccountDatastore accountDatastore) {
        this.transactionDatastore = transactionDatastore;
        this.accountDatastore = accountDatastore;
    }

    public List<Transaction> getTransactionHistory(UUID id) {
        Account account = accountDatastore.getAccount(id);
        List<Transaction> transactions = transactionDatastore.getTransactions(account);
        if(account instanceof Merchant){
            transactions.forEach(Transaction::anonymize);
        }
        return transactions;
    }
}
