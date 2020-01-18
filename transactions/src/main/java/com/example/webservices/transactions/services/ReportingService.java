package com.example.webservices.transactions.services;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.interfaces.IAccountService;
import com.example.webservices.library.interfaces.IReportingService;
import com.example.webservices.transactions.interfaces.ITransactionDatastore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportingService implements IReportingService {
    private final ITransactionDatastore transactionDatastore;
    private IAccountService accountService;

    public ReportingService(ITransactionDatastore transactionDatastore, IAccountService accountService) {
        this.transactionDatastore = transactionDatastore;
        this.accountService = accountService;
    }

    private void Anonymize(TransactionDto transaction) {
        transaction.setDebtor(null);
    }

    @Override
    public List<TransactionDto> getTransactionHistory(UUID id) throws EntryNotFoundException {
        AccountDto account = accountService.getAccount(id);
        List<TransactionDto> transactions = this.transactionDatastore.getTransactions(id)
                .stream()
                .map(t ->
                        new TransactionDto(t.getTransactionId(),
                            t.getTokenId(), t.getCreditorId(),
                            t.getDebtorId(), t.getAmount(),
                            t.getDescription(),
                            t.isRefund(),
                            t.getTransactionDate())
                )
                .collect(Collectors.toList());
        if(account.getType().toString().equals("Merchant")){
            transactions.forEach(this::Anonymize);
        }
        return transactions;
    }

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
