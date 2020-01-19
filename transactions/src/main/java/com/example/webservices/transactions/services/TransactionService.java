package com.example.webservices.transactions.services;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.interfaces.ITransactionService;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public void AddTransaction(TransactionDto transaction) {

    }

    @Override
    public void RefundTransaction(UUID tokenId) {
        TransactionDto dto = GetTransactionByTokenId(tokenId);
        UUID temp = dto.getCreditorId();
        dto.setCreditor(dto.getDebtorId());
        dto.setDebtor(temp);
        AddTransaction(dto);
    }

    @Override
    public TransactionDto GetTransactionByTokenId(UUID tokenId) {
        return null;
    }
}
