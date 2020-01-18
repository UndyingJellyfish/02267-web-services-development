package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {

    List<TransactionDto> getTransactions(UUID id);
    void AddTransaction(TransactionDto transaction);
    TransactionDto GetTransactionByTokenId(UUID tokenId);
}
