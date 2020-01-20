package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.DuplicateEntryException;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.List;
import java.util.UUID;

public interface ITransactionService {

    List<TransactionDto> getTransactions(UUID id);
    UUID refundTransaction(UUID tokenId) throws EntryNotFoundException, DuplicateEntryException;
    UUID addTransaction(TransactionDto transaction) throws DuplicateEntryException;
    TransactionDto getTransaction(UUID tokenId) throws EntryNotFoundException;
}
