package com.example.webservices.application.reporting;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.interfaces.ITransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService implements ITransactionService {
    @Override
    public List<TransactionDto> getTransactions(UUID id) {
        return null;
    }

    @Override
    public void AddTransaction(TransactionDto transaction) {

    }

    @Override
    public TransactionDto GetTransactionByTokenId(UUID tokenId) {
        return null;
    }
}
