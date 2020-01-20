package com.example.webservices.transactions.dataAccess;

import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.example.webservices.transactions.models.Transaction;

import java.util.List;
import java.util.UUID;

public class JpaTransactionDatastore implements ITransactionDatastore {
    private TransactionRepository transactionRepository;

    private boolean exists(UUID id){
        return this.transactionRepository.findById(id).isPresent();
    }

    public JpaTransactionDatastore(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction getTransaction(UUID transactionId) throws EntryNotFoundException {
        return this.transactionRepository.findById(transactionId).orElseThrow(EntryNotFoundException::new);
    }

    @Override
    public UUID addTransaction(Transaction transaction) throws DuplicateEntryException {
        if(exists(transaction.getTransactionId())){
            throw new DuplicateEntryException();
        }
        this.transactionRepository.save(transaction);
        return transaction.getTransactionId();
    }

    @Override
    public List<Transaction> getTransactions(UUID accountId) {
        return this.transactionRepository.findAllByDebtorOrCreditor(accountId, accountId);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return this.transactionRepository.save(transaction);
    }
}
