package com.example.webservices.transactions.services;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.dataTransferObjects.RequestReportingHistoryDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.IReportingService;
import com.example.webservices.library.interfaces.ITransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReportingService implements IReportingService {
    private ITransactionService transactionService;
    private IAccountService accountService;

    public ReportingService(ITransactionService transactionService, IAccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    private void Anonymize(TransactionDto transaction) {
        transaction.setDebtorId(null);
    }

    @Override
    public List<TransactionDto> getTransactionHistory(UUID id) throws EntryNotFoundException {
        return getTransactionHistoryWithStartDate(new RequestReportingHistoryDto(id));
    }

    @Override
    public List<TransactionDto> getTransactionHistorySince(RequestReportingHistoryDto dto) throws EntryNotFoundException {
        return getTransactionHistoryWithStartDate(dto);
    }

    private List<TransactionDto> getTransactionHistoryWithStartDate(RequestReportingHistoryDto dto) throws EntryNotFoundException{
        AccountDto account = accountService.getAccount(dto.getAccountId());
        List<TransactionDto> transactions = this.transactionService.getTransactions(dto.getAccountId());
        if (dto.getAccountId() != null) {
            transactions.removeIf(x -> x.getTransactionDate().before(dto.getStartDate()));
        }
        if (account.getType().equals(AccountType.MERCHANT)) {
            transactions.forEach(this::Anonymize);
        }
        return transactions;
    }

}
