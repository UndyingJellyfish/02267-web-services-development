package com.example.webservices.application.transfers;

import com.example.webservices.application.models.Customer;
import com.example.webservices.application.models.Merchant;
import com.example.webservices.application.models.Token;
import com.example.webservices.application.models.Transaction;
import dtu.ws.fastmoney.BankServiceException_Exception;
import com.example.webservices.application.dataAccess.IAccountDatastore;
import com.example.webservices.application.bank.IBank;
import com.example.webservices.application.exceptions.EntryNotFoundException;
import com.example.webservices.application.tokens.ITokenManager;
import com.example.webservices.application.dataAccess.ITransactionDatastore;
import com.example.webservices.application.exceptions.TokenException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {
    private ITokenManager tokenManager;
    private IAccountDatastore accountDatastore;
    private ITransactionDatastore transactionDatastore;
    private IBank bank;

    public PaymentService(ITokenManager tokenManager, IAccountDatastore accountDatastore, ITransactionDatastore transactionDatastore, IBank bank) {
        this.tokenManager = tokenManager;
        this.accountDatastore = accountDatastore;
        this.transactionDatastore = transactionDatastore;
        this.bank = bank;
    }

    public Transaction transfer(UUID tokenId, UUID merchantId, BigDecimal amount, String description) throws TokenException, BankServiceException_Exception, EntryNotFoundException {
        return this.transfer(tokenId, merchantId, amount, false, description);
    }

    public Transaction transfer(UUID tokenId, UUID merchantId, BigDecimal amount, boolean isRefund, String description) throws TokenException, BankServiceException_Exception, EntryNotFoundException {
        if(!isGreaterThanZero(amount)){
            throw new IllegalArgumentException();
        }
        Token token = tokenManager.GetToken(tokenId);
        Merchant merchant = accountDatastore.getMerchant(merchantId);
        Customer customer = token.getCustomer();
        tokenManager.UseToken(tokenId);
        bank.transferMoney(customer, merchant, amount, description);
        Transaction transaction = new Transaction(merchant, customer, amount, token, isRefund);
        transactionDatastore.AddTransaction(transaction);
        return transaction;
    }

    private boolean isGreaterThanZero(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) > 0;
    }

    public void refund(UUID customerId, UUID merchantId, UUID tokenId) throws TokenException, BankServiceException_Exception, EntryNotFoundException {
        UUID newToken = tokenManager.RequestToken(accountDatastore.getCustomer(customerId));
        Transaction oldTransaction;

        oldTransaction = transactionDatastore.GetTransactionByTokenId(tokenId);
        this.transfer(newToken, merchantId, oldTransaction.getAmount(), true,"Refund");

    }

}
