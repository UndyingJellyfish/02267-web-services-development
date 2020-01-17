package com.example.webservices.application.transfers;

import com.example.webservices.library.exceptions.EntryNotFoundException;
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
    public Transaction GetTransactionByTokenId(UUID tokenId) throws EntryNotFoundException {
        return this.transactions.stream().filter(t -> t.getTokenId().equals(tokenId)).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public void AddTransaction(Transaction transaction) {
        if(transactions.stream().anyMatch(t -> t.getTokenId().equals(transaction.getTokenId()))){
            throw new IllegalArgumentException("The value is already in the list.");
        }
        this.transactions.add(transaction);
    }

    @Override
    public List<Transaction> getTransactions(UUID accountId) {
        return this.transactions.stream().filter(t -> t.getCreditorId().equals(accountId) ||
                t.getDebtorId().equals(accountId)
        ).collect(Collectors.toList());

    }


}
