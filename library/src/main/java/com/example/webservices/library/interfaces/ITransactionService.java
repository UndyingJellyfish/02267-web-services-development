package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {

    List<TransactionDto> getTransactions(UUID id);
    UUID refundTransaction(UUID tokenId) throws EntryNotFoundException;
    UUID addTransaction(TransactionDto transaction) throws JsonProcessingException;
    TransactionDto getTransaction(UUID tokenId) throws EntryNotFoundException, JsonProcessingException;
}
