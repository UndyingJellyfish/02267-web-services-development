package com.example.webservices.application.transfers;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.BankException;
import com.example.webservices.library.exceptions.EntryNotFoundException;
import com.example.webservices.library.exceptions.TokenException;
import com.example.webservices.library.interfaces.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService implements IPaymentService {
    private ITokenManager tokenManager;
    private IAccountService accountService;
    private ITransactionService transactionService;
    private IBank bank;

    public PaymentService(ITokenManager tokenManager, IAccountService accountService, ITransactionService transactionService, IBank bank) {
        this.tokenManager = tokenManager;
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.bank = bank;
    }

    @Override
    public TransactionDto transfer(UUID tokenId, UUID merchantId, BigDecimal amount, String description) throws EntryNotFoundException, TokenException, BankException {
        return this.transfer(tokenId, merchantId, amount, false, description);
    }

    @Override
    public TransactionDto transfer(UUID tokenId, UUID merchantId, BigDecimal amount, boolean isRefund, String description) throws EntryNotFoundException, TokenException, BankException {
        if(!isGreaterThanZero(amount)){
            throw new IllegalArgumentException();
        }
        TokenDto token = tokenManager.GetToken(tokenId);
        AccountDto merchant = accountService.getMerchant(merchantId);
        AccountDto customer = accountService.getCustomer(token.getCustomerId());
        tokenManager.UseToken(tokenId);
        bank.transferMoney(customer, merchant, amount, description);
        TransactionDto transaction = new TransactionDto(tokenId, merchant.getAccountId(), customer.getAccountId(), amount, description, isRefund);
        transactionService.AddTransaction(transaction);
        return transaction;
    }

    private boolean isGreaterThanZero(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) > 0;
    }

    @Override
    public void refund(UUID customerId, UUID merchantId, UUID tokenId) throws TokenException, BankException, EntryNotFoundException {
        UUID newToken = tokenManager.RequestToken(customerId);
        TransactionDto oldTransaction;

        oldTransaction = transactionService.GetTransactionByTokenId(tokenId);
        this.transfer(newToken, merchantId, oldTransaction.getAmount(), true,"Refund");

    }

}
