package com.example.webservices.transactions.dataAccess;

import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.example.webservices.transactions.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InMemoryTransactionDatastore implements ITransactionDatastore {

    private List<Transaction> transactions = new ArrayList<>();

    public void flush(){
        transactions = new ArrayList<>();
    }


    @Override
    public Transaction getTransactionByTokenId(UUID tokenId) throws EntryNotFoundException {
        return this.transactions.stream().filter(t -> t.getTokenId().equals(tokenId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public UUID addTransaction(Transaction transaction) {
        if(transactions.stream().anyMatch(t -> t.getTokenId().equals(transaction.getTokenId()))){
            throw new IllegalArgumentException("The value is already in the list.");
        }
        this.transactions.add(transaction);
        return transaction.getTransactionId();
    }

    @Override
    public List<Transaction> getTransactions(UUID accountId) {
        return this.transactions.stream().filter(t -> t.getCreditorId().equals(accountId) ||
                t.getDebtorId().equals(accountId)
        ).collect(Collectors.toList());

    }


}
