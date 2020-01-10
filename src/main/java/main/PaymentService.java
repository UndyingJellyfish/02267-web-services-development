package main;

import Interfaces.IAccountDatastore;
import Interfaces.IBank;
import Interfaces.ITokenManager;
import dtu.ws.fastmoney.Bank;
import exceptions.InvalidTokenException;
import exceptions.PayException;
import exceptions.TokenException;
import models.Customer;
import models.Merchant;
import models.Token;
import models.Transaction;

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

    public Transaction pay(UUID tokenId, UUID merchantId, BigDecimal amount) throws TokenException {
        return this.pay(tokenId, merchantId, amount, false);
    }

    public Transaction pay(UUID tokenId, UUID merchantId, BigDecimal amount, boolean isRefund) throws TokenException {
        if(!isGreaterThanZero(amount)){
            throw new IllegalArgumentException();
        }
        Token token = tokenManager.GetToken(tokenId);
        Merchant merchant = accountDatastore.getMerchant(merchantId);
        Customer customer = token.getCustomer();
        tokenManager.UseToken(tokenId);
        bank.transferMoney(customer, merchant, amount);
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
            newTransaction = this.pay(newToken.getTokenId(), merchantId, oldTransaction.getAmount(), true);
        } catch (Exception e) {
            return false;
        }
        bank.transferMoney(accountDatastore.getCustomer(customerId),accountDatastore.getMerchant(merchantId),newTransaction.getAmount());
        return true;
    }

}
