package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {

    List<TransactionDto> getTransactions(UUID id);
    UUID addTransaction(TransactionDto transaction);
    TransactionDto getTransactionByTokenId(UUID tokenId) throws EntryNotFoundException;
}
