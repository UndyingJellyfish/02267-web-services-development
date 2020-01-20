package com.example.webservices.transactions.services;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.ITransactionService;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import com.example.webservices.transactions.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService implements ITransactionService {


    private final ITransactionDatastore transactionDatastore;

    public TransactionService(ITransactionDatastore transactionDatastore) {
        this.transactionDatastore = transactionDatastore;
    }


    @Override
    public List<TransactionDto> getTransactions(UUID id) {

        return this.transactionDatastore.getTransactions(id)
                .stream()
                .map(t ->
                        new TransactionDto(
                                t.getTransactionId(),
                                t.getTokenId(),
                                t.getCreditorId(),
                                t.getDebtorId(),
                                t.getAmount(),
                                t.getDescription(),
                                t.isRefund(),
                                t.getTransactionDate()
                        )
                    )
                .collect(Collectors.toList());
    }

    @Override
    public UUID addTransaction(TransactionDto dto) {
        Transaction transaction = new Transaction(
                dto.getCreditorId(),
                dto.getDebtorId(),
                dto.getAmount(),
                dto.getTokenId(),
                dto.getDescription(),
                dto.isRefund(),
                dto.getTransactionDate());

        return transactionDatastore.addTransaction(transaction);
    }

    @Override
    public UUID refundTransaction(UUID transactionId) throws EntryNotFoundException {
        TransactionDto dto = getTransaction(transactionId);
        // Swap debtor creditor
        UUID temp = dto.getCreditorId();
        dto.setCreditorId(dto.getDebtorId());
        dto.setDebtorId(temp);

        return addTransaction(dto);
    }

    @Override
    public TransactionDto getTransaction(UUID transactionId) throws EntryNotFoundException {
        Transaction transaction = transactionDatastore.getTransaction(transactionId);
        return new TransactionDto(
                transaction.getTransactionId(),
                transaction.getTokenId(),
                transaction.getCreditorId(),
                transaction.getDebtorId(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.isRefund(),
                transaction.getTransactionDate());
    }
}
