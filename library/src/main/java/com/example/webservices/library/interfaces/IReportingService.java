package com.example.webservices.library.interfaces;

import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.dataTransferObjects.RequestReportingHistoryDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;

import java.util.List;
import java.util.UUID;

public interface IReportingService {
    List<TransactionDto> getTransactionHistory(UUID id) throws EntryNotFoundException;

    List<TransactionDto> getTransactionHistorySince(RequestReportingHistoryDto dto) throws EntryNotFoundException;
}
