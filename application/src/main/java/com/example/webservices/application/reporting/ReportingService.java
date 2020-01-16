package com.example.webservices.application.reporting;

import com.example.webservices.application.dataAccess.IAccountDatastore;
import com.example.webservices.application.dataAccess.ITransactionDatastore;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.models.Account;
import com.example.webservices.application.models.Merchant;
import com.example.webservices.application.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReportingService {
    private ITransactionDatastore transactionDatastore;
    private IAccountDatastore accountDatastore;

    public ReportingService(ITransactionDatastore transactionDatastore, IAccountDatastore accountDatastore) {
        this.transactionDatastore = transactionDatastore;
        this.accountDatastore = accountDatastore;
    }

    public List<Transaction> getTransactionHistory(UUID id) throws EntryNotFoundException {
        Account account = accountDatastore.getAccount(id);
        List<Transaction> transactions = transactionDatastore.getTransactions(account);
        if(account instanceof Merchant){
            transactions.forEach(Transaction::anonymize);
        }
        return transactions;
    }
}
