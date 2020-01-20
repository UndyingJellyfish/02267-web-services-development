package com.example.webservices.transactions.dataAccess;

import com.example.webservices.transactions.models.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

    List<Transaction> findAllByDebtorOrCreditor(UUID debtor, UUID creditor);
}
