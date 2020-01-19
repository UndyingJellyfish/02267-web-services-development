package com.example.webservices.transactions.services;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.AccountType;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.IReportingService;
import com.example.webservices.library.interfaces.ITransactionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportingService implements IReportingService {
    private  ITransactionService transactionService;
    private IAccountService accountService;

    public ReportingService(ITransactionService transactionService, IAccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    private void Anonymize(TransactionDto transaction) {
        transaction.setDebtor(null);
    }

    @Override
    public List<TransactionDto> getTransactionHistory(UUID id) throws EntryNotFoundException {
        AccountDto account = accountService.getAccount(id);
        List<TransactionDto> transactions = this.transactionService.getTransactions(id);
        if(account.getType().equals(AccountType.MERCHANT)){
            transactions.forEach(this::Anonymize);
        }
        return transactions;
    }


}
