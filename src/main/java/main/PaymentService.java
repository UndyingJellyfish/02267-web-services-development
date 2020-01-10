package main;

import interfaces.IAccountDatastore;
import interfaces.IBank;
import interfaces.ITokenManager;
import interfaces.ITransactionDatastore;
import exceptions.TokenException;
import models.*;
import java.math.BigDecimal;
import java.util.UUID;

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

    public Transaction transfer(UUID tokenId, UUID merchantId, BigDecimal amount, String description) throws TokenException {
        return this.transfer(tokenId, merchantId, amount, false, description);
    }

    public Transaction transfer(UUID tokenId, UUID merchantId, BigDecimal amount, boolean isRefund, String description) throws TokenException {
        if(!isGreaterThanZero(amount)){
            throw new IllegalArgumentException();
        }
        Token token = tokenManager.GetToken(tokenId);
        Merchant merchant = accountDatastore.getMerchant(merchantId);
        Customer customer = token.getCustomer();
        tokenManager.UseToken(tokenId);
        bank.transferMoney(customer, merchant, amount, "");
        Transaction transaction = new Transaction(merchant, customer, amount, token, isRefund);
        transactionDatastore.AddTransaction(transaction);
        return transaction;
    }

    private boolean isGreaterThanZero(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(0)) > 0;
    }

    public boolean refund(UUID customerId, UUID merchantId, UUID tokenId) {
        Token newToken = tokenManager.RequestToken(accountDatastore.getCustomer(customerId));
        Transaction oldTransaction, newTransaction;
        try {
            oldTransaction = transactionDatastore.GetTransactionByTokenId(tokenId);
            newTransaction = this.transfer(newToken.getTokenId(), merchantId, oldTransaction.getAmount(), true,"Refund");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
