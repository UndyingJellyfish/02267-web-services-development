package com.example.webservices.payments.services;

import com.example.webservices.library.dataTransferObjects.AccountDto;
import com.example.webservices.library.dataTransferObjects.TokenDto;
import com.example.webservices.library.dataTransferObjects.TransactionDto;
import com.example.webservices.library.exceptions.*;
import com.example.webservices.library.interfaces.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Date;
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
    public TransactionDto transfer(TransactionDto transactionDto) throws EntryNotFoundException, TokenException, BankException, InvalidTransferAmountException, DuplicateEntryException {
        return this.transfer(transactionDto.getTokenId(), transactionDto.getCreditorId(), transactionDto.getAmount(), false, transactionDto.getDescription());
    }

    public TransactionDto transfer(UUID tokenId, UUID merchantId, BigDecimal amount, boolean isRefund, String description) throws EntryNotFoundException, TokenException, BankException, InvalidTransferAmountException, DuplicateEntryException {
        if(!isGreaterThanZero(amount)){
            throw new InvalidTransferAmountException();
        }
        TokenDto token;
        try {
            token = tokenManager.GetToken(tokenId);
        }
        catch (ResponseStatusException e){
            throw new InvalidTokenException();
        }
        AccountDto merchant;
        AccountDto customer;
        try {
             merchant = accountService.getMerchant(merchantId);
             customer = accountService.getCustomer(token.getCustomerId());
        } catch (ResponseStatusException e){
            throw new EntryNotFoundException();
        }
        try {
            tokenManager.UseToken(tokenId);
        } catch (ResponseStatusException e){
            throw new UsedTokenException();
        }
        bank.transferMoney(customer, merchant, amount, description);

        TransactionDto transaction = new TransactionDto(tokenId, merchant.getAccountId(), customer.getAccountId(), amount, description, isRefund, new Date());

        try {
            transactionService.addTransaction(transaction);
        } catch (ResponseStatusException e){
            throw new RuntimeException();
        }

        return transaction;
    }

    private boolean isGreaterThanZero(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) > 0;
    }

    @Override
    public void refund(UUID transactionId) throws EntryNotFoundException, DuplicateEntryException {
        transactionService.refundTransaction(transactionId);
    }

}
