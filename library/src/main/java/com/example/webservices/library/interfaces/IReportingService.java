package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.dataTransferObjects.RequestReportingHistoryDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.UUID;

public interface IReportingService {
    /**
     * @param id account of the user to search for
     * @return the full list of all transactions involving an account
     * @throws EntryNotFoundException thrown when the customer does not exist
     */
    List<TransactionDto> getTransactionHistory(UUID id) throws EntryNotFoundException;

    /**
     * @param dto data object containing the id of the user and the search date
     * @return a list of transactions for the account since the specified date
     * @throws EntryNotFoundException thrown when the customer does not exist
     */
    List<TransactionDto> getTransactionHistorySince(RequestReportingHistoryDto dto) throws EntryNotFoundException;
}
